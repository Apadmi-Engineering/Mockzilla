# Changelog

## [1.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/flutter-mockzilla-v0.1.0...flutter-mockzilla-v1.0.0) (2024-07-02)


### ⚠ BREAKING CHANGES

* Replaces `key` in `EndpointConfig` constructor with optional `customKey`.

### Features

* Add default value to `MockzillaConfig` parameters ([22ce858](https://github.com/Apadmi-Engineering/Mockzilla/commit/22ce8583bea6a0a55ec621d832dbb0f31735d264))
* add documentation and export builders ([8e3ccbe](https://github.com/Apadmi-Engineering/Mockzilla/commit/8e3ccbe08e44e80dde7f60914604fbc18dbdad66))
* add endpoint configuration builder ([bea825f](https://github.com/Apadmi-Engineering/Mockzilla/commit/bea825fef2b6766d929e63583a5002e388bd412b))
* Add missing `LogLevel` ([33794b1](https://github.com/Apadmi-Engineering/Mockzilla/commit/33794b1c446c854b5bbe8a28a0f1a88489da28fe))
* build out builder steps ([8b75c34](https://github.com/Apadmi-Engineering/Mockzilla/commit/8b75c341a093d1cf4c614b52804dbf2d92f3a995))
* create addEndpoint method for dart facing ([ebcfadc](https://github.com/Apadmi-Engineering/Mockzilla/commit/ebcfadce9fef3746c1068e12a304e5532621878c))
* create builder interface and add documentation ([98ef514](https://github.com/Apadmi-Engineering/Mockzilla/commit/98ef514a89df02f1eefd2452d2c29c6c3665bbf9))
* Generate Dart-iOS Pigeon bridge ([7003622](https://github.com/Apadmi-Engineering/Mockzilla/commit/70036227e27e96f5b728e3308b4e17b9b14d7a4f))
* Implement `firstWhereOrNull` list util ([0546cd4](https://github.com/Apadmi-Engineering/Mockzilla/commit/0546cd40e987320263514bee9113888f42d43ba6))
* Implement `MockzillaIos` class ([c69bb6a](https://github.com/Apadmi-Engineering/Mockzilla/commit/c69bb6a0115c1cc71a9a8f46c9462a69bdf9504a))
* Implement `startServer` + `stopServer` in `mockzilla_ios` ([127a898](https://github.com/Apadmi-Engineering/Mockzilla/commit/127a898de9a807051a5488fe63814c2560bbf6e5))
* implement builder with new builder steps ([44297e6](https://github.com/Apadmi-Engineering/Mockzilla/commit/44297e63b050ea68e3266347f08a2ee2a37edbd6))
* Implement Dart-side marshalling methods for `mockzilla_ios` ([8bbde96](https://github.com/Apadmi-Engineering/Mockzilla/commit/8bbde96dc1c80bb7701cc3f895af6c155fb9226f))
* Implement mapping from Pigeon bridge models to Swift models in `mockzilla_ios` ([3c500ad](https://github.com/Apadmi-Engineering/Mockzilla/commit/3c500ad406949cdef28d7069878b7598583cd5df))
* Implement platform interface in `mockzilla_ios` ([161fe49](https://github.com/Apadmi-Engineering/Mockzilla/commit/161fe49b2c731655cd984b7288d08a900daf9e39))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* make default headers with application/json ([a1e01f3](https://github.com/Apadmi-Engineering/Mockzilla/commit/a1e01f357ae785bf07e81886a8fee40287316d1d))
* make endpoints not required and default to empty list ([d1da896](https://github.com/Apadmi-Engineering/Mockzilla/commit/d1da8963e7b9a27886bab7bf2b1b66027a4819bb))
* Update `EndpointConfig` constructor ([91289a3](https://github.com/Apadmi-Engineering/Mockzilla/commit/91289a3cb072b346411356e2ce2c09b83d15b25d))
* Update `mockzilla_ios` plugin to use new `SwiftMockzilla` interface ([b4cf439](https://github.com/Apadmi-Engineering/Mockzilla/commit/b4cf43993e2932dd133c642f517784b17751df3a))
* update example to use addEndpoint ([7749957](https://github.com/Apadmi-Engineering/Mockzilla/commit/77499573a95fbf9b8ecf688495d8a91bf7f8b6cc))


### Bug Fixes

* Capitalise `mockzilla_ios` `HttpMethod` raw values ([497169e](https://github.com/Apadmi-Engineering/Mockzilla/commit/497169ec718dcb9640a6f1c81bc3aff2ab9f7757))
* fix mockzilla_android and ios breaking changes ([80abbec](https://github.com/Apadmi-Engineering/Mockzilla/commit/80abbec6e70b93717b8c7db5abf89abb08ffd0bb))
* regenerate models ([060a7b5](https://github.com/Apadmi-Engineering/Mockzilla/commit/060a7b59af516cde6847def92d1c8ca83e78927b))
* Remove cast of Swift boolean to `KotlinBoolean`, use constructor instead ([561806f](https://github.com/Apadmi-Engineering/Mockzilla/commit/561806fa47cd0a9d5e652e55a007ed6f5eb88039))
* remove key param from endpoint config builder ([f158d6d](https://github.com/Apadmi-Engineering/Mockzilla/commit/f158d6db05f96f9746d279a7be0f16211645a6a2))
* Remove placeholder data ++ fix parsing in `mockzilla_ios` bridging utils ([8ea5a99](https://github.com/Apadmi-Engineering/Mockzilla/commit/8ea5a999a168746a54ab6d413b7f9f9393d233cc))
* Resolve endpoints not being added due to use of an unmodifiable list ([640d833](https://github.com/Apadmi-Engineering/Mockzilla/commit/640d83324bd91627a325a25ad7bed35ebcb44939))
* Resolve threads warning in `mockzilla_ios`. ([7f36bbb](https://github.com/Apadmi-Engineering/Mockzilla/commit/7f36bbb35810f182d5fc63ae26626563e02111bf))
* return endpoint config builder instead of old handler step ([97c761f](https://github.com/Apadmi-Engineering/Mockzilla/commit/97c761fcaa9895efdebd201404a289327b5ab2b0))
* Update `mockzilla_ios` entrypoint to get it to compile ([794af1c](https://github.com/Apadmi-Engineering/Mockzilla/commit/794af1c7dacc40a956c5ba6b5b3f12e521716a4c))
* Update Flutter SDK version constraint in pubspec.yaml ([c635907](https://github.com/Apadmi-Engineering/Mockzilla/commit/c635907f5dd4149a541cba212099d76d9dc6d6b6))
* Update Mockzilla class names in `mockzilla_ios` host-side code ([7e78fa4](https://github.com/Apadmi-Engineering/Mockzilla/commit/7e78fa489620061af565fd3a602314a0143f00c1))
* Updates marshalling of `MockzillaConfig` in `mockzilla_android` + `mockzilla_ios` to account for removed fields. ([9ba7e81](https://github.com/Apadmi-Engineering/Mockzilla/commit/9ba7e81d157c9e8486d5002102cd65c91c938e1f))
* Use error handler in `mockzilla_ios` for error response. ([683543e](https://github.com/Apadmi-Engineering/Mockzilla/commit/683543eddc90cf2a70d99cadb201f97a811d734f))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))
