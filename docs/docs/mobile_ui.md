# Mockzilla Mobile Ui (alpha)

!!! warning
    Warning: This plugin is still experimental and may contain bugs! The API is also subject to change

Mockzilla provides embedded Ui into your App to control the server at runtime.

## Installation

=== "KMP & Android"
    Add the gradle dependency:
    
    ```kotlin
    implementation("com.apadmi:mockzilla-mobile-ui:{{ get_mobile_ui_version() }}")
    ```
=== "iOS"
    Add the SPM dependency in XCode:

    1. File > Swift Packages > Add Package Dependency
    2. Add `https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi`

    !!! note 
        Note: This is not for KMP projects (for those, the gradle dependecy should be added to `shared` source set). 
        This SPM dependency is for purely native iOS apps only.

=== "Flutter"
    Either install the package using:

    ```shell
    flutter pub add mockzilla_ui_mobile
    ```

    Or add the dependency in your pubspec.yaml file directly:

    ```yaml
    mockzilla_ui_mobile: {{ get_mobile_ui_version() }}
    ```

## Setup

If you've not configured the Mockzilla server yet, then do that first [here](../quick-start/)!

### Launch The Embedded Ui

You can do this from a button click or any trigger in your app code.

=== "Kotlin"
    ```kotlin
    import com.apadmi.mockzilla.mobile.ui.launchManagementUi

    launchManagementUi(context /* Activity Context */)
    ```
=== "Swift"
    ```swift
    import SwiftMockzillaMobileUi

    launchManagementUiSwift()
    ```
=== "Flutter"
    ```dart
    import 'package:mockzilla_ui_mobile/mockzilla_ui_mobile.dart';

    MockzillaUiMobile.launchManagementUi();
    ```

This will launch the Ui overlay allowing a user to configure the mocked endpoints.