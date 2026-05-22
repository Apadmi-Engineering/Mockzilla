import type { TurboModule } from 'react-native';
import type { Int32 } from 'react-native/Libraries/Types/CodegenTypes';
import { TurboModuleRegistry } from 'react-native';

export type NativeDashboardPreset = {
  name: string;
  description: string;
  statusCode: Int32;
  headers: Object;
  body: string;
};

export type NativeEndpointConfig = {
  key: string;
  name: string;
  shouldFail: boolean | null;
  delayMs: Int32 | null;
  versionCode: Int32 | null;
  presets: ReadonlyArray<NativeDashboardPreset>;
};

export type NativeMockzillaConfig = {
  port: Int32 | null;
  endpoints: ReadonlyArray<NativeEndpointConfig>;
  localHostOnly: boolean | null;
  logLevel: string | null;
  isNetworkDiscoveryEnabled: boolean | null;
};

export type NativeMockzillaRuntimeParams = {
  mockBaseUrl: string;
  apiBaseUrl: string;
  port: Int32;
};

export type NativeHttpResponse = {
  statusCode: Int32;
  headers: Object;
  body: string;
};

export interface Spec extends TurboModule {
  startMockzilla(config: NativeMockzillaConfig): Promise<NativeMockzillaRuntimeParams>;
  stopMockzilla(): Promise<void>;
  respondToMatcher(requestId: string, matches: boolean): void;
  respondToHandler(requestId: string, response: NativeHttpResponse): void;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('MockzillaModule');
