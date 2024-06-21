<center>
    <img src="../../icon.svg" height=200>
</center>

A Flutter plugin for running and configuring a local, mock HTTP server that allows your mobile app to simulate calls to a REST API.

**Full documentation available at [here!](https://apadmi-engineering.github.io/Mockzilla/)**

## Why use Mockzilla?

✅ Compile safe mock endpoint definitions.

✅ HTTP client agnostic.

✅ Works completely offline.

✅ Entirely self-contained in your application's codebase.

## To hit the ground running

 >**Before we begin:** Mockzilla is a development tool only. Do not use it in production! Advice on how to do this using different Dart entrypoints can be found [here](https://apadmi-engineering.github.io/Mockzilla/#recommendation).

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