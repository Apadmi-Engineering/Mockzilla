import type { EndpointConfig, MockzillaConfig } from '../types';
import { HttpMethod } from '../types';
import { Mockzilla } from '../Mockzilla';
import NativeMockzillaModule from '../specs/NativeMockzillaModule';
import { NativeEventEmitter } from 'react-native';

// Must be hoisted before imports via babel-plugin-jest-hoist
jest.mock('../specs/NativeMockzillaModule', () => ({
  __esModule: true,
  default: {
    startMockzilla: jest.fn(),
    stopMockzilla: jest.fn(),
    respondToMatcher: jest.fn(),
    respondToHandler: jest.fn(),
    addListener: jest.fn(),
    removeListeners: jest.fn(),
  },
}));

let capturedListener: ((event: unknown) => void) | null = null;
const mockRemove = jest.fn();

jest.mock('react-native', () => ({
  NativeEventEmitter: jest.fn().mockImplementation(() => ({
    addListener: jest
      .fn()
      .mockImplementation((_: string, cb: (e: unknown) => void) => {
        capturedListener = cb;
        return { remove: mockRemove };
      }),
  })),
}));

const mockRuntimeParams = {
  mockBaseUrl: 'http://localhost:8080',
  apiBaseUrl: 'http://api.example.com',
  port: 8080,
};

function makeEndpointConfig(
  overrides: Partial<EndpointConfig> = {}
): EndpointConfig {
  return {
    key: 'test-endpoint',
    endpointMatcher: jest.fn().mockResolvedValue(true),
    defaultHandler: jest
      .fn()
      .mockResolvedValue({ statusCode: 200, headers: {}, body: '{"ok":true}' }),
    errorHandler: jest
      .fn()
      .mockResolvedValue({ statusCode: 503, headers: {}, body: 'error' }),
    ...overrides,
  };
}

const baseRequestEvent = {
  requestId: 'req-1',
  key: 'test-endpoint',
  type: 'defaultHandler' as const,
  request: {
    uri: '/api/test',
    headers: { Accept: 'application/json' },
    body: '',
    method: 'GET',
  },
};

const flushPromises = () =>
  new Promise<void>((resolve) => process.nextTick(resolve));

async function dispatch(event: {
  requestId: string;
  key: string;
  type: 'endpointMatcher' | 'defaultHandler' | 'errorHandler';
  request: {
    uri: string;
    headers: Record<string, string>;
    body: string;
    method: string;
  };
}): Promise<void> {
  capturedListener!(event);
  await flushPromises();
}

beforeEach(() => {
  (NativeMockzillaModule.startMockzilla as jest.Mock).mockResolvedValue(
    mockRuntimeParams
  );
  (NativeMockzillaModule.stopMockzilla as jest.Mock).mockResolvedValue(
    undefined
  );
});

afterEach(async () => {
  await Mockzilla.stopMockzilla();
  capturedListener = null;
  jest.clearAllMocks();
});

describe('startMockzilla', () => {
  it('calls NativeMockzillaModule.startMockzilla with mapped config', async () => {
    const ep = makeEndpointConfig({
      name: 'My Endpoint',
      shouldFail: true,
      delayMs: 100,
      versionCode: 1,
      dashboardOptionsConfig: {
        presets: [
          {
            name: 'preset1',
            description: 'desc',
            response: { statusCode: 201, headers: { 'X-H': '1' }, body: 'b' },
          },
        ],
      },
    });
    const config: MockzillaConfig = {
      port: 9090,
      localHostOnly: true,
      logLevel: undefined,
      isNetworkDiscoveryEnabled: false,
      endpoints: [ep],
    };

    await Mockzilla.startMockzilla(config);

    expect(NativeMockzillaModule.startMockzilla).toHaveBeenCalledWith({
      port: 9090,
      localHostOnly: true,
      logLevel: null,
      isNetworkDiscoveryEnabled: false,
      endpoints: [
        {
          key: 'test-endpoint',
          name: 'My Endpoint',
          shouldFail: true,
          delayMs: 100,
          versionCode: 1,
          presets: [
            {
              name: 'preset1',
              description: 'desc',
              statusCode: 201,
              headers: { 'X-H': '1' },
              body: 'b',
            },
          ],
        },
      ],
    });
  });

  it('returns runtime params from native module', async () => {
    const result = await Mockzilla.startMockzilla({ endpoints: [] });
    expect(result).toEqual(mockRuntimeParams);
  });

  it('uses null for optional config fields when omitted', async () => {
    await Mockzilla.startMockzilla({ endpoints: [] });
    expect(NativeMockzillaModule.startMockzilla).toHaveBeenCalledWith(
      expect.objectContaining({
        port: null,
        localHostOnly: null,
        logLevel: null,
        isNetworkDiscoveryEnabled: null,
      })
    );
  });

  it('maps endpoint name to key when name is absent', async () => {
    await Mockzilla.startMockzilla({
      endpoints: [makeEndpointConfig({ key: 'my-ep', name: undefined })],
    });
    expect(NativeMockzillaModule.startMockzilla).toHaveBeenCalledWith(
      expect.objectContaining({
        endpoints: [expect.objectContaining({ key: 'my-ep', name: 'my-ep' })],
      })
    );
  });

  it('maps dashboard presets applying defaults for missing fields', async () => {
    await Mockzilla.startMockzilla({
      endpoints: [
        makeEndpointConfig({
          dashboardOptionsConfig: {
            presets: [{ name: 'p', response: {} }],
          },
        }),
      ],
    });
    expect(NativeMockzillaModule.startMockzilla).toHaveBeenCalledWith(
      expect.objectContaining({
        endpoints: [
          expect.objectContaining({
            presets: [
              { name: 'p', description: '', statusCode: 200, headers: {}, body: '' },
            ],
          }),
        ],
      })
    );
  });

  it('creates NativeEventEmitter with NativeMockzillaModule', async () => {
    await Mockzilla.startMockzilla({ endpoints: [] });
    expect(NativeEventEmitter).toHaveBeenCalledWith(NativeMockzillaModule);
  });

  it('subscribes to MockzillaRequest events', async () => {
    await Mockzilla.startMockzilla({ endpoints: [] });
    const emitterInstance = (NativeEventEmitter as jest.Mock).mock.results[0]!
      .value as { addListener: jest.Mock };
    expect(emitterInstance.addListener).toHaveBeenCalledWith(
      'MockzillaRequest',
      expect.any(Function)
    );
  });

  it('clears endpoint map before re-registering on repeated calls', async () => {
    const firstEp = makeEndpointConfig({ key: 'first-ep' });
    await Mockzilla.startMockzilla({ endpoints: [firstEp] });
    capturedListener = null;

    const secondEp = makeEndpointConfig({ key: 'second-ep' });
    await Mockzilla.startMockzilla({ endpoints: [secondEp] });

    await dispatch({ ...baseRequestEvent, key: 'first-ep', type: 'defaultHandler' });

    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      expect.objectContaining({ statusCode: 500, body: 'Unknown endpoint key: first-ep' })
    );
  });
});

describe('stopMockzilla', () => {
  it('calls NativeMockzillaModule.stopMockzilla', async () => {
    await Mockzilla.startMockzilla({ endpoints: [] });
    await Mockzilla.stopMockzilla();
    expect(NativeMockzillaModule.stopMockzilla).toHaveBeenCalledTimes(1);
  });

  it('removes the event subscription', async () => {
    await Mockzilla.startMockzilla({ endpoints: [] });
    await Mockzilla.stopMockzilla();
    expect(mockRemove).toHaveBeenCalledTimes(1);
  });

  it('can be called without a prior startMockzilla without throwing', async () => {
    await expect(Mockzilla.stopMockzilla()).resolves.toBeUndefined();
  });
});

describe('_dispatch — endpointMatcher type', () => {
  let ep: EndpointConfig;

  beforeEach(async () => {
    ep = makeEndpointConfig();
    await Mockzilla.startMockzilla({ endpoints: [ep] });
  });

  it('calls ep.endpointMatcher with the mapped request', async () => {
    await dispatch({ ...baseRequestEvent, type: 'endpointMatcher' });
    expect(ep.endpointMatcher).toHaveBeenCalledWith({
      uri: '/api/test',
      headers: { Accept: 'application/json' },
      body: '',
      method: HttpMethod.GET,
    });
  });

  it('calls respondToMatcher with true when matcher returns true', async () => {
    (ep.endpointMatcher as jest.Mock).mockResolvedValue(true);
    await dispatch({ ...baseRequestEvent, type: 'endpointMatcher' });
    expect(NativeMockzillaModule.respondToMatcher).toHaveBeenCalledWith(
      'req-1',
      true
    );
  });

  it('calls respondToMatcher with false when matcher returns false', async () => {
    (ep.endpointMatcher as jest.Mock).mockResolvedValue(false);
    await dispatch({ ...baseRequestEvent, type: 'endpointMatcher' });
    expect(NativeMockzillaModule.respondToMatcher).toHaveBeenCalledWith(
      'req-1',
      false
    );
  });

  it('calls respondToMatcher with false for unknown endpoint key', async () => {
    await dispatch({ ...baseRequestEvent, key: 'unknown-key', type: 'endpointMatcher' });
    expect(NativeMockzillaModule.respondToMatcher).toHaveBeenCalledWith(
      'req-1',
      false
    );
    expect(ep.endpointMatcher).not.toHaveBeenCalled();
  });

  it('handles synchronous false return from matcher', async () => {
    (ep.endpointMatcher as jest.Mock).mockReturnValue(false);
    await dispatch({ ...baseRequestEvent, type: 'endpointMatcher' });
    expect(NativeMockzillaModule.respondToMatcher).toHaveBeenCalledWith(
      'req-1',
      false
    );
  });

  it('calls respondToHandler with 500 when matcher throws', async () => {
    (ep.endpointMatcher as jest.Mock).mockRejectedValue(new Error('boom'));
    await dispatch({ ...baseRequestEvent, type: 'endpointMatcher' });
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Handler threw: Error: boom' }
    );
  });
});

describe('_dispatch — defaultHandler type', () => {
  let ep: EndpointConfig;

  beforeEach(async () => {
    ep = makeEndpointConfig();
    await Mockzilla.startMockzilla({ endpoints: [ep] });
  });

  it('calls ep.defaultHandler with the mapped request', async () => {
    await dispatch(baseRequestEvent);
    expect(ep.defaultHandler).toHaveBeenCalledWith({
      uri: '/api/test',
      headers: { Accept: 'application/json' },
      body: '',
      method: HttpMethod.GET,
    });
  });

  it('calls respondToHandler with the full handler response', async () => {
    (ep.defaultHandler as jest.Mock).mockResolvedValue({
      statusCode: 201,
      headers: { 'X-Custom': '1' },
      body: 'hello',
    });
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 201, headers: { 'X-Custom': '1' }, body: 'hello' }
    );
  });

  it('defaults statusCode to 200 when omitted', async () => {
    (ep.defaultHandler as jest.Mock).mockResolvedValue({
      headers: {},
      body: 'hi',
    });
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      expect.objectContaining({ statusCode: 200 })
    );
  });

  it('defaults headers to {} when omitted', async () => {
    (ep.defaultHandler as jest.Mock).mockResolvedValue({
      statusCode: 204,
      body: '',
    });
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      expect.objectContaining({ headers: {} })
    );
  });

  it('defaults body to empty string when omitted', async () => {
    (ep.defaultHandler as jest.Mock).mockResolvedValue({
      statusCode: 200,
      headers: {},
    });
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      expect.objectContaining({ body: '' })
    );
  });

  it('applies all three defaults when handler returns {}', async () => {
    (ep.defaultHandler as jest.Mock).mockResolvedValue({});
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 200, headers: {}, body: '' }
    );
  });

  it('calls respondToHandler with 500 for unknown endpoint key', async () => {
    await dispatch({ ...baseRequestEvent, key: 'unknown-key' });
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Unknown endpoint key: unknown-key' }
    );
    expect(ep.defaultHandler).not.toHaveBeenCalled();
  });

  it('calls respondToHandler with 500 when defaultHandler throws', async () => {
    (ep.defaultHandler as jest.Mock).mockRejectedValue(new Error('fail'));
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Handler threw: Error: fail' }
    );
  });

  it('handles non-Error throwables (string rejection)', async () => {
    (ep.defaultHandler as jest.Mock).mockRejectedValue('plain string');
    await dispatch(baseRequestEvent);
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Handler threw: plain string' }
    );
  });
});

describe('_dispatch — errorHandler type', () => {
  let ep: EndpointConfig;

  beforeEach(async () => {
    ep = makeEndpointConfig();
    await Mockzilla.startMockzilla({ endpoints: [ep] });
  });

  it('calls ep.errorHandler with the mapped request', async () => {
    await dispatch({ ...baseRequestEvent, type: 'errorHandler' });
    expect(ep.errorHandler).toHaveBeenCalledWith({
      uri: '/api/test',
      headers: { Accept: 'application/json' },
      body: '',
      method: HttpMethod.GET,
    });
  });

  it('calls respondToHandler with the error handler response', async () => {
    (ep.errorHandler as jest.Mock).mockResolvedValue({
      statusCode: 503,
      headers: { 'Retry-After': '10' },
      body: 'retry',
    });
    await dispatch({ ...baseRequestEvent, type: 'errorHandler' });
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 503, headers: { 'Retry-After': '10' }, body: 'retry' }
    );
  });

  it('calls respondToHandler with 500 for unknown endpoint key', async () => {
    await dispatch({
      ...baseRequestEvent,
      key: 'unknown-key',
      type: 'errorHandler',
    });
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Unknown endpoint key: unknown-key' }
    );
    expect(ep.errorHandler).not.toHaveBeenCalled();
  });

  it('calls respondToHandler with 500 when errorHandler throws', async () => {
    (ep.errorHandler as jest.Mock).mockRejectedValue(new Error('err'));
    await dispatch({ ...baseRequestEvent, type: 'errorHandler' });
    expect(NativeMockzillaModule.respondToHandler).toHaveBeenCalledWith(
      'req-1',
      { statusCode: 500, headers: {}, body: 'Handler threw: Error: err' }
    );
  });
});
