## 0.1.2

* Fixes an issue where HTTP request body was not passed from native models to Dart
  [#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172).

## 2.0.0 (2024-10-23)


### Features

* Implement `firstWhereOrNull` list util ([0546cd4](https://github.com/Apadmi-Engineering/Mockzilla/commit/0546cd40e987320263514bee9113888f42d43ba6))
* Implement `MockzillaIos` class ([c69bb6a](https://github.com/Apadmi-Engineering/Mockzilla/commit/c69bb6a0115c1cc71a9a8f46c9462a69bdf9504a))
* Implement `startServer` + `stopServer` in `mockzilla_ios` ([127a898](https://github.com/Apadmi-Engineering/Mockzilla/commit/127a898de9a807051a5488fe63814c2560bbf6e5))
* Implement Dart-side marshalling methods for `mockzilla_ios` ([8bbde96](https://github.com/Apadmi-Engineering/Mockzilla/commit/8bbde96dc1c80bb7701cc3f895af6c155fb9226f))
* Implement platform interface in `mockzilla_ios` ([161fe49](https://github.com/Apadmi-Engineering/Mockzilla/commit/161fe49b2c731655cd984b7288d08a900daf9e39))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* Update `mockzilla_ios` plugin to use new `SwiftMockzilla` interface ([b4cf439](https://github.com/Apadmi-Engineering/Mockzilla/commit/b4cf43993e2932dd133c642f517784b17751df3a))


### Bug Fixes

* Add `releaseModeConfig` field back to `MockzillaConfig` interface. ([37243da](https://github.com/Apadmi-Engineering/Mockzilla/commit/37243da848f2b9cba457fbcb1c09610dfab3ce38))
* Add missing parameter in HTTP request marshalling. ([fa762f1](https://github.com/Apadmi-Engineering/Mockzilla/commit/fa762f148425475b342f71fdb40960ae7d984cf7))
* Capitalise `mockzilla_ios` `HttpMethod` raw values ([497169e](https://github.com/Apadmi-Engineering/Mockzilla/commit/497169ec718dcb9640a6f1c81bc3aff2ab9f7757))
* fix mockzilla_android and ios breaking changes ([80abbec](https://github.com/Apadmi-Engineering/Mockzilla/commit/80abbec6e70b93717b8c7db5abf89abb08ffd0bb))
* HTTP request mapping in `mockzilla_android` + `mockzilla_ios` ([#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172)) ([9644a10](https://github.com/Apadmi-Engineering/Mockzilla/commit/9644a102b4af40c63ea3caa56db838eba04fb648))
* Remove cast of Swift boolean to `KotlinBoolean`, use constructor instead ([561806f](https://github.com/Apadmi-Engineering/Mockzilla/commit/561806fa47cd0a9d5e652e55a007ed6f5eb88039))
* Remove placeholder data ++ fix parsing in `mockzilla_ios` bridging utils ([8ea5a99](https://github.com/Apadmi-Engineering/Mockzilla/commit/8ea5a999a168746a54ab6d413b7f9f9393d233cc))
* Resolve threads warning in `mockzilla_ios`. ([7f36bbb](https://github.com/Apadmi-Engineering/Mockzilla/commit/7f36bbb35810f182d5fc63ae26626563e02111bf))
* Trigger deploy with updated dependencies ([b5200c6](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5200c6cff3e6c6e4ad258e4900e318831151444))
* Update `mockzilla_ios` entrypoint to get it to compile ([794af1c](https://github.com/Apadmi-Engineering/Mockzilla/commit/794af1c7dacc40a956c5ba6b5b3f12e521716a4c))
* Update Flutter SDK version constraint in pubspec.yaml ([c635907](https://github.com/Apadmi-Engineering/Mockzilla/commit/c635907f5dd4149a541cba212099d76d9dc6d6b6))
* Update Mockzilla class names in `mockzilla_ios` host-side code ([7e78fa4](https://github.com/Apadmi-Engineering/Mockzilla/commit/7e78fa489620061af565fd3a602314a0143f00c1))
* Updates marshalling of `MockzillaConfig` in `mockzilla_android` + `mockzilla_ios` to account for removed fields. ([9ba7e81](https://github.com/Apadmi-Engineering/Mockzilla/commit/9ba7e81d157c9e8486d5002102cd65c91c938e1f))
* Use error handler in `mockzilla_ios` for error response. ([683543e](https://github.com/Apadmi-Engineering/Mockzilla/commit/683543eddc90cf2a70d99cadb201f97a811d734f))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))

## 0.1.1

* Fixes an issue where `MockzillaIos.startMockzilla()` was referring to a missing
field in `MockzillaConfig`.

## 0.1.0

* Initial open-source release.
