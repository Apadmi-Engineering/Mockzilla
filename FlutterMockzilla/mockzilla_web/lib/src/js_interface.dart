// Ignoring incorrect variable names since this is the JS bridge and must match the JS properties
// ignore_for_file: non_constant_identifier_names

@JS('mockzilla.com.apadmi.mockzilla.lib.internal.jsinterface')
library;

import 'dart:js_interop';
import 'dart:js_interop_unsafe';

@JS('startMockzillaJs')
external JSPromise<JsMockzillaRuntimeParams> _startMockzillaJs(
  String appName,
  String appVersion,
  JsMockzillaConfig config,
);

@JS('stopMockzilla')
external JSPromise _stopMockzilla();

Future<JsMockzillaRuntimeParams> startMockzillaJs(
  String appName,
  String appVersion,
  JsMockzillaConfig config,
) =>
    _startMockzillaJs(appName, appVersion, config).toDart;

Future<void> stopMockzillaJs() => _stopMockzilla().toDart;

@JS('JsLogLevels')
extension type JsLogLevels._(JSObject _) implements JSObject {
  external static String get Assert;

  external static String get Debug;

  external static String get Error;

  external static String get Info;

  external static String get Verbose;

  external static String get Warn;
}

@JS('JsMockzillaHttpRequest')
extension type JsMockzillaHttpRequest._(JSObject _) implements JSObject {
  external String get uri;

  external String get headers; // JSON encoded
  external String get method;
  external JSPromise<JSAny> get bodyAsBytes;
  external JSPromise<JSString> get bodyAsString;
}

extension JsMockzillaHttpRequestExt on JsMockzillaHttpRequest {
  Future<String> bodyAsStringFuture() =>
      bodyAsString.toDart.then((it) => it.toDart);
}

@JS('Map')
extension type JsMap<K extends JSAny, V extends JSAny>._(JSObject _) {
  external JsMap();
  external V? get(K key);
  external void set(K key, V value);
  external void forEach(JSFunction f);
}

@JS('JsMockzillaHttpResponse')
extension type JsMockzillaHttpResponse._(JSObject _) implements JSObject {
  external JsMockzillaHttpResponse(
      int statusCode, JSObject? headers, String? body);

  external int get statusCode;
  external JsMap get headers; // JS object (map-like)
  external String get body;
}

@JS('JsDashboardOverridePreset')
extension type JsDashboardOverridePreset._(JSObject _) implements JSObject {
  external JsDashboardOverridePreset(
    String name,
    String? description,
    JsMockzillaHttpResponse response,
  );

  external String get name;
  external String? get description;
  external JsMockzillaHttpResponse get response;
}

@JS('JsDashboardOptionsConfig')
extension type JsDashboardOptionsConfig._(JSObject _) implements JSObject {
  external JsDashboardOptionsConfig(JSArray<JsDashboardOverridePreset> presets);

  external JSArray<JsDashboardOverridePreset> get presets;
}

typedef JsEndpointMatcher = Future<bool> Function(JsMockzillaHttpRequest req);
typedef JsEndpointHandler = JsMockzillaHttpResponse Function(
  JsMockzillaHttpRequest req,
);

@JS('JsEndpointConfiguration')
extension type JsEndpointConfiguration._(JSObject _) implements JSObject {
  external JsEndpointConfiguration(
    String name,
    String key,
    bool shouldFail,
    int? delay,
    JsDashboardOptionsConfig dashboardOptionsConfig,
    int versionCode,
    JSExportedDartFunction endpointMatcher,
    JSExportedDartFunction defaultHandler,
    JSExportedDartFunction errorHandler,
  );

  external String get name;

  external String get key;

  external bool get shouldFail;

  external int? get delay;

  external JsDashboardOptionsConfig get dashboardOptionsConfig;

  external int get versionCode;

  external JSExportedDartFunction get endpointMatcher;

  external JSExportedDartFunction get defaultHandler;

  external JSExportedDartFunction get errorHandler;
}

/// JsMockzillaConfig
@JS('JsMockzillaConfig')
extension type JsMockzillaConfig._(JSObject _) implements JSObject {
  external JsMockzillaConfig(
      JSArray<JsEndpointConfiguration> endpoints, String logLevel);

  external JSArray<JsEndpointConfiguration> get endpoints;

  external String get logLevel;
}

@JS('JsMockzillaRuntimeParams')
extension type JsMockzillaRuntimeParams._(JSObject _) implements JSObject {
  external JsMockzillaConfig get config;

  external String get ip;

  external String get mockBaseUrl;

  external String get apiBaseUrl;

  external int get port;

  external String get mockzillaVersion;
}

JsMockzillaHttpResponse buildResponse({
  required int statusCode,
  required Map<String, String> headers,
  required String body,
}) {
  final jsHeaders = JSObject();
  headers.forEach((key, value) {
    jsHeaders[key] = value.toJS;
  });
  return JsMockzillaHttpResponse(statusCode, jsHeaders, body);
}

JsDashboardOptionsConfig buildDashboardOptions(
  List<JsDashboardOverridePreset> presets,
) =>
    JsDashboardOptionsConfig(presets.toJS);

JsDashboardOverridePreset buildPreset({
  required String name,
  String? description,
  required JsMockzillaHttpResponse response,
}) =>
    JsDashboardOverridePreset(name, description, response);

JsMockzillaConfig buildConfig({
  required List<JsEndpointConfiguration> endpoints,
  required String logLevel,
}) =>
    JsMockzillaConfig(endpoints.toJS, logLevel);
