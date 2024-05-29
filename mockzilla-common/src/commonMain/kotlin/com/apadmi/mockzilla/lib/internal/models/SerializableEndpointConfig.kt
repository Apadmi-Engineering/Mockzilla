package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import io.ktor.http.*
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @property key
 * @property name
 * @property shouldFail
 * @property delayMs
 * @property defaultHeaders
 * @property defaultBody
 * @property defaultStatus
 * @property errorBody
 * @property errorStatus
 * @property errorHeaders
 * @property versionCode
 */
@Serializable
data class SerializableEndpointConfig(
    val key: EndpointConfiguration.Key,
    val name: String,
    val versionCode: Int,
    val shouldFail: Boolean?,
    val delayMs: Int?,
    val defaultHeaders: Map<String, String>?,
    val defaultBody: String?,
    val defaultStatus: @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?,
    val errorHeaders: Map<String, String>?,
    val errorBody: String?,
    val errorStatus: @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?,
) {
    companion object {
        fun allNulls(
            key: EndpointConfiguration.Key,
            name: String,
            versionCode: Int
        ) = SerializableEndpointConfig(
            key = key,
            name = name,
            versionCode = versionCode,
            shouldFail = null,
            delayMs = null,
            defaultHeaders = null,
            defaultBody = null,
            defaultStatus = null,
            errorBody = null,
            errorHeaders = null,
            errorStatus = null,
        )
        fun allNulls(
            key: String,
            name: String,
            versionCode: Int
        ) = allNulls(EndpointConfiguration.Key(key), name, versionCode)
    }
}

/**
 * DTO for interaction with the web portal.
 *
 * @property key
 * @property shouldFail
 * @property delayMs
 * @property headers
 * @property defaultBody
 * @property errorBody
 * @property errorStatus
 * @property defaultStatus
 * @property errorHeaders
 */
@Suppress("TYPE_ALIAS")
@Serializable
data class SerializableEndpointPatchItemDto(
    val key: EndpointConfiguration.Key,
    val shouldFail: SetOrDont<Boolean?> = SetOrDont.DoNotSet,
    val delayMs: SetOrDont<Int?> = SetOrDont.DoNotSet,
    val headers: SetOrDont<Map<String, String>?> = SetOrDont.DoNotSet,
    val defaultBody: SetOrDont<String?> = SetOrDont.DoNotSet,
    val defaultStatus: SetOrDont<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?> = SetOrDont.DoNotSet,
    val errorBody: SetOrDont<String?> = SetOrDont.DoNotSet,
    val errorHeaders: SetOrDont<Map<String, String>?> = SetOrDont.DoNotSet,
    val errorStatus: SetOrDont<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?> = SetOrDont.DoNotSet,
) {
    companion object {
        fun allUnset(key: String) = allUnset(EndpointConfiguration.Key(key))

        fun allUnset(key: EndpointConfiguration.Key) = SerializableEndpointPatchItemDto(
            key = key,
            shouldFail = SetOrDont.DoNotSet,
            delayMs = SetOrDont.DoNotSet,
            headers = SetOrDont.DoNotSet,
            defaultBody = SetOrDont.DoNotSet,
            defaultStatus = SetOrDont.DoNotSet,
            errorBody = SetOrDont.DoNotSet,
            errorStatus = SetOrDont.DoNotSet,
        )

        fun allSet(config: SerializableEndpointConfig) = SerializableEndpointPatchItemDto(
            key = config.key,
            shouldFail = SetOrDont.Set(config.shouldFail),
            delayMs = SetOrDont.Set(config.delayMs),
            headers = SetOrDont.Set(config.defaultHeaders),
            defaultBody = SetOrDont.Set(config.defaultBody),
            defaultStatus = SetOrDont.Set(config.defaultStatus),
            errorBody = SetOrDont.Set(config.errorBody),
            errorStatus = SetOrDont.Set(config.errorStatus),

        )
    }
}

/**
 * @property entries
 */
@Serializable
data class MockDataResponseDto(
    val entries: List<SerializableEndpointConfig>
)

/**
 * @property entries
 */
@Serializable
data class SerializableEndpointConfigPatchRequestDto(
    val entries: List<SerializableEndpointPatchItemDto>
) {
    constructor(entry: SerializableEndpointPatchItemDto) : this(listOf(entry))
}

@Serializable(with = ServiceResultSerializer::class)
sealed class SetOrDont<out T> {
    @Serializable
    @SerialName("DoNotSet")
    data object DoNotSet : SetOrDont<Nothing>()

    /**
     * @property value
     */
    @Serializable
    @SerialName("Set")
    data class Set<T>(val value: T) : SetOrDont<T>()
}

class ServiceResultSerializer<T : Any>(
    serializer: KSerializer<T?>
) : KSerializer<SetOrDont<T?>> {
    private val surrogateSerializer = ServiceResultSurrogate.serializer(serializer)
    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): SetOrDont<T?> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            ServiceResultSurrogate.Type.Set -> SetOrDont.Set(surrogate.value)
            ServiceResultSurrogate.Type.UnSet ->
                SetOrDont.DoNotSet
        }
    }

    override fun serialize(encoder: Encoder, value: SetOrDont<T?>) {
        val surrogate = when (value) {
            is SetOrDont.Set -> ServiceResultSurrogate(ServiceResultSurrogate.Type.Set, value.value)
            SetOrDont.DoNotSet -> ServiceResultSurrogate(ServiceResultSurrogate.Type.UnSet, null)
        }
        surrogateSerializer.serialize(encoder, surrogate)
    }
    /**
     * @property type
     */
    @Suppress("KDOC_NO_CONSTRUCTOR_PROPERTY_WITH_COMMENT")
    @Serializable
    @SerialName("ServiceResult")
    data class ServiceResultSurrogate<out T : Any>(
        val type: Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // */ for "UnSet" results.
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val value: T? = null,
    ) {
        enum class Type {
            Set, UnSet
        }
    }
}
