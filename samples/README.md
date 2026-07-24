# Samples

Sample apps demonstrating how to use Mockzilla, useful both for trying the library out and for sanity-checking changes while contributing.

## demo-kmm

A Kotlin Multiplatform shared module (`shared/`) consumed by both an Android app (`androidApp/`) and an iOS app (`iosApp/`), showing how to define mocks once and use them from both platforms.

- **Android**: open the repo root in Android Studio and run the `samples.demo-kmm.AndroidApp` target.
- **iOS**: open `samples/demo-kmm/iosApp` in Xcode and run as normal.

## demo-android

A native Android app showing Mockzilla usage without Kotlin Multiplatform.

- Open the repo root in Android Studio and run the `samples.demo-android` target.

## demo-ios

A native iOS app showing Mockzilla usage via [SwiftMockzilla](https://github.com/Apadmi-Engineering/SwiftMockzilla) (the SPM package), without Kotlin Multiplatform.

- Open `samples/demo-ios/demo-ios.xcodeproj` in Xcode and run as normal.

> Note: there's currently no way to test the SwiftMockzilla SPM package locally without it first being deployed — see [CONTRIBUTING.md](../CONTRIBUTING.md) for details.
