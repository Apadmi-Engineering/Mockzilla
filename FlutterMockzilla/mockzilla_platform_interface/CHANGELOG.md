## 0.2.0

* Adds default value of `{"Content-Type": "application/json"}` for parameter `headers` in 
`MockzillaHttpResponse`.
* Removes generated `MockzillaConfig.releaseModeConfig` from platform interface.
* Removes generated `MockzillaConfig.additionalLogWriters` from platform interface.

## 2.0.0 (2024-10-23)


### ⚠ BREAKING CHANGES

* Replaces `key` in `EndpointConfig` constructor with optional `customKey`.

### Features

* Add default value to `MockzillaConfig` parameters ([22ce858](https://github.com/Apadmi-Engineering/Mockzilla/commit/22ce8583bea6a0a55ec621d832dbb0f31735d264))
* add documentation and export builders ([8e3ccbe](https://github.com/Apadmi-Engineering/Mockzilla/commit/8e3ccbe08e44e80dde7f60914604fbc18dbdad66))
* add endpoint configuration builder ([bea825f](https://github.com/Apadmi-Engineering/Mockzilla/commit/bea825fef2b6766d929e63583a5002e388bd412b))
* Add missing `LogLevel` ([33794b1](https://github.com/Apadmi-Engineering/Mockzilla/commit/33794b1c446c854b5bbe8a28a0f1a88489da28fe))
* build out builder steps ([8b75c34](https://github.com/Apadmi-Engineering/Mockzilla/commit/8b75c341a093d1cf4c614b52804dbf2d92f3a995))
* create builder interface and add documentation ([98ef514](https://github.com/Apadmi-Engineering/Mockzilla/commit/98ef514a89df02f1eefd2452d2c29c6c3665bbf9))
* implement builder with new builder steps ([44297e6](https://github.com/Apadmi-Engineering/Mockzilla/commit/44297e63b050ea68e3266347f08a2ee2a37edbd6))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* make default headers with application/json ([a1e01f3](https://github.com/Apadmi-Engineering/Mockzilla/commit/a1e01f357ae785bf07e81886a8fee40287316d1d))
* make endpoints not required and default to empty list ([d1da896](https://github.com/Apadmi-Engineering/Mockzilla/commit/d1da8963e7b9a27886bab7bf2b1b66027a4819bb))
* Update `EndpointConfig` constructor ([91289a3](https://github.com/Apadmi-Engineering/Mockzilla/commit/91289a3cb072b346411356e2ce2c09b83d15b25d))


### Bug Fixes

* Add `releaseModeConfig` field back to `MockzillaConfig` interface. ([37243da](https://github.com/Apadmi-Engineering/Mockzilla/commit/37243da848f2b9cba457fbcb1c09610dfab3ce38))
* regenerate models ([060a7b5](https://github.com/Apadmi-Engineering/Mockzilla/commit/060a7b59af516cde6847def92d1c8ca83e78927b))
* remove key param from endpoint config builder ([f158d6d](https://github.com/Apadmi-Engineering/Mockzilla/commit/f158d6db05f96f9746d279a7be0f16211645a6a2))
* return endpoint config builder instead of old handler step ([97c761f](https://github.com/Apadmi-Engineering/Mockzilla/commit/97c761fcaa9895efdebd201404a289327b5ab2b0))
* Trigger deploy with updated dependencies ([b5200c6](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5200c6cff3e6c6e4ad258e4900e318831151444))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))

## 0.1.0

* Initial open-source release.
