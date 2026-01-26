# Changelog

## [2.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.1.1...flutter_mockzilla-v2.0.0) (2025-11-18)

### Features

* Handle default handlers, error handlers and pattern matchers being asynchronous

## [1.3.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.2.0...flutter_mockzilla-v1.3.0) (2025-10-01)


### Features

* setup flutter wrapper and deployments ([86fa014](https://github.com/Apadmi-Engineering/Mockzilla/commit/86fa0140f5b3de8c716f5c1cdd3e088b992a78ba))


### Bug Fixes

* fix flutter example ([#463](https://github.com/Apadmi-Engineering/Mockzilla/issues/463)) ([2c4fd3f](https://github.com/Apadmi-Engineering/Mockzilla/commit/2c4fd3fb4e2a450dccec1ad09464fdfea4364b97))
* remove unneeded server dependency from mobile ui ([a5e6c38](https://github.com/Apadmi-Engineering/Mockzilla/commit/a5e6c38ae3fc30df846c2963b053b272e242149f))
* various build issues ([b5d43b2](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5d43b278030904d1da12704a405acb974a70e61))

## [1.2.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.1.0...flutter_mockzilla-v1.2.0) (2025-09-25)


### Features

* bump underlying kotlin library version to bump android target ([3a6fc2f](https://github.com/Apadmi-Engineering/Mockzilla/commit/3a6fc2fb682061159f87b652ad2dab759dc6fb41))


### Bug Fixes

* Migrate off compatibility release for kotlinx datetime and use stdlib kotlin.time.Instant instead ([e021d8f](https://github.com/Apadmi-Engineering/Mockzilla/commit/e021d8f42d88d27101cf445306468beb21f8512e))

## [1.1.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.0.2...flutter_mockzilla-v1.1.0) (2025-06-06)


### Features

* Implement support for custom Flutter loggers ([#370](https://github.com/Apadmi-Engineering/Mockzilla/issues/370)) ([78e9112](https://github.com/Apadmi-Engineering/Mockzilla/commit/78e9112cc447188eb58750986324b3bec62c4b12))


### Bug Fixes

* Update Flutter package inter-dependencies ([afe63a4](https://github.com/Apadmi-Engineering/Mockzilla/commit/afe63a49275f57e17df814addd0f922bb5aca22d))

## [1.0.2](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.0.1...flutter_mockzilla-v1.0.2) (2025-04-10)


### Bug Fixes

* Bump `freezed` dependency from 2.X -&gt; 3.x ([b8df550](https://github.com/Apadmi-Engineering/Mockzilla/commit/b8df5501b415206537278fd7d93fd5a6aa8ecb8e))

## [1.0.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla-v1.0.0...flutter_mockzilla-v1.0.1) (2025-01-17)


### Bug Fixes

* Hides `FlutterCallbackProvider` from exported symbols ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))
* Loosens dependency constraints ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))

## [1.0.0](https://github.com/Apadmi-Engineering/Mockzilla/releases/tag/flutter_mockzilla-v1.0.0) (2025-01-09)


### ⚠️ BREAKING CHANGES

* Removes `delayVariance`, and `failureProbability` fields from endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Replaces `delayMean` with `mean` field for endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Removes Web Api fields (these are replaced with dashboard overrides) ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Updates `startMockzilla` to return runtime information ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

### Features

* Adds support for Mockzilla Desktop ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Improves handling of port conflicts ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Adds support for SPM ([bbcd7ba](https://github.com/Apadmi-Engineering/Mockzilla/compare/5363e6d11372011bfec9dae3841910f907cbd681...bbcd7baaeb6556255714de7f90bac80fb6b65595))

### Bug Fixes

* Fixes an issue in the marshalling of request headers for bridge models to Dart ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

## 1.0.0-dev.1

* Fixes an issue in the marshalling of request headers from bridge models to Dart.
* Upgrades dependencies of federated plugins
  * `mockzilla_android` from `1.0.0-dev.0` -> `1.0.0-dev.1`
  * `mockzilla_ios` from `1.0.0-dev.0` -> `1.0.0-dev.1`

## 1.0.0-dev.0

* Updates underlying KMP library to 2.0.1.
* Supports Mockzilla Desktop Application.
* Bumps dependencies of federated plugins
  * `mockzilla_platform_interface` from `^0.2.0` -> `^1.0.0-dev.0`
  * `mockzilla_android` from `^0.1.1` -> `^1.0.0-dev.0`
  * `mockzilla_ios` from `^0.1.1` -> `^1.0.0-dev.0`

## 0.1.2

* Updates example app to include request body in fetch packages request.

## 0.1.1

* Adds default value of `{"Content-Type": "application/json"}` for parameter `headers` in
  `MockzillaHttpResponse`.
* Bumps dependencies of federated plugins
  * `mockzilla_platform_interface` from `^0.1.0` -> `^0.2.0`
  * `mockzilla_android` from `^0.1.0` -> `^0.1.1`
  * `mockzilla_ios` from `^0.1.0` -> `^0.1.1`

## 0.1.0

* Initial open-source release.
