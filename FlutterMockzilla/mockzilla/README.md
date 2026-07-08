<p align="center">
    <img src="https://raw.githubusercontent.com/Apadmi-Engineering/Mockzilla/develop/icon.svg" height=200>
</p>

A Flutter plugin for running and configuring a local, mock HTTP server that allows your mobile app to simulate calls to a REST API.

|             | Android                 | iOS   |
|-------------|-------------------------|-------|
| **Support** | SDK 23+ (Target SDK 36) | 13.0+ |


## Quick Start 🚀

Head to the [quick start guide](https://mockzilla.apadmi.dev/quick-start/) to get up and running, or jump straight to a specific topic:

- [Configuring Endpoints](https://mockzilla.apadmi.dev/endpoints/)
- [Mockzilla Desktop](https://mockzilla.apadmi.dev/desktop/overview/)
- [Mockzilla Mobile UI](https://mockzilla.apadmi.dev/mobile_ui/)
- [Presets](https://mockzilla.apadmi.dev/presets/)

### To hit the ground running

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

## Why's it useful? 🙌

Development servers go down, endpoints can be late being delivered or not exist at all! Mockzilla aims to easily provide a way of simulating your server from within your mobile application's codebase.

## Advantages

✅ Compile safe mock endpoint definitions.

✅ HTTP client agnostic.

✅ Works completely offline.

✅ Entirely self-contained in your application's codebase.

✅ Edit responses live from a [desktop app](https://mockzilla.apadmi.dev/desktop/overview/) or an [in-app overlay](https://mockzilla.apadmi.dev/mobile_ui/) — no rebuild required.

✅ [Presets](https://mockzilla.apadmi.dev/presets/) for one-tap switching between success, error, and edge-case responses.

## Control mocks live, while your app runs 🎛️

Beyond defining mocks in code, Mockzilla ships two ways to change what's returned *while your app is running* — force an endpoint to fail, add artificial latency, or apply a preset, all without touching code or rebuilding:

- **[Mockzilla Desktop](https://mockzilla.apadmi.dev/desktop/overview/)**: A companion app that connects to your device over Wifi.
- **[Mockzilla Mobile UI](https://mockzilla.apadmi.dev/mobile_ui/)**: An overlay you embed directly in your app.

![alt text](https://raw.githubusercontent.com/Apadmi-Engineering/Mockzilla/refs/heads/develop/docs/docs/img/controls-ui.png "Desktop app and embedded UI")

## Why not use a hosted solution? ☁️

Hosted mocking solutions can be powerful mocking tools in many cases. They have their downsides:

1. They can go down, Mockzilla works offline!
2. There's no compile-time checking
3. They require active maintenance with no compile-time safety if APIs change.

## Important Note 🛑

Mockzilla is designed as a development and test tool **only**.

Mockzilla should **never be used in production**. Its traffic is unprotected and by nature of running a server on device, it can introduce security issues. Advice on how to do this using different Dart entrypoints can be found [here](https://mockzilla.apadmi.dev/quick-start/#__tabbed_5_2).

**Do not ship it to production**.

## Contributing

Contributions are welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) for how to get set up.


