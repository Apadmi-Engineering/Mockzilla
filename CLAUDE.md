# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Is

Mockzilla is a compile-safe mock HTTP server for mobile apps. It runs an embedded Ktor HTTP server inside the app during development/testing, allowing teams to mock API responses without a real backend. It targets Android, iOS (via Kotlin Native), Flutter (federated plugin), Kotlin Multiplatform, JavaScript (via MSW), and desktop (Compose).

## Commands

### Kotlin/KMM

```bash
# Run all tests for core library
./gradlew :mockzilla:testAndroidHostTest :mockzilla-common:testAndroidHostTest

# iOS tests
./gradlew :mockzilla:iosX64Test :mockzilla-common:iosX64Test

# JavaScript tests
./gradlew :mockzilla:jsBrowserTest :mockzilla-common:jsBrowserTest

# Management library (JVM)
./gradlew :mockzilla-management:jvmTest

# Codegen (JVM)
./gradlew :mockzilla-codegen:jvmTest

# Management UI tests
./gradlew :mockzilla-management-ui:mockzilla-desktop:desktopTest :mockzilla-management-ui:mockzilla-management-ui-common:desktopTest

# Apply code formatting (Spotless + Diktat)
./gradlew spotlessApply

# Publish to local Maven repo
./gradlew publishToMavenLocal
```

### Flutter (Melos monorepo under `FlutterMockzilla/`)

```bash
cd FlutterMockzilla
melos bootstrap          # Install all package dependencies
melos run test:dart      # Run all Dart tests
melos run buildExample   # Build example apps

# Regenerate Pigeon platform channel code (after modifying pigeons/*.dart)
dart run pigeon --input pigeons/mockzilla.dart

# Regenerate Freezed models (after modifying model classes)
dart run build_runner build
```

### Documentation

```bash
pip install -r docs/requirements.txt
bundle exec fastlane generate_docs
```

## Architecture

The project is a monorepo with several independently published packages:

### Core Kotlin Modules

- **`:mockzilla-common`** — Shared data models (`MockzillaConfig`, `EndpointConfiguration`, DTOs, exceptions). Published to Maven Central. Everything else depends on this.
- **`:mockzilla`** — The embedded Ktor HTTP server. Handles request routing, rate limiting, auth plugin, and ZeroConf discovery. Targets Android, iOS (x64/arm64/simulatorArm64), JVM, and JS/Browser.
- **`:mockzilla-management`** — Ktor HTTP client that wraps the management REST API exposed by a running mockzilla server. Used by dashboards to control the mock server at runtime. Targets JVM, iOS, JS/Browser.
- **`:mockzilla-management-ui`** — Compose Multiplatform UI for the management dashboard. Split into:
  - `:mockzilla-desktop` — standalone desktop app
  - `:mockzilla-management-ui-common` — shared UI logic
  - `:mockzilla-mobile-ui` — embeddable in-app overlay (published to Maven Central + CocoaPods)
- **`build-logic/`** — Gradle convention plugins (`AndroidConfig`, `JavaConfig`, `CompilerConfig`) that standardise build settings across modules.

### Dependency Flow

```
mockzilla-common
    ↑
mockzilla (server)
    ↑
mockzilla-management (client)
    ↑
mockzilla-management-ui (dashboard)
```

### Flutter Plugin (`FlutterMockzilla/`)

Federated plugin with four packages:
- `mockzilla` — public Dart API
- `mockzilla_platform_interface` — abstract platform contract (Pigeon-generated)
- `mockzilla_android` — Android implementation (Kotlin, delegates to `:mockzilla`)
- `mockzilla_ios` — iOS implementation (Swift/Kotlin Native)
- `mockzilla_web` — Web implementation (Dart JS interop with MSW)

### iOS Swift Packages (separate Git repos, submodule-style)

- **`SwiftMockzilla/`** — SPM wrapper around the prebuilt `mockzilla.xcframework` binary
- **`SwiftMockzillaMobileUi/`** — SPM wrapper around the prebuilt mobile-ui XCFramework

These are separate repos included as directories here; changes to KMM code require rebuilding the XCFramework and updating the binary in these repos.

### Samples

- `samples/demo-android` — native Android
- `samples/demo-kmm/` — KMM shared module + Android/iOS apps
- `samples/demo-ios/` — native iOS (uses SwiftMockzilla SPM)

## Code Style

Kotlin formatting is enforced by **Spotless + Diktat**. Run `./gradlew spotlessApply` before committing. Configuration lives in `diktat-analysis.yml`.

## Release Process

Releases are managed by **Release-Please** (`.release-please-manifest.json`). Version bumps happen via PR labels/commit messages; don't manually edit version numbers in `pubspec.yaml` or `build.gradle.kts` files that contain `x-release-please-version` markers.

Publishing targets:
- Kotlin modules → **Maven Central** (via `mavenPublishing` Gradle plugin)
- iOS UI → **CocoaPods** (`pod trunk push`)
- Flutter packages → **pub.dev** (`flutter pub publish`)

Fastlane lanes in `fastlane/` orchestrate all publishing steps; see `fastlane/fastfiles/` for per-platform lanes.

## CI

GitHub Actions (`.github/workflows/`) uses **path filtering** — only affected modules run on a given PR. Jobs run on macOS 14 + Xcode 15.4. Java 17, Flutter 3.35.0, and Ruby (Bundler) are required tools.
