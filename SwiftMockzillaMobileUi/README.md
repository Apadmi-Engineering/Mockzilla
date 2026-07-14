# SwiftMockzillaMobileUi

[![License](https://img.shields.io/github/license/Apadmi-Engineering/SwiftMockzillaMobileUi)](LICENSE)

This repository hosts the Swift Package Manager (SPM) wrapper around Mockzilla's mobile-ui `xcframework` binary — an in-app overlay for controlling a running Mockzilla server (forcing failures, adding latency, applying presets) without a rebuild. It's a distribution package only — source, issues, and contributions all live in the [main Mockzilla repository](https://github.com/Apadmi-Engineering/Mockzilla).

## Installation

In Xcode: **File > Swift Packages > Add Package Dependency**, then add:

```
https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi
```

> This package is for native iOS apps. If you're using Kotlin Multiplatform, add the `com.apadmi:mockzilla-mobile-ui` Gradle dependency to your shared source set instead.

## Documentation

Full setup and usage docs are at [mockzilla.apadmi.dev](https://mockzilla.apadmi.dev/mobile_ui/).

For documentation, issues, feedback and everything else, please see the [main repository](https://github.com/Apadmi-Engineering/Mockzilla).
