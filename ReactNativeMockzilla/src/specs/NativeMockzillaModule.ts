import type { TurboModule, CodegenTypes } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export type NativeDashboardPreset = {
  name: string;
  description: string;
  statusCode: CodegenTypes.Int32;
  headers: Object;
  body: string;
};

export type NativeEndpointConfig = {
  key: string;
  name: string;
  shouldFail: boolean | null;
  delayMs: CodegenTypes.Int32 | null;
  versionCode: CodegenTypes.Int32 | null;
  presets: ReadonlyArray<NativeDashboardPreset>;
};

export type NativeMockzillaConfig = {
  port: CodegenTypes.Int32 | null;
  endpoints: ReadonlyArray<NativeEndpointConfig>;
  localHostOnly: boolean | null;
  logLevel: string | null;
  isNetworkDiscoveryEnabled: boolean | null;
};

export type NativeMockzillaRuntimeParams = {
  mockBaseUrl: string;
  apiBaseUrl: string;
  port: CodegenTypes.Int32;
};

export type NativeHttpResponse = {
  statusCode: CodegenTypes.Int32;
  headers: Object;
  body: string;
};

export interface Spec extends TurboModule {
  // `config` and `response` are passed as `UnsafeObject` (not the named struct
  // types above) on purpose. A named object parameter makes iOS codegen emit a
  // strongly-typed C++ struct passed by reference (e.g.
  // `JS::NativeMockzillaModule::NativeMockzillaConfig &`), which conflicts with
  // the `NSDictionary *` signatures in the native module and crashes at runtime.
  // `UnsafeObject` keeps the native boundary dictionary-based on every platform
  // (iOS `NSDictionary *`, Android `ReadableMap`, Swift `[String: Any]`). The
  // shapes are still described by NativeMockzillaConfig / NativeHttpResponse and
  // enforced by the public Mockzilla API.
  startMockzilla(config: CodegenTypes.UnsafeObject): Promise<NativeMockzillaRuntimeParams>;
  stopMockzilla(): Promise<void>;
  respondToMatcher(requestId: string, matches: boolean): void;
  respondToHandler(requestId: string, response: CodegenTypes.UnsafeObject): void;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('MockzillaModule');
