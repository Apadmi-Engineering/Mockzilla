# Mockzilla Desktop

### Building and Running

Use the following command to run the desktop app.

```bash
./gradlew mockzilla-management-ui:mockzilla-desktop:run
```

You'll need to run Mockzilla on a device in order to have test data to drive the desktop UI.


### Android (Development mode)

Alternatively you can run the desktop app on an Android tablet. This ships a running server within it for testing
so you don't need to run Mockzilla on a device alongside the desktop app.

Run the `mockzilla-management-ui:mockzilla-desktop:android` through Android Studio (ensuring you choose a tablet device/emulator). 

Note: This is just to make development of the desktop app easier, this is different to the embedded mobile UI which is in the `mockzilla-mobile-ui` module.