# Changelog

## [1.3.0-alpha2](https://github.com/Apadmi-Engineering/Mockzilla/compare/v1.2.0-alpha2...v1.3.0-alpha2) (2024-05-16)


### Features

* Add ability to get all endpoint data and to clear caches ([aaf0c53](https://github.com/Apadmi-Engineering/Mockzilla/commit/aaf0c5326e3570cffca9eb94bb44b994f93cbc40))
* Add endpoint for getting presets ([34518b4](https://github.com/Apadmi-Engineering/Mockzilla/commit/34518b4e605badda1d26bd79bd19cec260daec6b))
* Add mockzilla as dependency of native Android library ([b38e5e5](https://github.com/Apadmi-Engineering/Mockzilla/commit/b38e5e502c9be6891d0378733a07c60c1f409b5f))
* Deprecate all the web apis and replace them with dashboard override presets ([d1e5c2a](https://github.com/Apadmi-Engineering/Mockzilla/commit/d1e5c2ae3a9ff12273623feabe1d7e391766fd02))
* First pass updating the management API for new UI ([3b1d558](https://github.com/Apadmi-Engineering/Mockzilla/commit/3b1d558450928d6d9aef73ee96683a032f1c990a))
* Fix the way updates work ([68cc4c2](https://github.com/Apadmi-Engineering/Mockzilla/commit/68cc4c215c8adef63b7651b295948656aa4fcff0))
* Implement `firstWhereOrNull` list util ([2fc206c](https://github.com/Apadmi-Engineering/Mockzilla/commit/2fc206c5efceda5b9d955996b076bcc3bd7ee3e8))
* Implement `mockzilla_android` flutter code ([dac58bb](https://github.com/Apadmi-Engineering/Mockzilla/commit/dac58bba75c6dc6f52c399bc490f29424e30e584))
* Implement example project ([de5ac6f](https://github.com/Apadmi-Engineering/Mockzilla/commit/de5ac6f7ea613f58a68eab0a2725c1ada07aae5f))
* Implement marshalling from mockzilla interface models to pigeon models ([2a4b516](https://github.com/Apadmi-Engineering/Mockzilla/commit/2a4b51619548c6972ca05123864901dc4f29a575))
* Implement marshalling from pigeon models to native mockzilla models in `mockzilla_android` ([b94d65c](https://github.com/Apadmi-Engineering/Mockzilla/commit/b94d65c031f6a3c34f29765167335f4a17c14607))
* Implement native `mockzilla_android` class ([872908c](https://github.com/Apadmi-Engineering/Mockzilla/commit/872908c495e99e4b58f384c32e684bcc15d13760))
* Remove more unused methods ([8705e38](https://github.com/Apadmi-Engineering/Mockzilla/commit/8705e38829af203989c72f1092d5f515c3111d24))
* Setup Dart-facing `Mockzilla` interface ([957dec4](https://github.com/Apadmi-Engineering/Mockzilla/commit/957dec42d94e9945e8f08f72f15e795a02ffe3d5))
* Setup registration of `MockzillaAndroid` as platform interface instance on Android ([eec2588](https://github.com/Apadmi-Engineering/Mockzilla/commit/eec2588336ceeb06189e95202be3482bff464e7a))
* Support endpoint versioning ([6f02df2](https://github.com/Apadmi-Engineering/Mockzilla/commit/6f02df25d33c8453e6e43f577bafc34756ad6f55))
* Update mockzilla android pigeon interface ([abc512d](https://github.com/Apadmi-Engineering/Mockzilla/commit/abc512d526a23f09148658b5d793d08de7338324))


### Bug Fixes

* `MainActivity` package in Flutter example app ([9f795d6](https://github.com/Apadmi-Engineering/Mockzilla/commit/9f795d6cb4b6dbe35c5d5ae6327d9981c7b49c43))
* Add key to endpoint method invocations to specify an endpoint ([f8a5d11](https://github.com/Apadmi-Engineering/Mockzilla/commit/f8a5d11f552b9be16c864f0fd7f05f5aae3b7342))
* bump agp ([9693fc6](https://github.com/Apadmi-Engineering/Mockzilla/commit/9693fc6dd16b27a12aaad954ade212cec9fe2c00))
* bump remaining versions as much as possible ([40eeead](https://github.com/Apadmi-Engineering/Mockzilla/commit/40eeead8827c166cadde40d37721e1e5f6069205))
* exclude all the build folders from Diktat scanning ([b273669](https://github.com/Apadmi-Engineering/Mockzilla/commit/b2736696dc36d5d981619cdbbf179e5b259f1b5b))
* explicitly install setuptools to fix pkg_resource error ([833c457](https://github.com/Apadmi-Engineering/Mockzilla/commit/833c45773cbdde989b92fbede656663020352a9d))
* Fix `MockzillaLogger` interface ++ add release mode config to `MockzillaConfig` ([fc286f0](https://github.com/Apadmi-Engineering/Mockzilla/commit/fc286f0de367be795e94da48291db1dcac48d5e9))
* Fix compose compiler versions and update multipleform template ([c198adf](https://github.com/Apadmi-Engineering/Mockzilla/commit/c198adfbee403ed53757cbb0e4de636409b31b1b))
* Fix endpoint matching ([947a24e](https://github.com/Apadmi-Engineering/Mockzilla/commit/947a24e39be91f2b7e2219e30d7dd6bc434412a1))
* Fix paparazzi dependency issues ([0bb4d89](https://github.com/Apadmi-Engineering/Mockzilla/commit/0bb4d89b2ce872d0613cf0853df130b3cf9e4950))
* install our gem dependencies ([b92eb96](https://github.com/Apadmi-Engineering/Mockzilla/commit/b92eb9631a9e123933a27db0e537bc5f94b918e1))
* link in the SwiftMockzilla release notes had an extra 'v' ([6048258](https://github.com/Apadmi-Engineering/Mockzilla/commit/60482588ae7b379e207c308752ea09940abfb7d6))
* Native-side Android package name ([e7f1bf5](https://github.com/Apadmi-Engineering/Mockzilla/commit/e7f1bf5367dd453fed7958cb680c879d4f2e6e1c))
* Update `MockzillaFlutterApi` interface ([58b048c](https://github.com/Apadmi-Engineering/Mockzilla/commit/58b048c8f1b302e6a114114b3a806b30ee690ab4))


### Reverts

* Include generated code in Flutter packages ([a4192ae](https://github.com/Apadmi-Engineering/Mockzilla/commit/a4192aebd14f4f54e272f0db51da9417a3f6022e))

## [1.2.0-alpha2](https://github.com/Apadmi-Engineering/Mockzilla/compare/v1.1.0-alpha2...v1.2.0-alpha2) (2023-11-01)


### Features

* Stop the request body always being parsed as a string ([c49e7d9](https://github.com/Apadmi-Engineering/Mockzilla/commit/c49e7d9e00801dcda10abae76632acd2b729d73d))


### Bug Fixes

* wrong schema for monitor-logs response ([0d4cf22](https://github.com/Apadmi-Engineering/Mockzilla/commit/0d4cf22c008c905e39bc7ee3789c6a08143445ad))

## [1.1.0-alpha2](https://github.com/Apadmi-Engineering/Mockzilla/compare/v1.0.1-alpha2...v1.1.0-alpha2) (2023-09-29)


### Features

* Add the ability to specify custom log writers on mockzilla ([ce8108e](https://github.com/Apadmi-Engineering/Mockzilla/commit/ce8108e79fcaf2b198cf66878be167ac47fc3fd1))

## [1.0.1-alpha2](https://github.com/Apadmi-Engineering/Mockzilla/compare/1.0.0-alpha2...v1.0.1-alpha2) (2023-08-06)


### Bug Fixes

* Fix proguard rules to prevent users needing to add rules to app level files ([b00887d](https://github.com/Apadmi-Engineering/Mockzilla/commit/b00887dd5c9f859b2ded23936742ec173348b3a8))
* Fix runtime crash which happens only on proguarded non-debuggable builds ([60af941](https://github.com/Apadmi-Engineering/Mockzilla/commit/60af94106c42338eafa4e5c2505b6131d1ce2226))
* mimick a fix release to test release pipeline ([b97e8fb](https://github.com/Apadmi-Engineering/Mockzilla/commit/b97e8fb0ecdb259c6e0a503f8f61930f7d129a4b))
