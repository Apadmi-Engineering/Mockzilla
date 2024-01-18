package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
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
 * DTO for interaction with the web portal.
 *
 * @property name
 * @property key
 * @property shouldFail
 * @property delayMs
 * @property delayVariance
 * @property headers
 * @property defaultBody
 * @property errorBody
 * @property errorStatus
 * @property defaultStatus
 */
@Serializable
data class MockDataEntryDto(
    val key: String,
    val name: String,
    val shouldFail: SetOrDoNotSetValue<Boolean>,
    val delayMs: SetOrDoNotSetValue<Int>,
    val headers: SetOrDoNotSetValue<Map<String, String>>,
    val defaultBody: SetOrDoNotSetValue<String>,
    val defaultStatus: SetOrDoNotSetValue<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode>,
    val errorBody: SetOrDoNotSetValue<String>,
    val errorStatus: SetOrDoNotSetValue<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode>,
) {

    companion object {
        fun allUnset(key: String, name: String) = MockDataEntryDto(
            key = key,
            name = name,
            shouldFail = SetOrDoNotSetValue.DoNotSet,
            delayMs = SetOrDoNotSetValue.DoNotSet,
            headers = SetOrDoNotSetValue.DoNotSet,
            defaultBody = SetOrDoNotSetValue.DoNotSet,
            defaultStatus = SetOrDoNotSetValue.DoNotSet,
            errorBody = SetOrDoNotSetValue.DoNotSet,
            errorStatus = SetOrDoNotSetValue.DoNotSet,
        )
    }
}

@Serializable
data class MockDataResponseDto(
    val entries: List<MockDataEntryDto>
)
@Serializable(with = ServiceResultSerializer::class)
sealed class SetOrDoNotSetValue<out T> {
    @Serializable
    @SerialName("DoNotSet")
    data object DoNotSet : SetOrDoNotSetValue<Nothing>()

    /**
     * @property value
     */
    @Serializable
    @SerialName("Set")
    data class Set<T>(val value: T) : SetOrDoNotSetValue<T>()

}
    fun <T> SetOrDoNotSetValue<T>?.valueOrDefault(default:  T): T = when(this) {
        is SetOrDoNotSetValue.Set -> value
        null,
        SetOrDoNotSetValue.DoNotSet -> default
    }


class ServiceResultSerializer<T : Any>(
    tSerializer: KSerializer<T>
) : KSerializer<SetOrDoNotSetValue<T>> {
    @Serializable
    @SerialName("ServiceResult")
    data class ServiceResultSurrogate<out T : Any>(
        val type: Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // for "UnSet" results.
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val value: T? = null,
    ) {
        enum class Type { Set, UnSet }
    }

    private val surrogateSerializer = ServiceResultSurrogate.serializer(tSerializer)

    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): SetOrDoNotSetValue<T> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            ServiceResultSurrogate.Type.Set ->
                if (surrogate.value != null)
                    SetOrDoNotSetValue.Set(surrogate.value)
                else
                    throw SerializationException("Missing data for set result")
            ServiceResultSurrogate.Type.UnSet ->
                SetOrDoNotSetValue.DoNotSet
        }
    }

    override fun serialize(encoder: Encoder, value: SetOrDoNotSetValue<T>) {
        val surrogate = when (value) {
            is SetOrDoNotSetValue.Set -> ServiceResultSurrogate(ServiceResultSurrogate.Type.Set, value.value)
            SetOrDoNotSetValue.DoNotSet -> ServiceResultSurrogate(ServiceResultSurrogate.Type.UnSet, null)
        }
        surrogateSerializer.serialize(encoder, surrogate)
    }
}
