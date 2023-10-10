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

### get_version_name

```sh
[bundle exec] fastlane get_version_name
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

### ios lib_pull_request

```sh
[bundle exec] fastlane ios lib_pull_request
```

iOS target for the lib

### ios generate_xcframework

```sh
[bundle exec] fastlane ios generate_xcframework
```

Generate XCFramework

### ios publish_spm_package

```sh
[bundle exec] fastlane ios publish_spm_package
```

Deploy the package to github

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

### android lib_pull_request

```sh
[bundle exec] fastlane android lib_pull_request
```

Android target for the lib

### android management_ui_pull_request

```sh
[bundle exec] fastlane android management_ui_pull_request
```



### android update_reference_screenshots

```sh
[bundle exec] fastlane android update_reference_screenshots
```

Generate screenshots and upload them

### android demo_android_pull_request

```sh
[bundle exec] fastlane android demo_android_pull_request
```

Android target for the pure android demo

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
