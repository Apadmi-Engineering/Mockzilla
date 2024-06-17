# Changelog

## [2.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/mockzilla-v1.2.1-alpha2...mockzilla-v2.0.0) (2024-06-17)


### Features

* Add `setName` method to endpoint builder ([cf63e7c](https://github.com/Apadmi-Engineering/Mockzilla/commit/cf63e7c911eb5027cb3a96e18295269651bc7b89))
* Add ability to get all endpoint data and to clear caches ([aaf0c53](https://github.com/Apadmi-Engineering/Mockzilla/commit/aaf0c5326e3570cffca9eb94bb44b994f93cbc40))
* Add endpoint for getting presets ([34518b4](https://github.com/Apadmi-Engineering/Mockzilla/commit/34518b4e605badda1d26bd79bd19cec260daec6b))
* Deprecate all the web apis and replace them with dashboard override presets ([d1e5c2a](https://github.com/Apadmi-Engineering/Mockzilla/commit/d1e5c2ae3a9ff12273623feabe1d7e391766fd02))
* First pass updating the management API for new UI ([3b1d558](https://github.com/Apadmi-Engineering/Mockzilla/commit/3b1d558450928d6d9aef73ee96683a032f1c990a))
* Fix the way updates work ([68cc4c2](https://github.com/Apadmi-Engineering/Mockzilla/commit/68cc4c215c8adef63b7651b295948656aa4fcff0))
* Implement UI for using Presets in desktop ([ec9f44e](https://github.com/Apadmi-Engineering/Mockzilla/commit/ec9f44ee0cf18835c55841d35e02dba55cffd0f4))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* Remove more unused methods ([8705e38](https://github.com/Apadmi-Engineering/Mockzilla/commit/8705e38829af203989c72f1092d5f515c3111d24))
* Support endpoint versioning ([6f02df2](https://github.com/Apadmi-Engineering/Mockzilla/commit/6f02df25d33c8453e6e43f577bafc34756ad6f55))
* Update iOS SDK to use persisted device IDs ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Use android advertising ID as a persistant device identifier ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))


### Bug Fixes

* Attempt to fix discovery ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Fix compose compiler versions and update multipleform template ([c198adf](https://github.com/Apadmi-Engineering/Mockzilla/commit/c198adfbee403ed53757cbb0e4de636409b31b1b))
* Mockzilla server wasn't handling patches with null values correctly ([25feae0](https://github.com/Apadmi-Engineering/Mockzilla/commit/25feae036792b0123c60c8128e3ee4f490d0c7c4))
* Replace Swift bonjour service with direct C interop ([d4ca3f4](https://github.com/Apadmi-Engineering/Mockzilla/commit/d4ca3f4e6b6963ba8657145d28f1887aef013d6a))
* Update values in Podspec generation to match what Cocoapods expects ([977be26](https://github.com/Apadmi-Engineering/Mockzilla/commit/977be266f88857f9f99be664d8fbcb8f252cb2a4))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))

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
