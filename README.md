# Mockzilla

🏗️ This repo is still being setup, and binaries still being setup on Maven Central. Watch this space!

## What is Mockzilla?

A solution for running and configuring a local HTTP server to mimic REST API endpoints used by your iOS, Android or [KMM](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html) application.

The source code is written in Kotlin but is fully compatible with a Swift environemnt too!

## Advantages

✅ Compile safe mock endpoint definitions.

✅ HTTP client agnostic.

✅ Works completely offline

✅ Entirely self-contained in your application's codebase.


## Quick Start 🚀

Please see our quick start guide and full [here](TODO: Add link to docs).

## Why's it useful? 🙌

Development servers go down, endpoints can be late being delivered or not exist at all! Mockzilla aims to easily provide a way of simulating your server from within your mobile application's codebase.

## Why not use a hosted solution? ☁️

Hosted mocking solutions can be powerful mocking tools in many cases. They have their downsides:

1. They can go down, Mockzilla works offline!
2. There's no compile-time checking
3. They require active maintenance with no compile-time safety if APIs change.

## What makes it compile safe? 🖥️

By defining your mocks using the same model classes as are used for deserialization, changing them, means changing the mocks or we get compiler errors! 😎

Example using [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization):

#### Existing networking models

```kotlin
@Serializable
data class HelloWorldResponse(val greeting: String)
```

#### Mocking code
```kotlin
val myEndpoint = EndpointConfiguration.Builder("hello-world")
    .setPatternMatcher { uri.endsWith("hello-world") }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                // Using existing models
                HelloWorldResponse(greeting = "Hello world!")
            )
        )
    }
```

## Important Note 🛑 

Mockzilla is designed as a development and test tool **only**. 

Mockzilla should **never be used in production**. Its traffic is unprotected and by nature of running a server on device, it can introduce security issues. 

**Do not ship it to production**.




