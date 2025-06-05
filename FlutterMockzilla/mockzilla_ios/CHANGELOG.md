# Changelog

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
