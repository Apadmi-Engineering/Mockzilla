# Contributing

## Repo Structure

```
|- fastlane.    : Build scripts and configuration
|- demo-android : A native Android app demo
|- demo-ios     : A native iOS app demo
|- demo-kmm     : A KMM project using Mockzilla from a shared module
|- lib          : The main library code
|--- SwiftMockzilla : The directory forming the SPM package

```

## Issues

All work is tracked using Github Issues. Before starting please ensure you create an Issue associated with your work if one does not exist.

[https://github.com/Apadmi-Engineering/Mockzilla/issues](https://github.com/Apadmi-Engineering/Mockzilla/issues)

## Write your code!

1. Checkout the tests. The library is setup with TDD in mind, we recommend writing your tests first!
2. Implement your feature/bugfix!

### Testing through the demo apps.

It's a good idea to sanity check your work by using the library through the demo apps.

1. From within the `lib` directory, call `./gradlew publishToMavenLocal` to make the package available locally.
2. You can now run the KMM or Android apps as normal using the local package. (See [here](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html) for an introduction to KMM development). They will automatically pickup the new package from your local maven repository.

Note: Currently there's no way to test the Swift package locally without it first being deployed.

## Spotless

We use Spotless to reformat and organise all of our library code. It runs automatically on compilation so please ensure you've compiled your code before submitting a pull request.

## Create a pull request

Creating a pull request will check everything compiles and runs all your tests. Once all the checks pass, we'll review your code and hopefully get it merged!

## Releases

This section is specifically for Apadmi maintainers creating releases.

1. Releases are all managed automatically. To release a new version, merge the open release pull request as described here: [https://github.com/google-github-actions/release-please-action](https://github.com/google-github-actions/release-please-action).
4. Deploy the release to maven central from [https://s01.oss.sonatype.org/#stagingRepositories](https://s01.oss.sonatype.org/#stagingRepositories).


