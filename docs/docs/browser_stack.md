---
description: Run Mockzilla on BrowserStack devices - iOS works out of the box; Android requires bypassing BrowserStack's local-traffic proxy in your HTTP client.
---

# BrowserStack

## iOS
Mockzilla should work out of the box on iOS with BrowserStack.

## Android

BrowserStack seems to proxy local traffic by default. In your client app you'll need to bypass any proxy.

### OkHttp Example

```kotlin
OkHttpClient.Builder()
    .proxy(Proxy.NO_PROXY)
```

### Ktor Example:

See demo app example [here](https://github.com/Apadmi-Engineering/Mockzilla/blob/develop/samples/demo-android/src/main/java/com/apadmi/mockzilla/demo/Repository.kt#L52).