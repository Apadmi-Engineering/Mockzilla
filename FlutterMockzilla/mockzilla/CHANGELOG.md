## 0.1.2

* Updates example app to include request body in fetch packages request.

## 2.0.0 (2024-10-23)


### Features

* add documentation and export builders ([8e3ccbe](https://github.com/Apadmi-Engineering/Mockzilla/commit/8e3ccbe08e44e80dde7f60914604fbc18dbdad66))
* create addEndpoint method for dart facing ([ebcfadc](https://github.com/Apadmi-Engineering/Mockzilla/commit/ebcfadce9fef3746c1068e12a304e5532621878c))
* Integrate bonjour and enable network discovery ([34ecf69](https://github.com/Apadmi-Engineering/Mockzilla/commit/34ecf6923db72438df826ef1593d3a946176eebc))
* Update `mockzilla_ios` plugin to use new `SwiftMockzilla` interface ([b4cf439](https://github.com/Apadmi-Engineering/Mockzilla/commit/b4cf43993e2932dd133c642f517784b17751df3a))
* update example to use addEndpoint ([7749957](https://github.com/Apadmi-Engineering/Mockzilla/commit/77499573a95fbf9b8ecf688495d8a91bf7f8b6cc))


### Bug Fixes

* HTTP request mapping in `mockzilla_android` + `mockzilla_ios` ([#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172)) ([9644a10](https://github.com/Apadmi-Engineering/Mockzilla/commit/9644a102b4af40c63ea3caa56db838eba04fb648))
* Resolve endpoints not being added due to use of an unmodifiable list ([640d833](https://github.com/Apadmi-Engineering/Mockzilla/commit/640d83324bd91627a325a25ad7bed35ebcb44939))
* Trigger deploy with updated dependencies ([b5200c6](https://github.com/Apadmi-Engineering/Mockzilla/commit/b5200c6cff3e6c6e4ad258e4900e318831151444))
* Update `mockzilla_ios` entrypoint to get it to compile ([794af1c](https://github.com/Apadmi-Engineering/Mockzilla/commit/794af1c7dacc40a956c5ba6b5b3f12e521716a4c))
* Update Flutter SDK version constraint in pubspec.yaml ([c635907](https://github.com/Apadmi-Engineering/Mockzilla/commit/c635907f5dd4149a541cba212099d76d9dc6d6b6))


### Miscellaneous Chores

* release 2.0.0 ([c5aeb78](https://github.com/Apadmi-Engineering/Mockzilla/commit/c5aeb78c070a0dcee855920b6f0dce1966b98245))
* release 2.0.0 ([4fa2326](https://github.com/Apadmi-Engineering/Mockzilla/commit/4fa2326c45a13f764dbe8549cf91bae36db85a1b))

## 0.1.1

* Adds default value of `{"Content-Type": "application/json"}` for parameter `headers` in
  `MockzillaHttpResponse`.
* Bumps dependencies of federated plugins
  * `mockzilla_platform_interface` from `^0.1.0` -> `^0.2.0`
  * `mockzilla_android` from `^0.1.0` -> `^0.1.1`
  * `mockzilla_ios` from `^0.1.0` -> `^0.1.1`

## 0.1.0

* Initial open-source release.
