# Flutter Mockzilla

The Mockzilla Flutter plugin package is implemented as a [federated plugin package](https://docs.flutter.dev/packages-and-plugins/developing-packages#federated-plugins). It calls through to the KMM library utilising [Pigeon](https://pub.dev/packages/pigeon) platform channels; this decision was made to ensure that the Flutter package remains aligned with the underlying KMM package but also removes the complexity developers face with method channels.

## Handling package inter-dependencies

To handle the package inter-dependencies and minimise boilerplate in continuous integration scripts, the repository utilises [melos](https://melos.invertase.dev/). Once you have checked out the repository, install Melos with the following command.

```shell
dart pub global activate melos
```

Once installed, `cd` into the `FlutterMockzilla/` directory and run the following command, which will override package inter-dependencies with the versions on your local machine.

```shell
melos bootstrap
```

## Model generation

Immutable model classes are generated using the freezed package, included in the generated classes are utility methods such as `copyWith()` and `==`. When editing model class definitions, make sure to run the following command to update the generated code.

```shell
dart run build_runner build
```

Alternatively, start a background process to watch the project files and automatically update the generated code in response to changes.

```shell
dart run build_runner watch
```