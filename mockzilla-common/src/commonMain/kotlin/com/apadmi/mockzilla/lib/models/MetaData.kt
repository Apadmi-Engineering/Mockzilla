package com.apadmi.mockzilla.lib.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

/**
 * @property appName
 * @property appPackage
 * @property operatingSystemVersion
 * @property deviceModel
 * @property appVersion
 * @property runTarget
 * @property mockzillaVersion
 *
 * Don't add non optional fields to this type since that will break backward compatibility
 *
 * Short alternative JsonNames used for encoding/decoding when ZeroConf is used to reduce payload size
 *
 */
@Serializable
data class MetaData @OptIn(ExperimentalSerializationApi::class) constructor(
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
    val isAndroid = runTarget in listOf(RunTarget.AndroidEmulator, RunTarget.AndroidDevice)

    fun toMap(): Map<String, String> {
        val encoded = json.encodeToString(this)
        return json.decodeFromString<Map<String, String>>(encoded)
    }

    companion object {
        const val maxFieldLength = 254

        private val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        fun Map<String, String>.parseMetaData(): MetaData {
            val encoded = json.encodeToString(this)
            return json.decodeFromString<MetaData>(encoded)
        }
    }
}

enum class RunTarget {
    AndroidDevice,
    AndroidEmulator,
    IosDevice,
    IosSimulator,
    Js,
    Jvm,
    ;
}
