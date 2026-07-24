# Security Policy

## Supported Versions

Only the latest published release of each Mockzilla package (Kotlin/KMM, Flutter, Swift) receives security fixes. Please make sure you're on the latest version before reporting an issue.

## Scope

Mockzilla is a **development and test tool only** — it runs an HTTP server embedded in your app to serve mock responses, and is explicitly **not designed to be shipped to production** (see the [README](README.md)). Because of this, some behaviours that would be vulnerabilities in a production server (e.g. unauthenticated local network access) are inherent to how the tool is meant to be used in a dev/test build. If you find something that could compromise a user's device or data beyond that expected scope, please report it.

## Reporting a Vulnerability

Please **do not** open a public GitHub issue for security vulnerabilities.

Instead, use GitHub's private vulnerability reporting: go to the **Security** tab of this repository and select **Report a vulnerability**. This lets us discuss and fix the issue before it's publicly disclosed.

We aim to acknowledge reports within 5 business days and will keep you updated as we investigate and address the issue.
