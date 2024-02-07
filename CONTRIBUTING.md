# Contributing

## Repo Structure

```
|- fastlane.       : Build scripts and configuration
|- samples         : Sample apps demonstrating usage of Mockzilla
|- mockzilla       : The directory forming the core Mockzilla KMM module
|- mockzilla-common: Common utilities used by multiple other modules
|- mockzilla-management: A kotlin wrapper around the http apis defined in `mockzilla`
|- mockzilla-management-ui: Compose multiplatform app used to maniplulate mockzilla at runtime
|- SwiftMockzilla  : The directory forming the SPM package
|- FlutterMockzilla: The directory containing the federated Flutter plugin

```

## Issues

All work is tracked using Github Issues. Before starting please ensure you create an Issue associated with your work if one does not exist.

[https://github.com/Apadmi-Engineering/Mockzilla/issues](https://github.com/Apadmi-Engineering/Mockzilla/issues)

## Prerequisites

You might have all these installed already, but just in case:
* Android 32 SDK 
* Android Studio 
* Gradle version 7 (higher use JVM 21 which causes build errors)
* openjdk 17

## Getting Started

1. Fork the repo. Make sure to untick the "Copy the master branch only" option to get all branches.
2. Clone your local copy.
3. Checkout the develop branch `git checkout develop`.
4. Open up the project in the `lib` directory in Android Studio. 
5. Regarding your `local.properties` - if you have one, verify in your `local.properties` sdk location contains `sdk.dir=/Users/[Your pc username]/Library/Android/sdk`. If you get errors, double check you've got Android SDK 32.
If not, create a `local.properties` file in your `lib` directory containing the above path.
6. Go into the project level `build.gradle.kts` and sync the project.
7. Finally, run a test to make sure everything built correctly!


## Running the app
1. Go into the `lib` directory in Android Studio.
2. Run `./gradlew :mockzilla-management-ui:desktop:run` 

!!! note 
    For front end development, you'll need to be connected to a running Mockzilla instance. You can use an app you already have or run one of the demo apps in the top level of the repo :) 
## Write your code!

1. Checkout the tests. The library is setup with TDD in mind, we recommend writing your tests first! 
You can make these in the `Mockzilla/lib/mockzilla/commonTest/` mockzilla package. 
2. Implement your feature/bugfix!
3. Write the tests and make the tests pass.

### Testing through the demo apps.

It's a good idea to sanity check your work by using the library through the demo apps.

Open the root of this repo in Android Studio and run the `samples.demo-kmm.AndroidApp` or `samples.demo-android` targets. The KMM iOS app can also be run through XCode as normal.
Note: Currently there's no way to test the Swift package locally without it first being deployed.

## Spotless

We use Spotless to reformat and organise all of our library code. It runs automatically on compilation so please ensure you've compiled your code before submitting a pull request.

To run spotless manually, do `./gradlew spotlessApply`.


## Create a pull request

Creating a pull request will check everything compiles and runs all your tests. 

A good pull request will have an appropriate title and summary outlining what you've done.

Once all the checks pass, we'll review your code and hopefully get it merged!

## Releases

This section is specifically for Apadmi maintainers creating releases.

1. Releases are all managed automatically. To release a new version, merge the open release pull request as described here: [https://github.com/google-github-actions/release-please-action](https://github.com/google-github-actions/release-please-action).
2. Deploy the release to maven central from [https://s01.oss.sonatype.org/#stagingRepositories](https://s01.oss.sonatype.org/#stagingRepositories).


