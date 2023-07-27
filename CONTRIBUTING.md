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

1. Create a release branch with the appropriate version. (e.g. `release/1.1.0`).
2. Bump the `lib` version number in the `version` file (in the repo root) to match the branch name.
3. Run the `Deploy All` action through Github.
4. Deploy the release to maven central from [https://s01.oss.sonatype.org/#stagingRepositories](https://s01.oss.sonatype.org/#stagingRepositories).
5. Close the release branch as per GitFlow standards. Tag the merge into `main` with the version name.
6. In the right hand panel in Github. Create a release with the newly created tag. Follow the instructions to create release notes and finalise the release.


