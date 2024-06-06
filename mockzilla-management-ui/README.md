# Mockzilla Desktop



### Building and Running

Use the following command to run the desktop app.

```bash
./gradlew mockzilla-management-ui:desktop:run
```

You'll need to run Mockzilla on a device in order to have test data to drive the desktop UI.


### Android (Development mode)

Alternatively you can run the desktop app on an Android tablet. This ships a running server within it for testing
so you don't need to run Mockzilla on a device alongside the desktop app.

Run the `mockzilla-management-ui:android` through Android Studio (ensuring you choose a tablet device/emulator). 