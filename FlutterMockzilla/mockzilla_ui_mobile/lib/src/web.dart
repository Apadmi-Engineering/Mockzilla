import 'package:mockzilla_ui_mobile/mockzilla_ui_mobile.dart';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'package:web/web.dart' as web;
import 'dart:async';

import 'dart:js_interop';

class MockzillaMobileUiWeb extends MockzillaUiMobilePlatform {
  static void registerWith(Registrar registrar) {
    MockzillaUiMobilePlatform.instance = MockzillaMobileUiWeb();
  }

  bool _scriptLoaded = false;

  Future<void> _ensureScriptLoaded() async {
    if (_scriptLoaded) return;

    final web.Document document = web.window.document;

    final web.HTMLScriptElement script =
        document.createElement('script') as web.HTMLScriptElement;
    script.src = 'packages/mockzilla_ui_mobile/assets/mockzilla-mobile-ui.js';
    script.type = 'text/javascript';

    final completer = Completer<void>();
    script.onload = (() {
      completer.complete();
    }).toJS;

    script.onerror = ((web.Event error) {
      completer.completeError(Exception(
          'Failed to load mockzilla-mobile-ui.js: ${error.toString()}'));
    }).toJS;

    final web.HTMLElement? head = document.head;

    if (head != null) {
      head.appendChild(script);

      // Seems to take a little extra to initialize itself
      await Future.delayed(Duration(milliseconds: 400));
      await completer.future;
    } else {
      completer.completeError(
          Exception('Could not find the document head element.'));
      await completer.future;
    }

    _scriptLoaded = true;
  }

  @override
  void launchManagementUi() async {
    await _ensureScriptLoaded();

    dynamic root = web.window["mockzilla-mobile-ui"];
    root.com.apadmi.mockzilla.launchManagementUi();
  }

  @override
  void preloadAssets() async {
    await _ensureScriptLoaded();
  }
}
