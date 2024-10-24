// Autogenerated from Pigeon (v22.6.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon
@file:Suppress("UNCHECKED_CAST", "ArrayInDataClass")


import android.util.Log
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMessageCodec
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private fun wrapResult(result: Any?): List<Any?> {
  return listOf(result)
}

private fun wrapError(exception: Throwable): List<Any?> {
  return if (exception is FlutterError) {
    listOf(
      exception.code,
      exception.message,
      exception.details
    )
  } else {
    listOf(
      exception.javaClass.simpleName,
      exception.toString(),
      "Cause: " + exception.cause + ", Stacktrace: " + Log.getStackTraceString(exception)
    )
  }
}

private fun createConnectionError(channelName: String): FlutterError {
  return FlutterError("channel-error",  "Unable to establish connection on channel: '$channelName'.", "")}

/**
 * Error class for passing custom error details to Flutter via a thrown PlatformException.
 * @property code The error code.
 * @property message The error message.
 * @property details The error details. Must be a datatype supported by the api codec.
 */
class FlutterError (
  val code: String,
  override val message: String? = null,
  val details: Any? = null
) : Throwable()

enum class BridgeHttpMethod(val raw: Int) {
  GET(0),
  HEAD(1),
  POST(2),
  PUT(3),
  DELETE(4),
  OPTIONS(5),
  PATCH(6);

  companion object {
    fun ofRaw(raw: Int): BridgeHttpMethod? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class BridgeLogLevel(val raw: Int) {
  DEBUG(0),
  ERROR(1),
  INFO(2),
  VERBOSE(3),
  WARN(4),
  ASSERTION(5);

  companion object {
    fun ofRaw(raw: Int): BridgeLogLevel? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeMockzillaHttpRequest (
  val uri: String,
  val headers: Map<String, String>,
  val body: String,
  val method: BridgeHttpMethod
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeMockzillaHttpRequest {
      val uri = pigeonVar_list[0] as String
      val headers = pigeonVar_list[1] as Map<String, String>
      val body = pigeonVar_list[2] as String
      val method = pigeonVar_list[3] as BridgeHttpMethod
      return BridgeMockzillaHttpRequest(uri, headers, body, method)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      uri,
      headers,
      body,
      method,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeMockzillaHttpResponse (
  val statusCode: Long,
  val headers: Map<String, String>,
  val body: String
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeMockzillaHttpResponse {
      val statusCode = pigeonVar_list[0] as Long
      val headers = pigeonVar_list[1] as Map<String, String>
      val body = pigeonVar_list[2] as String
      return BridgeMockzillaHttpResponse(statusCode, headers, body)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      statusCode,
      headers,
      body,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeDashboardOverridePreset (
  val name: String,
  val description: String? = null,
  val response: BridgeMockzillaHttpResponse
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeDashboardOverridePreset {
      val name = pigeonVar_list[0] as String
      val description = pigeonVar_list[1] as String?
      val response = pigeonVar_list[2] as BridgeMockzillaHttpResponse
      return BridgeDashboardOverridePreset(name, description, response)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      name,
      description,
      response,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeDashboardOptionsConfig (
  val successPresets: List<BridgeDashboardOverridePreset>,
  val errorPresets: List<BridgeDashboardOverridePreset>
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeDashboardOptionsConfig {
      val successPresets = pigeonVar_list[0] as List<BridgeDashboardOverridePreset>
      val errorPresets = pigeonVar_list[1] as List<BridgeDashboardOverridePreset>
      return BridgeDashboardOptionsConfig(successPresets, errorPresets)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      successPresets,
      errorPresets,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeEndpointConfig (
  val name: String,
  val key: String,
  val shouldFail: Boolean,
  val delayMs: Long,
  val versionCode: Long,
  val config: BridgeDashboardOptionsConfig
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeEndpointConfig {
      val name = pigeonVar_list[0] as String
      val key = pigeonVar_list[1] as String
      val shouldFail = pigeonVar_list[2] as Boolean
      val delayMs = pigeonVar_list[3] as Long
      val versionCode = pigeonVar_list[4] as Long
      val config = pigeonVar_list[5] as BridgeDashboardOptionsConfig
      return BridgeEndpointConfig(name, key, shouldFail, delayMs, versionCode, config)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      name,
      key,
      shouldFail,
      delayMs,
      versionCode,
      config,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeReleaseModeConfig (
  val rateLimit: Long,
  val rateLimitRefillPeriodMillis: Long,
  val tokenLifeSpanMillis: Long
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeReleaseModeConfig {
      val rateLimit = pigeonVar_list[0] as Long
      val rateLimitRefillPeriodMillis = pigeonVar_list[1] as Long
      val tokenLifeSpanMillis = pigeonVar_list[2] as Long
      return BridgeReleaseModeConfig(rateLimit, rateLimitRefillPeriodMillis, tokenLifeSpanMillis)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      rateLimit,
      rateLimitRefillPeriodMillis,
      tokenLifeSpanMillis,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeMockzillaConfig (
  val port: Long,
  val endpoints: List<BridgeEndpointConfig>,
  val isRelease: Boolean,
  val localHostOnly: Boolean,
  val logLevel: BridgeLogLevel,
  val releaseModeConfig: BridgeReleaseModeConfig,
  val isNetworkDiscoveryEnabled: Boolean
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeMockzillaConfig {
      val port = pigeonVar_list[0] as Long
      val endpoints = pigeonVar_list[1] as List<BridgeEndpointConfig>
      val isRelease = pigeonVar_list[2] as Boolean
      val localHostOnly = pigeonVar_list[3] as Boolean
      val logLevel = pigeonVar_list[4] as BridgeLogLevel
      val releaseModeConfig = pigeonVar_list[5] as BridgeReleaseModeConfig
      val isNetworkDiscoveryEnabled = pigeonVar_list[6] as Boolean
      return BridgeMockzillaConfig(port, endpoints, isRelease, localHostOnly, logLevel, releaseModeConfig, isNetworkDiscoveryEnabled)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      port,
      endpoints,
      isRelease,
      localHostOnly,
      logLevel,
      releaseModeConfig,
      isNetworkDiscoveryEnabled,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeMockzillaRuntimeParams (
  val config: BridgeMockzillaConfig,
  val mockBaseUrl: String,
  val apiBaseUrl: String,
  val port: Long
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeMockzillaRuntimeParams {
      val config = pigeonVar_list[0] as BridgeMockzillaConfig
      val mockBaseUrl = pigeonVar_list[1] as String
      val apiBaseUrl = pigeonVar_list[2] as String
      val port = pigeonVar_list[3] as Long
      return BridgeMockzillaRuntimeParams(config, mockBaseUrl, apiBaseUrl, port)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      config,
      mockBaseUrl,
      apiBaseUrl,
      port,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class BridgeAuthHeader (
  val key: String,
  val value: String
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): BridgeAuthHeader {
      val key = pigeonVar_list[0] as String
      val value = pigeonVar_list[1] as String
      return BridgeAuthHeader(key, value)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      key,
      value,
    )
  }
}
private open class MessagesPigeonCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      129.toByte() -> {
        return (readValue(buffer) as Long?)?.let {
          BridgeHttpMethod.ofRaw(it.toInt())
        }
      }
      130.toByte() -> {
        return (readValue(buffer) as Long?)?.let {
          BridgeLogLevel.ofRaw(it.toInt())
        }
      }
      131.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeMockzillaHttpRequest.fromList(it)
        }
      }
      132.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeMockzillaHttpResponse.fromList(it)
        }
      }
      133.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeDashboardOverridePreset.fromList(it)
        }
      }
      134.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeDashboardOptionsConfig.fromList(it)
        }
      }
      135.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeEndpointConfig.fromList(it)
        }
      }
      136.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeReleaseModeConfig.fromList(it)
        }
      }
      137.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeMockzillaConfig.fromList(it)
        }
      }
      138.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeMockzillaRuntimeParams.fromList(it)
        }
      }
      139.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          BridgeAuthHeader.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is BridgeHttpMethod -> {
        stream.write(129)
        writeValue(stream, value.raw)
      }
      is BridgeLogLevel -> {
        stream.write(130)
        writeValue(stream, value.raw)
      }
      is BridgeMockzillaHttpRequest -> {
        stream.write(131)
        writeValue(stream, value.toList())
      }
      is BridgeMockzillaHttpResponse -> {
        stream.write(132)
        writeValue(stream, value.toList())
      }
      is BridgeDashboardOverridePreset -> {
        stream.write(133)
        writeValue(stream, value.toList())
      }
      is BridgeDashboardOptionsConfig -> {
        stream.write(134)
        writeValue(stream, value.toList())
      }
      is BridgeEndpointConfig -> {
        stream.write(135)
        writeValue(stream, value.toList())
      }
      is BridgeReleaseModeConfig -> {
        stream.write(136)
        writeValue(stream, value.toList())
      }
      is BridgeMockzillaConfig -> {
        stream.write(137)
        writeValue(stream, value.toList())
      }
      is BridgeMockzillaRuntimeParams -> {
        stream.write(138)
        writeValue(stream, value.toList())
      }
      is BridgeAuthHeader -> {
        stream.write(139)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface MockzillaHostApi {
  fun startServer(config: BridgeMockzillaConfig): BridgeMockzillaRuntimeParams
  fun stopServer()

  companion object {
    /** The codec used by MockzillaHostApi. */
    val codec: MessageCodec<Any?> by lazy {
      MessagesPigeonCodec()
    }
    /** Sets up an instance of `MockzillaHostApi` to handle messages through the `binaryMessenger`. */
    @JvmOverloads
    fun setUp(binaryMessenger: BinaryMessenger, api: MockzillaHostApi?, messageChannelSuffix: String = "") {
      val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.mockzilla_android.MockzillaHostApi.startServer$separatedMessageChannelSuffix", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val configArg = args[0] as BridgeMockzillaConfig
            val wrapped: List<Any?> = try {
              listOf(api.startServer(configArg))
            } catch (exception: Throwable) {
              wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.mockzilla_android.MockzillaHostApi.stopServer$separatedMessageChannelSuffix", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            val wrapped: List<Any?> = try {
              api.stopServer()
              listOf(null)
            } catch (exception: Throwable) {
              wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
    }
  }
}
/** Generated class from Pigeon that represents Flutter messages that can be called from Kotlin. */
class MockzillaFlutterApi(private val binaryMessenger: BinaryMessenger, private val messageChannelSuffix: String = "") {
  companion object {
    /** The codec used by MockzillaFlutterApi. */
    val codec: MessageCodec<Any?> by lazy {
      MessagesPigeonCodec()
    }
  }
  fun endpointMatcher(requestArg: BridgeMockzillaHttpRequest, keyArg: String, callback: (Result<Boolean>) -> Unit)
{
    val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName = "dev.flutter.pigeon.mockzilla_android.MockzillaFlutterApi.endpointMatcher$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(requestArg, keyArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else if (it[0] == null) {
          callback(Result.failure(FlutterError("null-error", "Flutter api returned null value for non-null return value.", "")))
        } else {
          val output = it[0] as Boolean
          callback(Result.success(output))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun defaultHandler(requestArg: BridgeMockzillaHttpRequest, keyArg: String, callback: (Result<BridgeMockzillaHttpResponse>) -> Unit)
{
    val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName = "dev.flutter.pigeon.mockzilla_android.MockzillaFlutterApi.defaultHandler$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(requestArg, keyArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else if (it[0] == null) {
          callback(Result.failure(FlutterError("null-error", "Flutter api returned null value for non-null return value.", "")))
        } else {
          val output = it[0] as BridgeMockzillaHttpResponse
          callback(Result.success(output))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun errorHandler(requestArg: BridgeMockzillaHttpRequest, keyArg: String, callback: (Result<BridgeMockzillaHttpResponse>) -> Unit)
{
    val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName = "dev.flutter.pigeon.mockzilla_android.MockzillaFlutterApi.errorHandler$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(requestArg, keyArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else if (it[0] == null) {
          callback(Result.failure(FlutterError("null-error", "Flutter api returned null value for non-null return value.", "")))
        } else {
          val output = it[0] as BridgeMockzillaHttpResponse
          callback(Result.success(output))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun generateAuthHeader(callback: (Result<BridgeAuthHeader>) -> Unit)
{
    val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName = "dev.flutter.pigeon.mockzilla_android.MockzillaFlutterApi.generateAuthHeader$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(null) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else if (it[0] == null) {
          callback(Result.failure(FlutterError("null-error", "Flutter api returned null value for non-null return value.", "")))
        } else {
          val output = it[0] as BridgeAuthHeader
          callback(Result.success(output))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun log(logLevelArg: BridgeLogLevel, messageArg: String, tagArg: String, exceptionArg: String?, callback: (Result<Unit>) -> Unit)
{
    val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName = "dev.flutter.pigeon.mockzilla_android.MockzillaFlutterApi.log$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(logLevelArg, messageArg, tagArg, exceptionArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
}
