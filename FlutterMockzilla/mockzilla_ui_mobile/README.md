<p align="center">
    <img src="https://raw.githubusercontent.com/Apadmi-Engineering/Mockzilla/develop/icon.svg" height=200>
</p>

*Warning: This plugin is still experimental and may contain bugs! The API is also subject to change*

A Flutter plugin providing inbuilt UI for configuring the [Mockzilla](https://pub.dev/packages/mockzilla) server at runtime.

|             | Android                 | iOS   |
|-------------|-------------------------|-------|
| **Support** | SDK 21+ (Target SDK 36) | 13.0+ |

**Full documentation available [here!](https://mockzilla.apadmi.dev/mobile_ui)**

## Setup Mockzilla

This plugin is an additional tool to be used in conjunction with the core [Mockzilla](https://pub.dev/packages/mockzilla) package which must be
setup first.

## Launch the Ui

Once your server is running launch the UI from your app.

```dart
MockzillaUiMobile.launchManagementUi()
```

This will launch the Ui overlay allowing a user to configure the mocked endpoints.


See full example [here](https://github.com/Apadmi-Engineering/Mockzilla/blob/develop/FlutterMockzilla/mockzilla/example/lib/main.dart#L75).