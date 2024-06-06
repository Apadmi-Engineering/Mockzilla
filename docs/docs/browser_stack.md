# Browserstack

## iOS
Mockzilla should work out of the box on iOS with Browserstack.

## Android

Browserstack seems to proxy local traffic by default. In your client app you'll need to bypass any proxy.

### OkHttp Example

```kotlin
OkHttpClient.Builder()
    .proxy(Proxy.NO_PROXY)
    .protocols(listOf(Protocol.HTTP_1_1)).build()
```

### Ktor Example:

See demo app example [here](https://github.com/Apadmi-Engineering/Mockzilla/blob/develop/samples/demo-android/src/main/java/com/apadmi/mockzilla/demo/Repository.kt).