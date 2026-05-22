export enum HttpMethod {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
  PATCH = 'PATCH',
  HEAD = 'HEAD',
  OPTIONS = 'OPTIONS',
}

export enum LogLevel {
  DEBUG = 'DEBUG',
  ERROR = 'ERROR',
  INFO = 'INFO',
  VERBOSE = 'VERBOSE',
  WARN = 'WARN',
  ASSERT = 'ASSERT',
}

export interface MockzillaHttpRequest {
  uri: string;
  headers: Record<string, string>;
  body: string;
  method: HttpMethod;
}

export interface MockzillaHttpResponse {
  statusCode?: number;
  headers?: Record<string, string>;
  body?: string;
}

export interface DashboardOverridePreset {
  name: string;
  description?: string;
  response: Partial<MockzillaHttpResponse>;
}

export interface DashboardOptionsConfig {
  presets?: DashboardOverridePreset[];
}

export interface EndpointConfig {
  key: string;
  name?: string;
  shouldFail?: boolean;
  delayMs?: number;
  versionCode?: number;
  dashboardOptionsConfig?: DashboardOptionsConfig;
  endpointMatcher: (req: MockzillaHttpRequest) => boolean | Promise<boolean>;
  defaultHandler: (req: MockzillaHttpRequest) => MockzillaHttpResponse | Promise<MockzillaHttpResponse>;
  errorHandler: (req: MockzillaHttpRequest) => MockzillaHttpResponse | Promise<MockzillaHttpResponse>;
}

export interface MockzillaConfig {
  port?: number;
  endpoints: EndpointConfig[];
  localHostOnly?: boolean;
  logLevel?: LogLevel;
  isNetworkDiscoveryEnabled?: boolean;
}

export interface MockzillaRuntimeParams {
  mockBaseUrl: string;
  apiBaseUrl: string;
  port: number;
}
