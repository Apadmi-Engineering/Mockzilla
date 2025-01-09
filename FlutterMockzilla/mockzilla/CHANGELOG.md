## 1.0.0 (2025-01-09)

**⚠️ BREAKING CHANGES**

* Removes `delayVariance`, and `failureProbability` fields from endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Replaces `delayMean` with `mean` field for endpoint configurations ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Removes Web Api fields (these are replaced with dashboard overrides) ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Updates `startMockzilla` to return runtime information ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))

**Features**

* Adds support for Mockzilla Desktop ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Improves handling of port conflicts ([ff272ae](https://github.com/Apadmi-Engineering/Mockzilla/compare/55ec38c96e12c90753e9996420625c6f3c2a4da3...ff272ae419acd8de42baf29a5cb97cde0023f75a))
* Adds support for SPM ([bbcd7ba](https://github.com/Apadmi-Engineering/Mockzilla/compare/5363e6d11372011bfec9dae3841910f907cbd681...bbcd7baaeb6556255714de7f90bac80fb6b65595))

**Bug Fixes**

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
