<p align="center">
    <img src="https://raw.githubusercontent.com/Apadmi-Engineering/Mockzilla/develop/icon.svg" height=200>
</p>

A Flutter plugin for running and configuring a local, mock HTTP server that allows your mobile app to simulate calls to a REST API.

|             | Android                 | iOS   |
|-------------|-------------------------|-------|
| **Support** | SDK 21+ (Target SDK 36) | 13.0+ |

**Full documentation available [here!](https://mockzilla.apadmi.dev/)**

## Why use Mockzilla?

✅ Compile safe mock endpoint definitions.

✅ HTTP client agnostic.

✅ Works completely offline.

✅ Entirely self-contained in your application's codebase.

## To hit the ground running

> ⚠️ **Warning:** Mockzilla is a development tool only. Do not use it in production! Advice on how to do this using different Dart entrypoints can be found [here](https://mockzilla.apadmi.dev/#recommendation).

**(1)** Create your Mockzilla server config and add mocked endpoints.

```dart
final mockzillaConfig = MockzillaConfig().addEndpoint(
    () => EndpointConfig(
        name: "Hello world",
        endpointMatcher: (request) => request.uri.endsWith("/hello-world"),
        defaultHandler: (request) => const MockzillaHttpResponse(
            body: jsonEncode(const HelloWorldResponse())),
        ),
        errorHandler: (request) => const MockzillaHttpResponse(
            statusCode: 418,
        ),
    ),
);
```

**(2)** Start the mock server!

```dart
// Make sure to call this before starting Mockzilla!
WidgetsFlutterBinding.ensureInitialized();

await Mockzilla.startMockzilla(mockzillaConfig);
```

## Mockzilla Desktop

The Mockzilla Desktop application lets you inspect and configure the mocked endpoints at runtime. You can manipulate the responses and adjust simulated latency on a connected device.

<img src="https://raw.githubusercontent.com/Apadmi-Engineering/Mockzilla/refs/heads/develop/docs/docs/desktop/img/desktop_demo.gif">

More information about Mockzilla Desktop can be found [here](https://mockzilla.apadmi.dev/desktop/overview/).

> ℹ️ **Note:** Mockzilla Desktop replaces the web console from version 1.0.0 onwards. The web console is now deprecated and will eventually be retired.