import { NativeEventEmitter } from 'react-native';
import NativeMockzillaModule from './specs/NativeMockzillaModule';
import type {
  MockzillaConfig,
  MockzillaRuntimeParams,
  MockzillaHttpRequest,
  EndpointConfig,
} from './types';
import { HttpMethod } from './types';

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
    this._endpointMap.clear();
    for (const ep of config.endpoints) {
      this._endpointMap.set(ep.key, ep);
    }

    this._emitter = new NativeEventEmitter(NativeMockzillaModule);
    this._sub = this._emitter.addListener(
      'MockzillaRequest',
      (event: RequestEvent) => void this._dispatch(event)
    );

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

    return result as MockzillaRuntimeParams;
  }

  static async stopMockzilla(): Promise<void> {
    this._sub?.remove();
    this._sub = null;
    this._emitter = null;
    this._endpointMap.clear();
    return NativeMockzillaModule.stopMockzilla();
  }

  private static async _dispatch(event: RequestEvent): Promise<void> {
    const { requestId, key, type, request } = event;
    const ep = this._endpointMap.get(key);

    if (!ep) {
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
        NativeMockzillaModule.respondToMatcher(
          requestId,
          await ep.endpointMatcher(req)
        );
      } else {
        const fn =
          type === 'defaultHandler' ? ep.defaultHandler : ep.errorHandler;
        const res = await fn(req);
        NativeMockzillaModule.respondToHandler(requestId, {
          statusCode: res.statusCode ?? 200,
          headers: res.headers ?? { 'Content-Type': 'application/json' },
          body: res.body ?? '',
        });
      }
    } catch (err) {
      NativeMockzillaModule.respondToHandler(requestId, {
        statusCode: 500,
        headers: {},
        body: `Handler threw: ${err}`,
      });
    }
  }
}
