# Mockzilla Mobile UI (alpha)

!!! warning
    This plugin is still experimental and may contain bugs! The API is also subject to change

Mockzilla provides an embedded UI for your App to control the server at runtime.

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
        This is not for KMP projects (for those, the gradle dependency should be added to `shared` source set). 
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

### Launch the Embedded UI

You can do this from a button click or any trigger in your app code.

=== "Kotlin"
    ```kotlin
    import com.apadmi.mockzilla.mobile.ui.launchManagementUi

    /// Android Target ///
    launchManagementUi(context /* Activity Context */)

    /// iOS Target ///
    launchManagementUi()

    // Or 

    // Handle the ViewController directly
    createManagementUiViewController(onClose = { 
        // Dismiss the ViewController
    }
    ```
=== "Swift"
    ```swift
    import SwiftMockzillaMobileUi

    launchManagementUiSwift()

    // Or

    // Handle the ViewController directly
    createManagementUiViewControllerSwift {
      // Dismiss the ViewController
    }
    ```
=== "Flutter"
    ```dart
    import 'package:mockzilla_ui_mobile/mockzilla_ui_mobile.dart';

    MockzillaUiMobile.launchManagementUi();
    ```

    !!! note
        On web, you can optionally call `MockzillaUiMobile.preloadAssets()` early (e.g. at app start) to
        speed up the first time the overlay is launched.
=== "Web"
    ```kotlin
    import com.apadmi.mockzilla.launchManagementUi

    launchManagementUi(rootId = "mockzilla-ui-root")
    ```

    If no element with the given `rootId` exists on the page, one is created automatically as a fixed,
    bottom-anchored panel. If you'd rather control placement and sizing yourself, add an element with a
    matching `id` to your page first - it'll be reused as-is.

This will launch the UI overlay, allowing a user to configure the mocked endpoints.

<video autoplay loop muted playsinline controls>
  <source src="img/mobile-ui.mp4" type="video/mp4">
</video>
