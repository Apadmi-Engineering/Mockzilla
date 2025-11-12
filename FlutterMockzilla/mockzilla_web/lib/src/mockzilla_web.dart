import 'dart:async';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'package:mockzilla_web/src/version_json_utils.dart';
import 'package:web/web.dart' as web;

import 'dart:js_interop';
import 'dart:convert';

import 'package:mockzilla_web/src/js_interface.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaWeb extends MockzillaPlatform {
  static void registerWith(Registrar registrar) {
    MockzillaPlatform.instance = MockzillaWeb();
  }

  bool _scriptLoaded = false;

  Future<void> _ensureScriptLoaded() async {
    if (_scriptLoaded) return;

    final web.Document document = web.window.document;

    final web.HTMLScriptElement script =
        document.createElement('script') as web.HTMLScriptElement;
    script.src = 'packages/mockzilla_web/assets/mockzilla.js';
    script.type = 'text/javascript';

    final completer = Completer<void>();
    script.onload = (() {
      completer.complete();
    }).toJS;

    script.onerror = ((web.Event error) {
      completer.completeError(
          Exception('Failed to load mockzilla.js: ${error.toString()}'));
    }).toJS;

    final web.HTMLElement? head = document.head;

    if (head != null) {
      head.appendChild(script);
      await completer.future;
    } else {
      completer.completeError(
          Exception('Could not find the document head element.'));
      await completer.future;
    }

    _scriptLoaded = true;
  }

  @override
  Future<MockzillaRuntimeParams> startMockzilla(MockzillaConfig config) async {
    await _ensureScriptLoaded();

    final jsConfig = _toJsConfig(config);
    final versionJson = await VersionJsonUtils.getVersionJson();

    final jsResult = await startMockzillaJs(
      versionJson["app_name"],
      versionJson["version"],
      jsConfig,
    );

    return _fromJsRuntimeParams(config, jsResult);
  }

  @override
  Future<void> stopMockzilla() async {
    await _ensureScriptLoaded();
    await stopMockzilla();
  }

  JsMockzillaConfig _toJsConfig(MockzillaConfig config) {
    final jsEndpoints = config.endpoints.map(_toJsEndpoint).toList();
    final jsLogLevel = _mapLogLevel(config.logLevel);
    return buildConfig(endpoints: jsEndpoints, logLevel: jsLogLevel);
  }

  JsEndpointConfiguration _toJsEndpoint(EndpointConfig endpoint) {
    final dashboardPresets = [
      ...endpoint.dashboardOptionsConfig.successPresets.map(_toJsPreset),
      ...endpoint.dashboardOptionsConfig.errorPresets.map(_toJsPreset),
    ];

    final jsDashboard = buildDashboardOptions(dashboardPresets);

    JSPromise<JSAny?> endpointMatcher(JsMockzillaHttpRequest req) =>
        Future<JSBoolean>(() async {
          final dartReq = await _toDartRequest(req);
          final res = await endpoint.endpointMatcher(dartReq);
          return res.toJS;
        }).toJS;

    JSPromise<JSAny?> defaultHandler(JsMockzillaHttpRequest req) =>
        Future<JsMockzillaHttpResponse>(() async {
          final dartReq = await _toDartRequest(req);
          final res = await endpoint.defaultHandler(dartReq);
          return _toJsResponse(res);
        }).toJS;

    JSPromise<JSAny?> errorHandler(JsMockzillaHttpRequest req) =>
        Future<JsMockzillaHttpResponse>(() async {
          final dartReq = await _toDartRequest(req);
          final res = await endpoint.errorHandler(dartReq);
          return _toJsResponse(res);
        }).toJS;

    return JsEndpointConfiguration(
      endpoint.name,
      endpoint.key,
      endpoint.shouldFail,
      endpoint.delay.inMilliseconds,
      jsDashboard,
      endpoint.versionCode,
      endpointMatcher.toJS,
      defaultHandler.toJS,
      errorHandler.toJS,
    );
  }

  Future<MockzillaHttpRequest> _toDartRequest(
      JsMockzillaHttpRequest req) async {
    final stringMap = Map.castFrom(json.decode(req.headers)).map((key, value) {
      return MapEntry(key.toString(), value.toString());
    });
    return MockzillaHttpRequest(
      uri: req.uri,
      headers: stringMap,
      method: HttpMethod.values.firstWhere(
        (m) => m.name.toUpperCase() == req.method.toUpperCase(),
        orElse: () => HttpMethod.get,
      ),
      body: await req.bodyAsStringFuture(),
    );
  }

  JsMockzillaHttpResponse _toJsResponse(MockzillaHttpResponse response) {
    return buildResponse(
      statusCode: response.statusCode,
      headers: response.headers,
      body: response.body,
    );
  }

  JsDashboardOverridePreset _toJsPreset(DashboardOverridePreset preset) {
    return buildPreset(
      name: preset.name,
      description: preset.description,
      response: _toJsResponse(preset.response),
    );
  }

  MockzillaRuntimeParams _fromJsRuntimeParams(
      MockzillaConfig config, JsMockzillaRuntimeParams js) {
    return MockzillaRuntimeParams(
      config: config,
      mockBaseUrl: js.mockBaseUrl,
      apiBaseUrl: js.apiBaseUrl,
      port: js.port,
    );
  }

  String _mapLogLevel(LogLevel level) {
    switch (level) {
      case LogLevel.debug:
        return JsLogLevels.Debug;
      case LogLevel.error:
        return JsLogLevels.Error;
      case LogLevel.info:
        return JsLogLevels.Info;
      case LogLevel.verbose:
        return JsLogLevels.Verbose;
      case LogLevel.warn:
        return JsLogLevels.Warn;
      case LogLevel.assertion:
        return JsLogLevels.Assert;
    }
  }
}
