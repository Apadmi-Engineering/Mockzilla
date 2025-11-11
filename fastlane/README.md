fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

### generate_docs

```sh
[bundle exec] fastlane generate_docs
```



### flutter_lib_pull_request

```sh
[bundle exec] fastlane flutter_lib_pull_request
```

Flutter target for the lib

### demo_flutter_pull_request

```sh
[bundle exec] fastlane demo_flutter_pull_request
```



### download_mockzilla_js_assets

```sh
[bundle exec] fastlane download_mockzilla_js_assets
```



### download_mockzilla_ui_mobile_js_assets

```sh
[bundle exec] fastlane download_mockzilla_ui_mobile_js_assets
```



### publish_to_maven_local

```sh
[bundle exec] fastlane publish_to_maven_local
```

Publish to maven local

### publish_to_maven

```sh
[bundle exec] fastlane publish_to_maven
```

Publish to maven remote

### build_js_artifacts

```sh
[bundle exec] fastlane build_js_artifacts
```

Generate JavaScript artifacts

### get_core_mockzilla_version_name

```sh
[bundle exec] fastlane get_core_mockzilla_version_name
```



### lib_mockzilla_management_pull_request

```sh
[bundle exec] fastlane lib_mockzilla_management_pull_request
```

Run tests for management module

### publish_mobile_ui_to_maven_local

```sh
[bundle exec] fastlane publish_mobile_ui_to_maven_local
```

Publish to maven local

### publish_mobile_ui_to_maven

```sh
[bundle exec] fastlane publish_mobile_ui_to_maven
```

Publish to maven remote

### build_mobile_ui_js_artifacts

```sh
[bundle exec] fastlane build_mobile_ui_js_artifacts
```

Generate JavaScript artifacts

### management_ui_pre_deploy_checks

```sh
[bundle exec] fastlane management_ui_pre_deploy_checks
```

During deployment we don't use local packages, but for deployment we have to, so run tests against the deployed core binaries

### management_ui_pull_request

```sh
[bundle exec] fastlane management_ui_pull_request
```



### combined_pull_request

```sh
[bundle exec] fastlane combined_pull_request
```



----


## iOS

### ios demo_kmm_pull_request

```sh
[bundle exec] fastlane ios demo_kmm_pull_request
```

iOS target for the kmm demo

### ios generate_framework_and_podspec

```sh
[bundle exec] fastlane ios generate_framework_and_podspec
```

Generate Podspec and Framework

### ios publish_swift_package

```sh
[bundle exec] fastlane ios publish_swift_package
```

Deploy the package to github & push podspec

### ios lib_mockzilla_pull_request

```sh
[bundle exec] fastlane ios lib_mockzilla_pull_request
```

iOS target for the lib

### ios lib_swift_mockzilla_pull_request

```sh
[bundle exec] fastlane ios lib_swift_mockzilla_pull_request
```

Build and test SwiftMockzilla

### ios publish_mobile_ui_swift_package

```sh
[bundle exec] fastlane ios publish_mobile_ui_swift_package
```

Deploy the package to github & push podspec

### ios demo_ios_pull_request

```sh
[bundle exec] fastlane ios demo_ios_pull_request
```

iOS target for the pure iOS demo

----


## Android

### android demo_kmm_pull_request

```sh
[bundle exec] fastlane android demo_kmm_pull_request
```

Android target for the kmm demo

### android lib_mockzilla_pull_request

```sh
[bundle exec] fastlane android lib_mockzilla_pull_request
```

Android target for the lib

### android demo_android_pull_request

```sh
[bundle exec] fastlane android demo_android_pull_request
```

Android target for the pure android demo

----


## js

### js lib_mockzilla_pull_request

```sh
[bundle exec] fastlane js lib_mockzilla_pull_request
```

JavaScript target for the lib

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
