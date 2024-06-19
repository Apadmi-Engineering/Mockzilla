# Snapshot builds

!!! warning
    Snapshot builds are deployed directly from the `develop` branch of this repo. They are *not* stable and 
    should *not* be used for anything other than testing and trying out new features.

=== "Kotlin"
    To try out the latest development build add the following Maven url to your project.

    ```kotlin
    maven { setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    ```

    Update the Mockzilla version to the latest Snapshot version found [here](https://s01.oss.sonatype.org/content/repositories/snapshots/com/apadmi/mockzilla/).

=== "Flutter"
    If you wish to use the latest pre-release version of Mockzilla for Flutter, add the following dependency to your pubspec.yaml
    file:

    ```yaml
    mockzilla:
        git:
            ref: develop
            path: FlutterMockzilla/mockzilla
            url: https://github.com/Apadmi-Engineering/Mockzilla
    ```
