# Changelog

## [2.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.1.1...flutter_mockzilla_platform_interface-v2.0.0) (2026-01-26)


### ⚠ BREAKING CHANGES

* Default/Error handles are now async functions = ([#518](https://github.com/Apadmi-Engineering/Mockzilla/issues/518))

### Features

* Feature/flutter support new mobile UI ([#532](https://github.com/Apadmi-Engineering/Mockzilla/issues/532)) ([70e0a52](https://github.com/Apadmi-Engineering/Mockzilla/commit/70e0a52276220b97db2d427ec93bc85994c50280))
* Feature/flutter web ([#518](https://github.com/Apadmi-Engineering/Mockzilla/issues/518)) ([4183f06](https://github.com/Apadmi-Engineering/Mockzilla/commit/4183f0626c5936e0ef213a006a46019e9d08d213))

## [2.0.0-dev.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.1.1...flutter_mockzilla_platform_interface-v2.0.0-dev.1) (2025-11-18)

### Features

* Make default handlers, error handlers and pattern matchers asynchronous

## [1.1.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.1.0...flutter_mockzilla_platform_interface-v1.1.1) (2025-09-24)

### Bug Fixes

* Migrate off compatibility release for kotlinx datetime and use stdlib kotlin.time.Instant instead ([e021d8f](https://github.com/Apadmi-Engineering/Mockzilla/commit/e021d8f42d88d27101cf445306468beb21f8512e))

## [1.1.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.0.2...flutter_mockzilla_platform_interface-v1.1.0) (2025-06-05)

### Features

* Implement support for custom Flutter loggers ([#370](https://github.com/Apadmi-Engineering/Mockzilla/issues/370)) ([78e9112](https://github.com/Apadmi-Engineering/Mockzilla/commit/78e9112cc447188eb58750986324b3bec62c4b12))

## [1.0.2](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.0.1...flutter_mockzilla_platform_interface-v1.0.2) (2025-04-09)


### Bug Fixes

* Bump `freezed` dependency from 2.X -&gt; 3.x ([b8df550](https://github.com/Apadmi-Engineering/Mockzilla/commit/b8df5501b415206537278fd7d93fd5a6aa8ecb8e))

## [1.0.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_platform_interface-v1.0.0...flutter_mockzilla_platform_interface-v1.0.1) (2025-01-16)


### Bug Fixes

* Hides `FlutterCallbackProvider` from exported symbols ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))
* Loosens dependency constraints ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))

## [1.0.0](https://github.com/Apadmi-Engineering/Mockzilla/releases/tag/flutter_mockzilla_platform_interface-v1.0.0) (2025-01-09)


### ⚠️ BREAKING CHANGES

* Removes `delayVariance`, and `failureProbability` fields from endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Replaces `delayMean` with `mean` field for endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Removes Web Api fields (these are replaced with dashboard overrides) ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Updates `startMockzilla` to return runtime information ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

### Features

* Adds support for Mockzilla Desktop ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Improves handling of port conflicts ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

### Bug Fixes

* Fixes an issue in the marshalling of request headers for bridge models to Dart ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

## 1.0.0-dev.0

* Removes `delayVariance`, and `failureProbability` fields from endpoint configurations.
* Replaces `delayMean` with `mean` field for endpoint configurations.
* Removes Web Api fields (these are replaced with dashboard overrides).
* Adds `isNetworkDiscoveryEnabled` field to top level config.

## 0.2.0

* Adds default value of `{"Content-Type": "application/json"}` for parameter `headers` in 
`MockzillaHttpResponse`.
* Removes generated `MockzillaConfig.releaseModeConfig` from platform interface.
* Removes generated `MockzillaConfig.additionalLogWriters` from platform interface.

## 0.1.0

* Initial open-source release.
