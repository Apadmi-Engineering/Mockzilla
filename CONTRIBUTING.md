# Contributing

Want to help? Great! 🙌 Work is driven by GitHub Issues, so that's the best place to start.

## Tree Hygiene

This project follows a **Gitflow** feature branching model. Please also use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) so others can scan the tree and understand changes at a glance. Pull requests for feature branches target the `develop` branch and once reviewed by a maintainer, will have their commits squashed for tidiness.

## Repo Structure

```
|- fastlane                       : Build scripts and configuration
|- samples                        : Sample apps demonstrating usage of Mockzilla
|- mockzilla                      : The core Mockzilla KMP module
|- mockzilla-common               : Common utilities shared across modules
|- mockzilla-management           : Kotlin wrapper around the HTTP APIs defined in `mockzilla`
|- mockzilla-management-ui        : Compose Multiplatform management UI
|   |- mockzilla-desktop          : Desktop app
|   |- mockzilla-mobile-ui        : Mobile management UI (Android/iOS)
|   |- mockzilla-management-ui-common : Shared UI code
|- SwiftMockzilla                 : The SPM package
|- SwiftMockzillaMobileUi         : Swift management UI for mobile
|- FlutterMockzilla               : The federated Flutter plugin
|- js-scripts                     : Web/JS support scripts
```

## Issues

All work is tracked using GitHub Issues. Before diving in, make sure there's an issue linked to your work — create one if it doesn't exist yet.

[https://github.com/Apadmi-Engineering/Mockzilla/issues](https://github.com/Apadmi-Engineering/Mockzilla/issues)

## Write your code!

For guidance on contributing to the Flutter packages, please check out the [Flutter README](https://github.com/Apadmi-Engineering/Mockzilla/tree/develop/FlutterMockzilla).

1. Check out the tests. The library is set up with TDD in mind — we recommend writing your tests first!
2. Implement your feature/bugfix!

### Testing through the demo apps

It's a good idea to sanity check your work by using the library through the demo apps.

Open the root of this repo in Android Studio and run the `samples.demo-kmm.AndroidApp` or `samples.demo-android` targets. The KMM iOS app can also be run through Xcode as normal.

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

**Exception — `mockzilla-management-ui-common`:** This module is shared UI logic consumed only by `mockzilla-desktop` and `mockzilla-mobile-ui` in this repo; it has no intended external API surface at all, so virtually every `public` declaration is annotated with `@InternalMockzillaApi` regardless of package. Declarations here are **not** required to live in a `*.internal.*` package to receive the annotation — the `*.internal.*` rule above is the general convention for modules that do have a real public surface, not a hard requirement everywhere.

## Spotless

We use Spotless to reformat and organise all of our library code. It runs automatically on every commit so ensure you commit it's changes before creating your pull request.

## Create a pull request

Creating a pull request will check everything compiles and runs all your tests. Once all the checks pass, we'll review your code and hopefully get it merged!

## Releases

This section is specifically for Apadmi maintainers creating releases.

1. Releases are all managed automatically. To release a new version, merge the open release pull request as described here: [https://github.com/google-github-actions/release-please-action](https://github.com/google-github-actions/release-please-action).
2. Deploy the release to Maven Central from [https://s01.oss.sonatype.org/#stagingRepositories](https://s01.oss.sonatype.org/#stagingRepositories).
