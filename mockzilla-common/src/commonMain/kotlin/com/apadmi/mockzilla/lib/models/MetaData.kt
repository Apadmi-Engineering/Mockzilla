package com.apadmi.mockzilla.lib.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

/**
 * Device and application metadata collected when Mockzilla starts. Displayed in the management
 * dashboard to identify the connected device, and used in ZeroConf service records.
 *
 * Don't add non-optional fields to this type since that will break backward compatibility
 *
 * @property appName The name of the application.
 * @property appPackage The application package name or bundle identifier.
 * @property operatingSystemVersion The OS version string of the device.
 * @property deviceModel The device model identifier.
 * @property appVersion The application version string.
 * @property runTarget The platform the server is running on, or `null` if unknown.
 * @property mockzillaVersion The version of the Mockzilla library.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MetaData @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("appName")
    @SerialName("appName")
    val appName: String,

    @JsonNames("appPkg", "appPackage")
    @SerialName("appPkg")
    val appPackage: String,

    @JsonNames("osVer", "operatingSystemVersion")
    @SerialName("osVer")
    val operatingSystemVersion: String,

    @JsonNames("devModel", "deviceModel")
    @SerialName("devModel")
    val deviceModel: String,

    @JsonNames("appVer", "appVersion")
    @SerialName("appVer")
    val appVersion: String,

    @JsonNames("runTarg", "runTarget")
    @SerialName("runTarg")
    val runTarget: RunTarget? = null,

    @JsonNames("mzVer", "mockzillaVersion")
    @SerialName("mzVer")
    val mockzillaVersion: String
) {
    /**
     * `true` if the server is running on an Android device or emulator.
     */
    public val isAndroid: Boolean = runTarget in listOf(RunTarget.AndroidEmulator, RunTarget.AndroidDevice)

    /**
     * Serialises this metadata to a [Map] for embedding in ZeroConf TXT records.
     *
     * @return A map of field names to string values.
     */
    public fun toMap(): Map<String, String> {
        val encoded = json.encodeToString(this)
        return json.decodeFromString<Map<String, String>>(encoded)
    }

    public companion object {
        /**
         * Maximum length in characters for each metadata field. Fields collected from the platform
         * (device model, OS version, etc.) are truncated to this limit to comply with ZeroConf
         * DNS-SD payload constraints (RFC 1035).
         */
        public const val maxFieldLength: Int = 254
        private val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        /**
         * Deserialises a [MetaData] instance from a [Map] of field names to string values, as
         * produced by [MetaData.toMap]. Intended for reconstructing metadata received via ZeroConf
         * TXT records.
         *
         * @return The deserialised [MetaData].
         */
        public fun Map<String, String>.parseMetaData(): MetaData {
            val encoded = json.encodeToString(this)
            return json.decodeFromString<MetaData>(encoded)
        }
    }
}

/**
 * Identifies the platform on which the Mockzilla server is running. Reported in [MetaData] and
 * visible in the management dashboard.
 */
public enum class RunTarget {
    AndroidDevice,
    AndroidEmulator,
    IosDevice,
    IosSimulator,
    Js,
    Jvm,
    ;
}
