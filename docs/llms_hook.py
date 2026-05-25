import re
import glob
from pathlib import Path

# ---------------------------------------------------------------------------
# llms.txt generation
#
# Produces per-platform LLM reference files in the site output directory:
#   llms.txt            — index
#   llms-android.txt
#   llms-ios.txt
#   llms-flutter.txt
#   llms-kmp.txt
#
# Content is assembled from the live doc source pages so it stays in sync
# automatically. Tab filtering ensures each file contains only its
# platform's code snippets.
# ---------------------------------------------------------------------------

_LLMS_PAGES = [
    "quick-start.md",
    "endpoints.md",
    "additional_config.md",
    "mobile_ui.md",
    "presets.md",
]

# Tab labels used in the MkDocs source, mapped to each platform.
# KMP shows all tabs because shared Kotlin code calls into both Android and iOS targets.
_PLATFORMS = {
    "android": {
        "label":    "Android (Native Kotlin)",
        "filename": "llms-android.txt",
        "tabs":     {"KMP & Android", "Android Application", "Kotlin"},
    },
    "ios": {
        "label":    "iOS (Native Swift)",
        "filename": "llms-ios.txt",
        "tabs":     {"iOS", "iOS App Delegate", "Swift"},
    },
    "flutter": {
        "label":    "Flutter",
        "filename": "llms-flutter.txt",
        "tabs":     {"Flutter"},
    },
    "kmp": {
        "label":    "Kotlin Multiplatform (KMP)",
        "filename": "llms-kmp.txt",
        # KMP spans both Android and iOS so all tabs are relevant
        "tabs":     {"KMP & Android", "iOS", "Android Application", "iOS App Delegate", "Kotlin", "Swift"},
    },
}

# Preambles are written per-platform so an LLM using the file cannot accidentally
# mix up class names, method signatures, or startup sequences across platforms.
_PREAMBLES = {
    "android": """\
# Mockzilla — Android (Native Kotlin) Reference

> This file contains only the Android native Kotlin API.
> Do NOT use Swift, Dart, or Flutter APIs from other llms-*.txt files.

**Mock server base URL:** `http://localhost:8080/local-mock`
**Management API base URL:** `http://localhost:8080/api`

## Quick API reference

### Dependency
```kotlin
implementation("com.apadmi:mockzilla:{version}")
// Optional embedded UI:
implementation("com.apadmi:mockzilla-mobile-ui:{mobile_ui_version}")
```

### Start / stop
```kotlin
// Application.onCreate
val params = startMockzilla(config, this /* Application context */)
// params.mockBaseUrl  → "http://localhost:8080/local-mock"
```

### Endpoint builder (always EndpointConfiguration.Builder, not EndpointConfigurationBuilder)
```kotlin
EndpointConfiguration.Builder("key")          // key is a String
    .setPatternMatcher {{ uri.endsWith("/path") && method == HttpMethod.Get }}
    .setDefaultHandler {{ MockzillaHttpResponse(body = "...") }}
    .setErrorHandler   {{ MockzillaHttpResponse(statusCode = HttpStatusCode.InternalServerError) }}
    .build()
```

### Response type
```kotlin
MockzillaHttpResponse(
    statusCode = HttpStatusCode.OK,     // io.ktor.http.HttpStatusCode
    headers    = mapOf("Content-Type" to "application/json"),
    body       = "..."
)
```

### Request body (inside a handler, `this` is MockzillaHttpRequest)
```kotlin
val body = bodyAsString()
val dto  = Json.decodeFromString<MyDto>(bodyAsString())
```

### Release mode auth header (add to every outgoing request)
```kotlin
val (headerName, headerValue) = params.authHeaderProvider.generateHeader()
```
""",

    "ios": """\
# Mockzilla — iOS (Native Swift) Reference

> This file contains only the iOS native Swift API (via the SwiftMockzilla SPM package).
> Do NOT use Kotlin, Dart, or Flutter APIs from other llms-*.txt files.

**Mock server base URL:** `http://localhost:8080/local-mock`
**Management API base URL:** `http://localhost:8080/api`

## Quick API reference

### Dependency
Add SPM package in Xcode → File › Swift Packages › Add Package Dependency:
- Core: `https://github.com/Apadmi-Engineering/SwiftMockzilla.git`
- Optional embedded UI: `https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi`

### Start / stop
```swift
import SwiftMockzilla
import mockzilla

// AppDelegate.didFinishLaunchingWithOptions
let params = startMockzilla(config: config)
// params.mockBaseUrl → use as HTTP client base URL

// AppDelegate.applicationWillTerminate
stopMockzilla()
```

### Endpoint builder (always EndpointConfigurationBuilder, not EndpointConfiguration.Builder)
```swift
EndpointConfigurationBuilder(id: "key")    // id is a String
    .setSwiftPatternMatcher {{ $0.uri.hasSuffix("/path") }}
    .setSwiftDefaultHandler {{ _ in MockzillaHttpResponse(status: HttpStatusCode.OK, headers: [:], body: "...") }}
    .setSwiftErrorHandler   {{ _ in MockzillaHttpResponse(status: HttpStatusCode.InternalServerError) }}
    .build()
```

Note: use `.setSwiftPatternMatcher`, `.setSwiftDefaultHandler`, `.setSwiftErrorHandler` — NOT the
bare Kotlin versions.

### Response type
```swift
// SwiftMockzilla adds this convenience init:
MockzillaHttpResponse(
    status:  HttpStatusCode.OK,   // Ktor_httpHttpStatusCode via typealias
    headers: ["Content-Type": "application/json"],
    body:    "..."
)
```

### Request body (inside a Swift handler closure)
```swift
let body = request.bodyAsString()
let dto  = try! JSONDecoder().decode(MyDto.self, from: body.data(using: .utf8)!)
```

### Release mode auth header (add to every outgoing request)
```swift
let pair = params.authHeaderProvider.generateHeader() // KotlinPair<String, String>
```
""",

    "flutter": """\
# Mockzilla — Flutter (Dart) Reference

> This file contains only the Flutter Dart API.
> Do NOT use Kotlin or Swift APIs from other llms-*.txt files.

**Mock server base URL:** `http://localhost:8080/local-mock`
**Management API base URL:** `http://localhost:8080/api`

## Quick API reference

### Dependency
```yaml
# pubspec.yaml
dependencies:
  mockzilla: {version}
  mockzilla_ui_mobile: {mobile_ui_version}  # optional embedded UI
```

### Start / stop
```dart
// main_mock.dart — keep separate from production main.dart
import 'package:mockzilla/mockzilla.dart';

Future<void> main() async {{
    WidgetsFlutterBinding.ensureInitialized(); // MUST come before startMockzilla
    final params = await Mockzilla.startMockzilla(config);
    // params.mockBaseUrl → use as HTTP client base URL
    runApp(const MyApp());
}}
// Run with: flutter run -t lib/main_mock.dart

await Mockzilla.stopMockzilla();
```

### Endpoint (EndpointConfig constructor — no builder pattern)
```dart
EndpointConfig(
    name:            "key",
    endpointMatcher: (request) => request.uri.endsWith("/path") && request.method == HttpMethod.get,
    defaultHandler:  (request) => const MockzillaHttpResponse(statusCode: 200, body: "..."),
    errorHandler:    (request) => const MockzillaHttpResponse(statusCode: 500),
)
```

### Response type (statusCode is an int, not an enum)
```dart
MockzillaHttpResponse(
    statusCode: 200,
    headers:    const {{"Content-Type": "application/json"}},
    body:       "...",
)
```

Default headers are `{{"Content-Type": "application/json"}}` — unlike Kotlin/Swift which default to empty.

### Request body (body is a plain String field, already read)
```dart
final dto = MyDto.fromJson(jsonDecode(request.body));
```

### Release mode
Not supported on Flutter.
""",

    "kmp": """\
# Mockzilla — Kotlin Multiplatform (KMP) Reference

> This file covers KMP: shared Kotlin config in commonMain, started from both Android and iOS
> native targets. The Swift snippets show how iOS calls into the Kotlin/Native framework —
> do NOT confuse them with the pure-native iOS API in llms-ios.txt.

**Mock server base URL:** `http://localhost:8080/local-mock`
**Management API base URL:** `http://localhost:8080/api`

## Quick API reference

### Dependency
```kotlin
// shared/build.gradle.kts (commonMain)
implementation("com.apadmi:mockzilla:{version}")
implementation("com.apadmi:mockzilla-mobile-ui:{mobile_ui_version}") // optional
```

### Pattern: define config in commonMain, start from each native target

```kotlin
// shared/src/commonMain — MockzillaConfig.Builder and EndpointConfiguration.Builder only
val mockzillaConfig = MockzillaConfig.Builder()
    .addEndpoint(myEndpoint)
    .build()
```

```kotlin
// shared/src/androidMain or androidApp — Application.onCreate
val params = startMockzilla(mockzillaConfig, this)
```

```kotlin
// shared/src/iosMain — expose thin wrappers for Swift to call
fun startMockServer() = startMockzilla(mockzillaConfig)
fun stopMockServer()  = stopMockzilla()
```

```swift
// iosApp AppDelegate — calls Kotlin via generated framework
import shared
let params = MockServerKt.startMockServer()
// applicationWillTerminate: MockServerKt.stopMockServer()
```

### Endpoint builder (EndpointConfiguration.Builder in commonMain)
```kotlin
EndpointConfiguration.Builder("key")
    .setPatternMatcher {{ uri.endsWith("/path") && method == HttpMethod.Get }}
    .setDefaultHandler {{ MockzillaHttpResponse(body = "...") }}
    .setErrorHandler   {{ MockzillaHttpResponse(statusCode = HttpStatusCode.InternalServerError) }}
    .build()
```

### Response type
```kotlin
MockzillaHttpResponse(
    statusCode = HttpStatusCode.OK,     // io.ktor.http.HttpStatusCode
    headers    = mapOf("Content-Type" to "application/json"),
    body       = "..."
)
```

### Release mode auth header
```kotlin
// Android — in your HTTP interceptor
val (headerName, headerValue) = params.authHeaderProvider.generateHeader()
```
""",
}


def _read_gradle_version(gradle_relative_path):
    """Read the version string from a build.gradle.kts file relative to the repo root."""
    matches = glob.glob(f"../**/{gradle_relative_path}", recursive=True)
    if not matches:
        return "latest"
    text = Path(matches[0]).read_text()
    m = re.search(r'version\s*=.*"(.*\..*\..*)"', text)
    return m.group(1) if m else "latest"


def _transform_tabs(lines, allowed_tabs=None):
    """
    Flatten pymdownx.tabbed blocks. If allowed_tabs is given, only emit tabs whose
    label is in that set; skip others silently so no wrong-platform code leaks in.
    """
    result = []
    i = 0
    while i < len(lines):
        tab_m = re.match(r'^=== "(.+)"$', lines[i])
        if tab_m:
            label = tab_m.group(1)
            i += 1
            content_lines = []
            while i < len(lines):
                l = lines[i]
                if l.startswith("    "):
                    content_lines.append(l[4:])
                    i += 1
                elif l == "" and i + 1 < len(lines) and lines[i + 1].startswith("    "):
                    content_lines.append("")
                    i += 1
                else:
                    break
            if allowed_tabs is None or label in allowed_tabs:
                result.append(f"\n**{label}:**")
                result.extend(content_lines)
            # else: silently drop this tab's content
        else:
            result.append(lines[i])
            i += 1
    return result


def _transform_admonitions(lines):
    """Convert MkDocs admonitions (!!! warning ...) to plain blockquotes."""
    result = []
    i = 0
    while i < len(lines):
        admon_m = re.match(r'^!!! (\w+)(?:\s+"[^"]*")?\s*$', lines[i])
        if admon_m:
            kind = admon_m.group(1).upper()
            i += 1
            body = []
            while i < len(lines) and (lines[i].startswith("    ") or lines[i] == ""):
                body.append(lines[i][4:] if lines[i].startswith("    ") else "")
                i += 1
            if body:
                result.append(f"> **{kind}:** {body[0]}")
                for bl in body[1:]:
                    result.append(f"> {bl}" if bl else ">")
            else:
                result.append(f"> **{kind}**")
        else:
            result.append(lines[i])
            i += 1
    return result


def _strip_relative_links(text):
    """Replace relative MkDocs links with just their label text."""
    return re.sub(r'\[([^\]]+)\]\([^)]*\.\.[^)]*\)', r'\1', text)


def _transform_page(text, version, mobile_ui_version, allowed_tabs=None):
    text = text.replace("{{ get_version() }}", version)
    text = text.replace("{{ get_mobile_ui_version() }}", mobile_ui_version)
    text = _strip_relative_links(text)
    lines = text.split("\n")
    lines = _transform_tabs(lines, allowed_tabs)
    lines = _transform_admonitions(lines)
    return "\n".join(lines)


def _build_llms_txt(docs_dir, site_dir):
    version           = _read_gradle_version("mockzilla/build.gradle.kts")
    mobile_ui_version = _read_gradle_version(
        "mockzilla-management-ui/mockzilla-mobile-ui/build.gradle.kts"
    )

    site_path = Path(site_dir)

    for key, platform in _PLATFORMS.items():
        preamble = _PREAMBLES[key].format(version=version, mobile_ui_version=mobile_ui_version)
        sections = [preamble]

        for filename in _LLMS_PAGES:
            page_path = Path(docs_dir) / filename
            if not page_path.exists():
                continue
            raw = page_path.read_text(encoding="utf-8")
            transformed = _transform_page(raw, version, mobile_ui_version, platform["tabs"])
            sections.append(f"\n---\n\n{transformed.strip()}\n")

        (site_path / platform["filename"]).write_text(
            "\n".join(sections), encoding="utf-8"
        )

    index_lines = [
        "# Mockzilla LLM Reference Index\n",
        "Use the platform-specific file that matches your target to avoid mixing APIs.\n",
    ]
    for key, platform in _PLATFORMS.items():
        index_lines.append(f"- [{platform['label']}]({platform['filename']})")

    (site_path / "llms.txt").write_text("\n".join(index_lines), encoding="utf-8")


def on_post_build(config):
    _build_llms_txt(config["docs_dir"], config["site_dir"])


if __name__ == "__main__":
    # Run standalone to generate files into docs/ for VCS commit.
    # Usage (from repo root): python docs/llms_hook.py
    _build_llms_txt("docs/docs", "docs")
