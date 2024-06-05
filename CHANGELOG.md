# Changelog

## [2.1.0](https://github.com/Apadmi-Engineering/Mockzilla/compare/2.0.0...v2.1.0) (2024-06-05)


### Features

* Update iOS SDK to use persisted device IDs ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Use android advertising ID as a persistant device identifier ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))


### Bug Fixes

* Attempt to fix discovery ([fbd907e](https://github.com/Apadmi-Engineering/Mockzilla/commit/fbd907e97e7cf104404d1e0ae12e3778bbd974eb))
* Replace Swift bonjour service with direct C interop ([d4ca3f4](https://github.com/Apadmi-Engineering/Mockzilla/commit/d4ca3f4e6b6963ba8657145d28f1887aef013d6a))

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
