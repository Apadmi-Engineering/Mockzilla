# Contributing

## Repo Structure

```
|- fastlane.       : Build scripts and configuration
|- samples         : Sample apps demonstrating usage of Mockzilla
|- mockzilla       : The directory forming the core Mockzilla KMM module
|- mockzilla-common: Common utilities used by multiple other modules
|- mockzilla-management: A kotlin wrapper around the http apis defined in `mockzilla`
|- mockzilla-management-ui: Compose multiplatform app used to maniplulate mockzilla at runtime
|- SwiftMockzilla  : The directory forming the SPM package
|- FlutterMockzilla: The directory containing the federated Flutter plugin

```

## Issues

All work is tracked using Github Issues. Before starting please ensure you create an Issue associated with your work if one does not exist.

[https://github.com/Apadmi-Engineering/Mockzilla/issues](https://github.com/Apadmi-Engineering/Mockzilla/issues)

## Write your code!

For guidance on contributing to the Flutter packages, please checkout the [Flutter README](https://github.com/Apadmi-Engineering/Mockzilla/tree/develop/FlutterMockzilla).

1. Checkout the tests. The library is setup with TDD in mind, we recommend writing your tests first!
2. Implement your feature/bugfix!

### Testing through the demo apps.

It's a good idea to sanity check your work by using the library through the demo apps.

Open the root of this repo in Android Studio and run the `samples.demo-kmm.AndroidApp` or `samples.demo-android` targets. The KMM iOS app can also be run through XCode as normal.
Note: Currently there's no way to test the Swift package locally without it first being deployed.

## Internal API (`@InternalMockzillaApi`)

Some types must be `public` because they are shared across library modules (e.g. DTOs used by both `mockzilla` and `mockzilla-management`), but they are **not intended for use by external consumers**.

These are annotated with `@InternalMockzillaApi` (defined in `mockzilla-common`). The annotation is a `@RequiresOptIn` at the `ERROR` level, so consumers who accidentally reference an internal type will get a compile-time error.

**When to apply it:** Any public declaration (class, interface, function, property) that lives in a `*.internal.*` package.

**When NOT to apply it:**
- Swift/Objective-C interop entry points (e.g. `AsyncUtils.kt`, `NestedClassBridgeGeneration.kt` on iOS) — Swift has no way to satisfy `@OptIn` requirements, so annotating these would break the Swift bridge.
- `@JsExport` declarations in `jsinterface/JsInterface.kt` — these are intentionally public JS API even though they happen to live in an `internal` package.
- Declarations that already have the Kotlin `internal` visibility modifier — the compiler already prevents access, so no annotation is needed (`private` and `internal` Kotlin modifiers should be preferred if they're possible).

**How library modules opt in:** All modules in this repo are inside the internal-API boundary. Rather than adding `@file:OptIn` to every file, each module's `build.gradle.kts` opts in at the module level:

```kotlin
compilerOptions {
    freeCompilerArgs.add("-opt-in=com.apadmi.mockzilla.lib.InternalMockzillaApi")
}
```

**For `expect`/`actual` declarations:** The annotation must be present on both the `expect` declaration and **every** `actual` declaration across all platforms. Missing one platform will cause a compile error.

## Spotless

We use Spotless to reformat and organise all of our library code. It runs automatically on compilation so please ensure you've compiled your code before submitting a pull request.

## Create a pull request

Creating a pull request will check everything compiles and runs all your tests. Once all the checks pass, we'll review your code and hopefully get it merged!

## Releases

This section is specifically for Apadmi maintainers creating releases.

1. Releases are all managed automatically. To release a new version, merge the open release pull request as described here: [https://github.com/google-github-actions/release-please-action](https://github.com/google-github-actions/release-please-action).
2. Deploy the release to maven central from [https://s01.oss.sonatype.org/#stagingRepositories](https://s01.oss.sonatype.org/#stagingRepositories).


