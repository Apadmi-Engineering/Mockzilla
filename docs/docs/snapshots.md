# Snapshot builds

!!! warning
    Snapshot builds are deployed directly from the `develop` branch of this repo. They are *not* stable and 
    should *not* be used for anything other than testing and trying out new features.

To try out the latest development build add the following Maven url to your project.

```kotlin
maven { setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
```

Update the Mockzilla version to the latest Snapshot version found [here](https://s01.oss.sonatype.org/content/repositories/snapshots/com/apadmi/mockzilla/).
