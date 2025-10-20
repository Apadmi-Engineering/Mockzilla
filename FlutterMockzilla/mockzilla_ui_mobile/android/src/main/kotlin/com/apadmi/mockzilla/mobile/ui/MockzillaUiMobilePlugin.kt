package com.apadmi.mockzilla.mobile.ui

import android.app.Activity
import com.apadmi.mockzilla.mobile.ui.MockzillaUiMobileHostApi

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** MockzillaUiMobilePlugin */
class MockzillaUiMobilePlugin : FlutterPlugin, ActivityAware {
    private var activity: Activity? = null;

    lateinit var mockzillaApi: MockzillaUiMobileAndroid

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        mockzillaApi = MockzillaUiMobileAndroid() { activity }
        MockzillaUiMobileHostApi.setUp(flutterPluginBinding.binaryMessenger, mockzillaApi)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        activity = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}
