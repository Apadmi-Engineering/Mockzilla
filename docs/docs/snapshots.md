# Snapshot builds

!!! warning
    Snapshot builds are deployed directly from the `develop` branch of this repo. They are *not* stable and 
    should *not* be used for anything other than testing and trying out new features.

=== "Kotlin"
    To try out the latest development build add the following Maven url to your project.

    ```kotlin
    maven { setUrl("https://central.sonatype.com/repository/maven-snapshots/") }
    ```

    The latest snapshot can be found by examining the latest build [here](https://github.com/Apadmi-Engineering/Mockzilla/actions/workflows/action_deploy_binaries.yml).

=== "Swift"
    Snapshots are published to the SPM repos on the branch `deployment/snapshot`:

    - [SwiftMockzilla](https://github.com/Apadmi-Engineering/SwiftMockzilla/commits/deployment/snapshot/) 
    - [SwiftMockzillaMobileUi](https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi/commits/deployment/snapshot)

=== "Flutter"
Snapshots are published on [pub.dev](https://pub.dev/packages/mockzilla) as [pre-releases](https://dart.dev/tools/pub/publishing#publishing-prereleases).