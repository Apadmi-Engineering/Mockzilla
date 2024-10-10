## 1.0.0-dev.0

* Removes `delayVariance`, and `failureProbability` fields from endpoint configurations.
* Replaces `delayMean` with `mean` field for endpoint configurations.
* Removes Web Api fields (these are replaced with dashboard overrides).
* Adds `isNetworkDiscoveryEnabled` field to top level config.

## 0.2.0

* Adds default value of `{"Content-Type": "application/json"}` for parameter `headers` in 
`MockzillaHttpResponse`.
* Removes generated `MockzillaConfig.releaseModeConfig` from platform interface.
* Removes generated `MockzillaConfig.additionalLogWriters` from platform interface.

## 0.1.0

* Initial open-source release.
