
# Additional Configuration

Most Mockzilla configuration is relatively self-explanatory with API references available at the following:

* [Kotlin](../dokka/mockzilla-common/com.apadmi.mockzilla.lib.models/-mockzilla-config/-builder/index.html)
* [Dart](https://pub.dev/documentation/mockzilla/latest/)

## Logging

By default, Mockzilla outputs minimal logging. If more is needed to help with debugging, enable it as follows.

=== "Kotlin"
    ```kotlin
    val config = MockzillaConfig.Builder()
        .setLogLevel(MockzillaConfig.LogLevel.Verbose)
        ...
        .build()
    ```
=== "Swift"
    ```swift
    let config = MockzillaConfigBuilder()
        .setLogLevel(level: LogLevel.verbose)
        ...
        .build()
    ```
=== "Flutter"
    ```dart
    final mockzillaConfig = const MockzillaConfig(
        logLevel: LogLevel.verbose,
    );
    ```

## Release Mode

!!! warning
    We still don't recommend releasing to non-developers, even with this mode if at all avoidable.
    Mockzilla is NOT a production server substitute and should **NOT** be used in production.


By default, the Mockzilla server can be called by anyone on the network via the device's IP address. This is often useful for debugging.

If you need to release your mock app to non-developers, we recommend enabling "release mode" which does the following.

1. Introduces rate limiting to the server
2. Enforces rudamentary token authentication on each request (explained below).
3. Only allows connections from `127.0.0.1` (i.e from apps running on the device).


=== "Kotlin"
    ```kotlin
    val config = MockzillaConfig.Builder()
        .setIsReleaseModeEnabled(true)
        
        // Following is optional, the defaults should be fine for most apps
        .setReleaseModeConfig(
                MockzillaConfig.ReleaseModeConfig(
                    rateLimit = 60,
                    rateLimitRefillPeriod = 60.seconds,
                    tokenLifeSpan = 0.5.seconds
                )
            )
    ```
=== "Swift"
    ```swift
    let config = MockzillaConfigBuilder()
        .setIsReleaseModeEnabled(isRelease: true)
         // Following is optional, the defaults should be fine for most apps
        .setReleaseModeConfig(
            releaseConfig: MockzillaConfig.ReleaseModeConfig(
                rateLimit: 60,
                rateLimitRefillPeriod: 60_000, // milliseconds
                tokenLifeSpan: 500 // milliseconds
            )
        )
    ```

An additional header now needs to be added to each request. The header **changes per request** and is generated as follows.

=== "Kotlin"
    ```kotlin
    val params = startMockzilla(config, ..)

    // Generate a new header for each request.
    val header = params.authHeaderProvider.generateHeader()
    ```
=== "Swift"
    ```swift
    let params = startMockzilla(config: config, ..)

    // Generate a new header for each request.
    let header = params.authHeaderProvider.generateHeader()
    ```