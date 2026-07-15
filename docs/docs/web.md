---
description: Use Mockzilla in a browser app - Kotlin/JS, Kotlin Multiplatform web, or Flutter Web - backed by Mock Service Worker (MSW).
---

# Web / JavaScript

!!! warning
    Web support is newer and less battle-tested than the other platforms - expect some rough edges, and
    treat the API as subject to change.

    **It is *not* designed to be consumed from a JavaScript web application, only from Kotlin & Flutter apps running
    against the web target.**

## How it works

Unlike the other platforms, Mockzilla on web isn't a real HTTP server - a browser can't bind a listening
port. Instead it's built on [Mock Service Worker (MSW)](https://mswjs.io/), which intercepts `fetch`/`XHR`
calls made by your page. This has a couple of practical implications:

- The `localhost:8080` base URL is kept only for consistency with the other platforms - nothing actually
  binds to port 8080. It's just the URL prefix MSW matches requests against.
- Requests that don't match any configured endpoint are passed straight through to the real network
  (`onUnhandledRequest` is set to bypass, not to warn or error).

## Installation

Installation should be through the Kotlin or Flutter mechanisms as described in the [Quick Start](quick-start.md) guide.

## Required: Add the mock service worker script

This step is easy to miss and isn't handled automatically - without it, starting Mockzilla will fail.
MSW needs its worker script, `mockServiceWorker.js`, served from your site's root.

1. Get a copy of the script. Either use the one bundled in this repo at `js-scripts/mockServiceWorker.js`
   (pinned to MSW `2.11.6`, matching the version Mockzilla uses internally), or generate one yourself with
   the standard MSW CLI: `npx msw init <your-web-output-dir>`.
2. Place it so it's served from your site root:

    === "Kotlin/JS or KMP web"
        Copy it to `src/webMain/resources/mockServiceWorker.js` (or `src/jsMain/resources/` if you don't
        have a `webMain` source set). Kotlin's default resource processing copies everything under that
        folder straight into your build output, next to `index.html`.
    === "Flutter"
        Copy it to `web/mockServiceWorker.js` in your Flutter project. Flutter serves everything under
        `web/` at the site root, so it'll be available at `/mockServiceWorker.js`.

## Differences from mobile/native

A few things that work on Android/iOS don't apply (or don't yet exist) on web - to save you filing a bug
for expected behaviour:

- No network device auto-discovery in the [desktop app](desktop/overview.md) - a browser tab can't be
  discovered the way a phone on the same Wi-Fi network can. The embedded mobile UI (which runs in the same
  page as your app) is the way to control mocks on web.
- The connected-device metadata shown in the dashboard won't include an app icon.
- [Release mode](additional_config.md#release-mode)'s auth-header generation is a no-op stub on web.

## Distribution

There is currently no npm package for consuming Mockzilla from a plain JavaScript/TypeScript project outside of Kotlin or Flutter.
