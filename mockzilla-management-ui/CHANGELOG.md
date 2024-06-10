# Changelog

## [1.0.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/mockzilla-management-ui-v1.0.0...mockzilla-management-ui-v1.0.0) (2024-06-10)


### Features

* Add eventbus so that different bits of UI update eachother ([48c4c10](https://github.com/Apadmi-Engineering/Mockzilla/commit/48c4c1064267167726f859f43b7152edb0d3c83d))
* Endpoint detail component now fully wired up ([7208ef3](https://github.com/Apadmi-Engineering/Mockzilla/commit/7208ef337914f846da9495f8d31a09b5a24893e6))
* First pass at adb integration ([49c670e](https://github.com/Apadmi-Engineering/Mockzilla/commit/49c670ee692f47815f17034350564ec736046327))
* First pass at error handling ([7afde53](https://github.com/Apadmi-Engineering/Mockzilla/commit/7afde5345d7e501fa84cbec5711665ecec94348a))
* First pass wiring up the endpoints list ([cf14be2](https://github.com/Apadmi-Engineering/Mockzilla/commit/cf14be2d84d9ddc3bfa9653d0b48bc256845c188))
* Fix the way updates work ([68cc4c2](https://github.com/Apadmi-Engineering/Mockzilla/commit/68cc4c215c8adef63b7651b295948656aa4fcff0))
* Implement UI for using Presets in desktop ([ec9f44e](https://github.com/Apadmi-Engineering/Mockzilla/commit/ec9f44ee0cf18835c55841d35e02dba55cffd0f4))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* Obscure the polling network calls from logs since they're just spam ([a5902e3](https://github.com/Apadmi-Engineering/Mockzilla/commit/a5902e3cfd6d7de0651d9dfe0e48dc4e7079448e))
* Update iOS SDK to use persisted device IDs ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Use android advertising ID as a persistant device identifier ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Wire up resolution of emulators in ADB against the host addresses from bonjour ([854fc77](https://github.com/Apadmi-Engineering/Mockzilla/commit/854fc779576b88c2b09f6f7cdcc82f651b774d1a))
* Wire up select all + tooltip ([620b1ec](https://github.com/Apadmi-Engineering/Mockzilla/commit/620b1ececc612c517c88c3986229ca5a180ff007))


### Bug Fixes

* Attempt to fix discovery ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Fix compose compiler versions and update multipleform template ([c198adf](https://github.com/Apadmi-Engineering/Mockzilla/commit/c198adfbee403ed53757cbb0e4de636409b31b1b))
* Fix paparazzi dependency issues ([0bb4d89](https://github.com/Apadmi-Engineering/Mockzilla/commit/0bb4d89b2ce872d0613cf0853df130b3cf9e4950))
* Mockzilla server wasn't handling patches with null values correctly ([25feae0](https://github.com/Apadmi-Engineering/Mockzilla/commit/25feae036792b0123c60c8128e3ee4f490d0c7c4))
* Replace Swift bonjour service with direct C interop ([d4ca3f4](https://github.com/Apadmi-Engineering/Mockzilla/commit/d4ca3f4e6b6963ba8657145d28f1887aef013d6a))
* Turns out jmdns doesn't work on Android ([e5fe202](https://github.com/Apadmi-Engineering/Mockzilla/commit/e5fe2023e6a3b7ea260642a1b991742c48ca2415))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))
