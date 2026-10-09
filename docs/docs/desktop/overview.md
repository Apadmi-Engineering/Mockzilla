---
description: Download, connect to, and use the Mockzilla Desktop app to control your mocked endpoints live.
---

# Mockzilla Desktop (beta)

Mockzilla Desktop is a companion app that connects to Mockzilla on your device over your local network, letting you override responses, force failures, and adjust latency live - no code changes or rebuilds required.

<div class="mz-download-cta">
  <a href="{{ get_download_site_url() }}" class="md-button md-button--primary">Download Mockzilla Desktop</a>
  <p>macOS &middot;  Windows &middot; Linux</p>
</div>

!!! important
    You **must** be using the same Wi-Fi network on your device running Mockzilla and the device running
    the desktop app.

## Preparation

On iOS add the following to your Info.plist.
 
```xml
<key>NSBonjourServices</key>
<array>
  <string>_mockzilla._tcp</string>
</array>
```

This allows your device to be automatically detected on the network.

## Android 17 local network permission

Android 17 (API 37) blocks local network access by default for apps that **target API 37 or higher**. Mockzilla needs
this access to advertise your device to the desktop app, and for the desktop app to connect to it.

To allow it, add the permission to your `AndroidManifest.xml`. Since Mockzilla is for development only, we recommend 
adding it to your debug (or mock flavour) manifest, e.g. `src/debug/AndroidManifest.xml`, so it never reaches your 
production app.

```xml
<uses-permission android:name="android.permission.ACCESS_LOCAL_NETWORK" />
```

That's all you need to do. The first time your app starts, Mockzilla shows the "Nearby devices" permission prompt 
itself and starts advertising your device as soon as it's granted. It doesn't wait for your answer to start the server, 
so your app isn't held up.

If you deny the permission, or the line above is missing, your app keeps working and a warning is logged, but the 
desktop app won't be able to discover your device or connect to it. You can grant the permission later in your device's 
settings (Apps > your app > Permissions > Nearby devices), or with `adb`:

```shell
adb shell pm grant <your.application.id> android.permission.ACCESS_LOCAL_NETWORK
```

!!! warning
    If your app targets API 36 or lower, **don't** add the permission. Android grants local network access 
    automatically, and declaring the permission removes that and means it has to be granted by the user.

!!! note
    Android emulators are not affected. The desktop app connects to them using `adb` port forwarding, which reaches your 
    app over localhost rather than the network, so they work with or without the permission. Test on a physical device 
    to check your setup.

If you don't use the desktop app, you can disable network discovery and Mockzilla won't ask for the permission. 
See [Network discovery](../additional_config.md#network-discovery).

## Connecting a device

Your device should be automatically discovered by Mockzilla (you may need to restart the app on your device). Android emulators are also detected automatically, with no extra setup required.

![alt text](img/device_connection.png "Device connection")

!!! note
    If network discovery does not find your device you can manually type in the IP address of your device. (Don't forget the port!)
    On Android 17 (API 37) and above this also needs the [local network permission](#android-17-local-network-permission).

### Multiple devices

You can connect to more than one device at once - each connected device gets its own tab along the top of the window, so you can switch between them without losing your place. Use the "+" tab to connect another device, and close a tab's ✕  to disconnect.

![alt text](img/multiple_devices.png "Multiple devices")

## Editing mock data

Once connected, the app allows you to manipulate the latency of responses as well as their content in different ways.

### Apply a Preset

A preset is a predefined response defined in code to cater for a given scenario. 

<video autoplay loop muted playsinline controls>
  <source src="img/presets.mp4" type="video/mp4">
</video>

### Adjust Latency

Latency can be adjusted on the fly to help debug loading states and to simulate slow networks.

<video autoplay loop muted playsinline controls>
  <source src="img/latency.mp4" type="video/mp4">
</video>

### Force an endpoint to fail

Forcing an endpoint to fail causes the `errorHandler` in code to be called (defaulting to a 400 error).

<video autoplay loop muted playsinline controls>
  <source src="img/force-fail.mp4" type="video/mp4">
</video>

## Global Controls

To adjust latency on all endpoints or force all endpoints to fail, use the global controls.

<video autoplay loop muted playsinline controls>
  <source src="img/global-overrides.mp4" type="video/mp4">
</video>

## Monitor logs

The bottom panel is a live log of every request your device makes through Mockzilla, whether "Force failure" was enabled or not.

Each entry shows the request URL, status code, and response time - colour-coded so slow responses and errors stand out - along with a badge showing whether the response was the result of forced failure ("FORCED"). Selecting a log entry opens its full details: request and response headers, and body content.

![alt text](img/monitor_logs.png "Monitor Logs")

## Miscellaneous controls

Alongside the device/app info panel you'll find a few utility controls:

- **Refresh all** - re-fetches the latest state from the connected device.
- **Clear overrides** - same as "Reset all" in Global Controls.
- **Presentation mode** - scales the entire UI, handy when demoing on a projector or a shared screen.
- **Dark mode** - forces dark theme regardless of your OS setting.

![alt text](img/misc_controls.png "Miscellaneous Controls")

## Known limitations

- The desktop app enforces a minimum compatible Mockzilla server version (`2.0.0`) - if the app you're connecting to bundles an older `mockzilla` library, you'll see an "unsupported version" message instead of the dashboard. Update the `mockzilla` dependency in your app to resolve this.
- **Linux is not officially supported.** The desktop app is provided as-is on Linux - rendering issues may occur, and this platform doesn't receive the same testing as macOS and Windows.
