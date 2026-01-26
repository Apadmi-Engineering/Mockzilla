# Changelog

## [2.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.1.1...flutter_mockzilla_ios-v2.0.0) (2025-11-18)

### Features

* Handle default handlers, error handlers and pattern matchers being asynchronous

## [1.2.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.2.0...flutter_mockzilla_ios-v1.2.1) (2025-09-30)


### Bug Fixes

* Fix various build issues to support mobile UI ([b5d43b2](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5d43b278030904d1da12704a405acb974a70e61))

## [1.2.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.1.2...flutter_mockzilla_ios-v1.2.0) (2025-09-25)


### Features

* bump underlying kotlin library version to bump android target ([3a6fc2f](https://github.com/Apadmi-Engineering/Mockzilla/commit/3a6fc2fb682061159f87b652ad2dab759dc6fb41))
* bump underlying kotlin library version to bump android target ([1d254d1](https://github.com/Apadmi-Engineering/Mockzilla/commit/1d254d12373196c7efed2046ce8a58112f0ef1ed))


### Bug Fixes

* Migrate off compatibility release for kotlinx datetime and use stdlib kotlin.time.Instant instead ([e021d8f](https://github.com/Apadmi-Engineering/Mockzilla/commit/e021d8f42d88d27101cf445306468beb21f8512e))

## [1.1.2](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.1.1...flutter_mockzilla_ios-v1.1.2) (2025-06-27)


### Bug Fixes

* **flutter:** Swap usage of `asyncAndWait` -&gt; `async` in iOS proxy logger ([a9cdf90](https://github.com/Apadmi-Engineering/Mockzilla/commit/a9cdf902dac5e0cac2c1506bfb0d36cffc978ced))

## [1.1.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.1.0...flutter_mockzilla_ios-v1.1.1) (2025-06-06)


### Bug Fixes

* Fix `mockzilla_ios` release by removing reference to symlink ([#380](https://github.com/Apadmi-Engineering/Mockzilla/issues/380)) ([852ce69](https://github.com/Apadmi-Engineering/Mockzilla/commit/852ce6993efe921d99c423282ee53f4c3e2ef3be))

## [1.1.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.0.3...flutter_mockzilla_ios-v1.1.0) (2025-06-05)


### Features

* Implement support for custom Flutter loggers ([#370](https://github.com/Apadmi-Engineering/Mockzilla/issues/370)) ([78e9112](https://github.com/Apadmi-Engineering/Mockzilla/commit/78e9112cc447188eb58750986324b3bec62c4b12))


### Bug Fixes

* Bump Flutter version used in CI workflows from `3.29.2` -&gt; `3.32.2`. ([54af7ba](https://github.com/Apadmi-Engineering/Mockzilla/commit/54af7ba8ffdc021100a11a40608997e4ee9bb9b5))
* Fix minor Flutter warnings ([#378](https://github.com/Apadmi-Engineering/Mockzilla/issues/378)) ([54af7ba](https://github.com/Apadmi-Engineering/Mockzilla/commit/54af7ba8ffdc021100a11a40608997e4ee9bb9b5))
* Update Flutter package inter-dependencies ([afe63a4](https://github.com/Apadmi-Engineering/Mockzilla/commit/afe63a49275f57e17df814addd0f922bb5aca22d))

## [1.0.3](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.0.2...flutter_mockzilla_ios-v1.0.3) (2025-04-09)


### Bug Fixes

* Bump `freezed` dependency from 2.X -&gt; 3.x ([b8df550](https://github.com/Apadmi-Engineering/Mockzilla/commit/b8df5501b415206537278fd7d93fd5a6aa8ecb8e))

## [1.0.2](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.0.1...flutter_mockzilla_ios-v1.0.2) (2025-01-17)


### Bug Fixes

* Hides `FlutterCallbackProvider` from exported symbols ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))
* Loosens dependency constraints ([93149c8](https://github.com/Apadmi-Engineering/Mockzilla/commit/93149c8d647dfbd8c149b4458a91135fbffab437))

## [1.0.1](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter_mockzilla_ios-v1.0.0...flutter_mockzilla_ios-v1.0.1) (2025-01-15)


### Bug Fixes

* Fix crash in `mockzilla_ios` caused by multiple requests in quick succession ([85d21a8](https://github.com/Apadmi-Engineering/Mockzilla/commit/85d21a8acc685dc5e0c7db1fe7a726b3d31e56d4))

## [1.0.0](https://github.com/Apadmi-Engineering/Mockzilla/releases/tag/flutter_mockzilla_ios-v1.0.0) (2025-01-09)


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

* Upgrades `pigeon` from `12.0.1` -> `22.6.0`.
* Fixes an issue in the marshalling of request headers from bridge models to Dart.

## 1.0.0-dev.0

* Updates underlying KMP library to 2.0.1.

## 0.1.2

* Fixes an issue where HTTP request body was not passed from native models to Dart
  [#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172).

## 0.1.1

* Fixes an issue where `MockzillaIos.startMockzilla()` was referring to a missing
field in `MockzillaConfig`.

## 0.1.0

* Initial open-source release.
