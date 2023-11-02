package com.example.mockzilla_android

import MockzillaFlutterApi
import MockzillaHostApi
import android.content.Context
import androidx.annotation.NonNull
import com.apadmi.mockzilla.MockzillaAndroid

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MockzillaAndroidPlugin */
class MockzillaAndroidPlugin: FlutterPlugin {

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
