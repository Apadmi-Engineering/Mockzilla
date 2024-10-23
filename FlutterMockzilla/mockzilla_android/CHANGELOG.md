## 0.1.2

* Fixes an issue where HTTP request body was not passed from native models to Dart 
[#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172).

## 2.0.0 (2024-10-23)


### Features

* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))


### Bug Fixes

* Add `releaseModeConfig` field back to `MockzillaConfig` interface. ([37243da](https://github.com/Apadmi-Engineering/Mockzilla/commit/37243da848f2b9cba457fbcb1c09610dfab3ce38))
* Add missing parameter in HTTP request marshalling. ([fa762f1](https://github.com/Apadmi-Engineering/Mockzilla/commit/fa762f148425475b342f71fdb40960ae7d984cf7))
* fix mockzilla_android and ios breaking changes ([80abbec](https://github.com/Apadmi-Engineering/Mockzilla/commit/80abbec6e70b93717b8c7db5abf89abb08ffd0bb))
* HTTP request mapping in `mockzilla_android` + `mockzilla_ios` ([#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172)) ([9644a10](https://github.com/Apadmi-Engineering/Mockzilla/commit/9644a102b4af40c63ea3caa56db838eba04fb648))
* Remove `packageId` from `mockzilla_android` manifest ([3113fa4](https://github.com/Apadmi-Engineering/Mockzilla/commit/3113fa4d2ea261ba01b905c5adb488e4afdfa525))
* Trigger deploy with updated dependencies ([b5200c6](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5200c6cff3e6c6e4ad258e4900e318831151444))
* Updates marshalling of `MockzillaConfig` in `mockzilla_android` + `mockzilla_ios` to account for removed fields. ([9ba7e81](https://github.com/Apadmi-Engineering/Mockzilla/commit/9ba7e81d157c9e8486d5002102cd65c91c938e1f))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))

## 0.1.1

* Removes `package` attribute from AndroidManifest that was incompatible with AGP 8.
* Fixes an issue where `MockzillaIos.startMockzilla()` was referring to a missing
  field in `MockzillaConfig`.

## 0.1.0

* Initial open-source release.
