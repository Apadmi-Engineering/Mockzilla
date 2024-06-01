package com.apadmi.mockzilla.lib.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
 */
@Serializable
data class MetaData(
    val appName: String,
    val appPackage: String,
    val operatingSystemVersion: String,
    val deviceModel: String,
    val appVersion: String,
    val runTarget: RunTarget,
    val mockzillaVersion: String
) {
    val isAndroid = runTarget in listOf(RunTarget.AndroidEmulator, RunTarget.AndroidDevice)

    fun toMap(): Map<String, String> {
        val encoded = json.encodeToString(this)
        return json.decodeFromString<Map<String, String>>(encoded)
    }

    companion object {
        const val maxFieldLength = 254

        @OptIn(ExperimentalSerializationApi::class)
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
    Jvm,
    ;
}
