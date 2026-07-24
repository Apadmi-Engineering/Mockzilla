# SwiftMockzilla

[![License](https://img.shields.io/github/license/Apadmi-Engineering/SwiftMockzilla)](LICENSE)

This repository hosts the Swift Package Manager (SPM) wrapper around Mockzilla's `mockzilla.xcframework` binary, for use in native iOS apps. It's a distribution package only — source, issues, and contributions all live in the [main Mockzilla repository](https://github.com/Apadmi-Engineering/Mockzilla).

Mockzilla is a compile-safe mock HTTP server you run embedded in your app during development/testing, so you can mock API responses without a real backend — see the [main README](https://github.com/Apadmi-Engineering/Mockzilla#readme) for the full pitch.

## Installation

In Xcode: **File > Swift Packages > Add Package Dependency**, then add:

```
https://github.com/Apadmi-Engineering/SwiftMockzilla.git
```

> This package is for native iOS apps. If you're using Kotlin Multiplatform, add the `com.apadmi:mockzilla` Gradle dependency to your shared source set instead.

## Documentation

Full setup and usage docs are at [mockzilla.apadmi.dev](https://mockzilla.apadmi.dev/quick-start/).

For documentation, issues, feedback and everything else, please see the [main repository](https://github.com/Apadmi-Engineering/Mockzilla).
