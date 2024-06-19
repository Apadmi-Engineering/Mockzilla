# Overview
A solution for running and configuring a local HTTP server to mimic REST API endpoints used by your iOS, Android or [KMM](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html) application.

## Supported Environments
- Native iOS apps (written in Swift)
- Native Android apps
- Kotlin Multiplatform Apps (Android & iOS targets only).

## Quick Start
!!! important
    Mockzilla does not support HTTPS, all traffic is cleartext HTTP.

!!! warning
    Mockzilla is **not** a production tool. It is for development and testing only. It should **never** be deployed to 
    production. Running a server on device may introduce un-foreseen security issues.

## Installation

=== "KMM & Android"
    Add the gradle dependency:
    
    ```kotlin
    implementation("com.apadmi:mockzilla:{{ get_version() }}")
    ```
=== "iOS"
    Add the SPM dependency in XCode:

    1. File > Swift Packages > Add Package Dependency
    2. Add `https://github.com/Apadmi-Engineering/SwiftMockzilla.git`

    !!! note 
        Note: This is not for KMM projects (for those, the gradle dependecy should be added to `shared` source set). 
        This SPM dependency is for purely native iOS apps only.

=== "Flutter"
    Either install the package using:

    ```shell
    flutter pub add mockzilla
    ```

    Or add the dependency in your pubspec.yaml file directly:

    ```yaml
    mockzilla: <version>
    ```

## Starting The Server

Mockzilla is entirely driven by a config object which is used to start the server.

### (1): Create the config:

=== "Kotlin"
    ```kotlin
    val config = MockzillaConfig.Builder()
        .addEndpoint(
            EndpointConfiguration
                .Builder("Hello World")
                .setDefaultHandler {
                    MockzillaHttpResponse(body = "Hello World")
                })
        .build()
    ```
=== "Swift"
    ```swift
    let config = MockzillaConfigBuilder()
        .addEndpoint(endpoint: EndpointConfigurationBuilder(id: "Hello world")
            .setDefaultHandler { _ in
                MockzillaHttpResponse(body: "Hello world")
            }.build()
        ).build()
    ```
=== "Flutter"
    ```dart
    final mockzillaConfig = MockzillaConfig().addEndpoint(
        () => EndpointConfig(
            name: "Hello world",
            endpointMatcher: (request) => request.uri.endsWith("/hello-world"),
            defaultHandler: (request) => const MockzillaHttpResponse(
                body: "Hello world",
            ),
            errorHandler: (request) => const MockzillaHttpResponse(
                statusCode: 418,
            ),
        ),
    );
    ```
See [here](./endpoints/) for more information on configuring your endpoints. (Including compile-time safety!)

### (2): Just start the server!

!!! note 
    For KMM apps, even though all the configuration can be defined in Kotlin. The server should still be started directly from within native code on both platforms.
    See the [KMM demo](https://github.com/Apadmi-Engineering/Mockzilla/tree/develop/demo-kmm) for an example.

=== "Android Application"
    ```kotlin
    class RootApplication : Application() {
        
        override fun onCreate() {
            super.onCreate()
    
            val config = MockzillaConfig.Builder()....
            startMockzilla(config, this)
        }
    }
    ```
=== "iOS App Delegate"
    ```swift
    import UIKit
    import SwiftMockzilla
    import mockzilla
    
    @main
    class AppDelegate: UIResponder, UIApplicationDelegate {
    
        func application(_: UIApplication,
                         didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]?) -> Bool
        {
            let config = MockillaConfig.Builder()...
            startMockzilla(config: config)
            
            return true
        }
    }
    ```
=== "Flutter"
    ```dart
        // Make sure to call this before starting Mockzilla!
        WidgetsFlutterBinding.ensureInitialized();
        
        await Mockzilla.startMockzilla(mockzillaConfig);
    ```

### (3): Call the server from your client code

Mockzilla listens for calls to `http://localhost:8080/local-mock` (this should be your base url).

To configure the port see [here](../dokka/mockzilla-common/com.apadmi.mockzilla.lib.models/-mockzilla-config/-builder/).

## Recommendation

Since Mockzilla shouldn't be included in production binaries, we recommend creating a new product flavour specifically 
for the mock and only including this dependency for this variant.

For KMM projects this will require creating a new KMM module in your project specifically for the mock.

## Tips

Ensure your development machine and test device are on the same wifi network. You can replace `localhost` with your 
device's IP addresss and try calling these endpoints from Postman (or a similar REST client.)

![alt text](img/postman-example.png "Postman example")