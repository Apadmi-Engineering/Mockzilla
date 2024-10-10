## 1.0.0-dev.0

* Updates underlying KMP library to 2.0.1.

## 0.1.2

* Fixes an issue where HTTP request body was not passed from native models to Dart 
[#172](https://github.com/Apadmi-Engineering/Mockzilla/issues/172).

## 0.1.1

* Removes `package` attribute from AndroidManifest that was incompatible with AGP 8.
* Fixes an issue where `MockzillaIos.startMockzilla()` was referring to a missing
  field in `MockzillaConfig`.

## 0.1.0

* Initial open-source release.
