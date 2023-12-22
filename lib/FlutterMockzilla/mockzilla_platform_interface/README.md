A common platform interface for the mockzilla plugin.

This interface is used by for platform-specific implementations of the mockzilla plugin, as well as said plugin itself to ensure that they are supporting the same interface.

## Usage

To implement a new platform-specific implementation of the mockzilla plugin, extend `MockzillaPlatform` and when you register your native implementation, be sure to call `MockzillaPlatform.instance = /* your implementation */;`.

There is a strong preference to keep the API defined in this package closely aligned with the native Mockzilla packages to make maintenance of all Mockzilla packages easier.