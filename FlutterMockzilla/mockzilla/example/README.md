This Flutter app is an example of how to use the `mockzilla` plugin.

## Getting started

In order to download the required dependencies for this app, run the following command in the base 
directory.

```shell
flutter pub get
```

This app uses the [freezed](https://pub.dev/packages/freezed) package for code generation of 
immutable classes that contain utilities such as `==`, `hashCode`, and `copyWith`. It also uses 
[retrofit](https://pub.dev/packages/retrofit) to generate HTTP Dio clients.

In order to execute the code generation, run the following command in the base directory of this 
app.

```shell
dart run build_runner build
```