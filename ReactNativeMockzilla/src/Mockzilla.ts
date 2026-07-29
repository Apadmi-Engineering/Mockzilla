import { NativeEventEmitter } from 'react-native';
import NativeMockzillaModule from './specs/NativeMockzillaModule';
import type {
  MockzillaConfig,
  MockzillaRuntimeParams,
  MockzillaHttpRequest,
  EndpointConfig,
} from './types';
import { HttpMethod } from './types';

const TAG = 'NativeMockzilla';

/* eslint-disable no-console */
const log = {
  debug: (message: string) => console.debug(`[${TAG}] ${message}`),
  info: (message: string) => console.info(`[${TAG}] ${message}`),
  warn: (message: string) => console.warn(`[${TAG}] ${message}`),
  error: (message: string, err?: unknown) =>
    console.error(`[${TAG}] ${message}`, err ?? ''),
};
/* eslint-enable no-console */

interface RequestEvent {
  requestId: string;
  key: string;
  type: 'endpointMatcher' | 'defaultHandler' | 'errorHandler';
  request: {
    uri: string;
    headers: Record<string, string>;
    body: string;
    method: string;
  };
}

export class Mockzilla {
  private static _endpointMap = new Map<string, EndpointConfig>();
  private static _emitter: NativeEventEmitter | null = null;
  private static _sub: ReturnType<NativeEventEmitter['addListener']> | null =
    null;

  static async startMockzilla(
    config: MockzillaConfig
  ): Promise<MockzillaRuntimeParams> {
    log.debug(
      `startMockzilla: registering ${config.endpoints.length} endpoint(s)`
    );
    this._endpointMap.clear();
    for (const ep of config.endpoints) {
      if (this._endpointMap.has(ep.key)) {
        log.warn(
          `startMockzilla: duplicate endpoint key "${ep.key}", overwriting previous registration`
        );
      }
      this._endpointMap.set(ep.key, ep);
    }

    this._emitter = new NativeEventEmitter(NativeMockzillaModule);
    this._sub = this._emitter.addListener('MockzillaRequest', (event) => {
      this._dispatch(event as RequestEvent).catch((err) => {
        // _dispatch already catches handler/matcher errors internally, so
        // reaching here means something in the dispatch plumbing itself
        // failed (e.g. respondToMatcher/respondToHandler threw). Log it so
        // it isn't silently swallowed and the native side doesn't hang.
        log.error(
          `startMockzilla: unhandled error dispatching event for requestId=${
            (event as RequestEvent)?.requestId
          }`,
          err
        );
      });
    });

    try {
      const result = await NativeMockzillaModule.startMockzilla({
        port: config.port ?? null,
        localHostOnly: config.localHostOnly ?? null,
        logLevel: config.logLevel ?? null,
        isNetworkDiscoveryEnabled: config.isNetworkDiscoveryEnabled ?? null,
        endpoints: config.endpoints.map((ep) => ({
          key: ep.key,
          name: ep.name ?? ep.key,
          shouldFail: ep.shouldFail ?? null,
          delayMs: ep.delayMs ?? null,
          versionCode: ep.versionCode ?? null,
          presets:
            ep.dashboardOptionsConfig?.presets?.map((p) => ({
              name: p.name,
              description: p.description ?? '',
              statusCode: p.response.statusCode ?? 200,
              headers: p.response.headers ?? {},
              body: p.response.body ?? '',
            })) ?? [],
        })),
      });

      log.info(
        `startMockzilla: server started (mockBaseUrl=${
          (result as MockzillaRuntimeParams).mockBaseUrl
        })`
      );
      return result as MockzillaRuntimeParams;
    } catch (err) {
      log.error('startMockzilla: native module failed to start server', err);
      throw err;
    }
  }

  static async stopMockzilla(): Promise<void> {
    log.debug('stopMockzilla: tearing down listener and stopping server');
    this._sub?.remove();
    this._sub = null;
    this._emitter = null;
    this._endpointMap.clear();
    try {
      await NativeMockzillaModule.stopMockzilla();
      log.info('stopMockzilla: server stopped');
    } catch (err) {
      log.error('stopMockzilla: native module failed to stop server', err);
      throw err;
    }
  }

  private static async _dispatch(event: RequestEvent): Promise<void> {
    const { requestId, key, type, request } = event;
    log.debug(`_dispatch: received ${type} event (key=${key}, id=${requestId})`);
    const ep = this._endpointMap.get(key);

    if (!ep) {
      log.warn(
        `_dispatch: no registered endpoint for key="${key}" (id=${requestId}). ` +
          'Was it registered in startMockzilla?'
      );
      type === 'endpointMatcher'
        ? NativeMockzillaModule.respondToMatcher(requestId, false)
        : NativeMockzillaModule.respondToHandler(requestId, {
            statusCode: 500,
            headers: {},
            body: `Unknown endpoint key: ${key}`,
          });
      return;
    }

    const req: MockzillaHttpRequest = {
      ...request,
      method: request.method as HttpMethod,
    };

    try {
      if (type === 'endpointMatcher') {
        const matches = await ep.endpointMatcher(req);
        log.debug(
          `_dispatch: endpointMatcher resolved (key=${key}, id=${requestId}, matches=${matches})`
        );
        NativeMockzillaModule.respondToMatcher(requestId, matches);
      } else {
        const fn =
          type === 'defaultHandler' ? ep.defaultHandler : ep.errorHandler;
        const res = await fn(req);
        log.debug(
          `_dispatch: ${type} resolved (key=${key}, id=${requestId}, statusCode=${
            res.statusCode ?? 200
          })`
        );
        NativeMockzillaModule.respondToHandler(requestId, {
          statusCode: res.statusCode ?? 200,
          headers: res.headers ?? {},
          body: res.body ?? '',
        });
      }
    } catch (err) {
      // IMPORTANT: always respond via the channel matching `type`. Responding
      // on the wrong channel (e.g. respondToHandler for a matcher failure)
      // leaves the native side's matching deferred uncompleted forever,
      // hanging the underlying HTTP request indefinitely.
      if (type === 'endpointMatcher') {
        log.error(
          `_dispatch: endpointMatcher threw (key=${key}, id=${requestId}). Treating as no-match.`,
          err
        );
        NativeMockzillaModule.respondToMatcher(requestId, false);
      } else {
        log.error(
          `_dispatch: ${type} threw (key=${key}, id=${requestId}). Responding with 500.`,
          err
        );
        NativeMockzillaModule.respondToHandler(requestId, {
          statusCode: 500,
          headers: {},
          body: `Handler threw: ${err}`,
        });
      }
    }
  }
}
