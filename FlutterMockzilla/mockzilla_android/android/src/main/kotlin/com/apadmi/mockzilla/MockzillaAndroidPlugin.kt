package com.apadmi.mockzilla

import MockzillaFlutterApi
import MockzillaHostApi

import io.flutter.embedding.engine.plugins.FlutterPlugin

/** MockzillaAndroidPlugin */
class MockzillaAndroidPlugin : FlutterPlugin {
    lateinit var mockzillaApi: MockzillaAndroid

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val flutterApi = MockzillaFlutterApi(flutterPluginBinding.binaryMessenger)
        mockzillaApi = MockzillaAndroid(flutterApi, flutterPluginBinding.applicationContext)
        MockzillaHostApi.setUp(flutterPluginBinding.binaryMessenger, mockzillaApi)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mockzillaApi.stopServer()
    }
}
