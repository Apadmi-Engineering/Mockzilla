import 'dart:convert';
import 'dart:js_interop';

import 'package:web/web.dart' as web;

extension type JsResponse._(JSObject _) implements JSObject {
  external JSPromise<JSString> text();
}

@JS()
external JSPromise<JsResponse> fetch(String resource);

class VersionJsonUtils {
  static final _suffixRegExp = RegExp(r'[^/]+\.html.*');

  static Future<Map<String, dynamic>> getVersionJson() async {
    final uri = _getVersionJsonUri();
    final response = await fetch(uri.toString()).toDart;
    final body = await response.text().toDart;
    return json.decode(body.toString());
  }

  static Uri _getVersionJsonUri() {
    final currentUri = Uri.parse(web.window.document.baseURI);
    final String withoutSuffix =
        '${currentUri.origin}${currentUri.path.replaceAll(_suffixRegExp, '')}';

    Uri uri = Uri.parse(withoutSuffix).removeFragment().replace(query: '');

    if (uri.path.length > 1 &&
        !uri.path.endsWith('/') &&
        (uri.isScheme('http') || uri.isScheme('https'))) {
      uri = uri.replace(path: uri.path.substring(0, uri.path.lastIndexOf('/')));
    }

    final List<String> segments = [...uri.pathSegments]
      ..removeWhere((element) => element.isEmpty);

    return uri.replace(pathSegments: [...segments, 'version.json']);
  }
}
