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

@Serializable
data class MockDataEntryDto(
    val key: String,
    val name: String,
    val shouldFail: Boolean?,
    val delayMs: Int?,
    val headers: Map<String, String>?,
    val defaultBody: String?,
    val defaultStatus: @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?,
    val errorBody: String?,
    val errorStatus: @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?,
) {
    companion object {
        fun allNulls(key: String, name: String) = MockDataEntryDto(
            key = key, name = name,
            shouldFail = null,
            delayMs = null,
            headers = null,
            defaultBody = null,
            defaultStatus = null,
            errorBody = null,
            errorStatus = null,
        )
    }
}

/**
 * DTO for interaction with the web portal.
 *
 * @property name
 * @property key
 * @property shouldFail
 * @property delayMs
 * @property headers
 * @property defaultBody
 * @property errorBody
 * @property errorStatus
 * @property defaultStatus
 */
@Serializable
data class MockDataEntryUpdateRequestDto(
    val key: String,
    val name: String,
    val shouldFail: SetOrDont<Boolean?> = SetOrDont.DoNotSet,
    val delayMs: SetOrDont<Int?> = SetOrDont.DoNotSet,
    val headers: SetOrDont<Map<String, String>?> = SetOrDont.DoNotSet,
    val defaultBody: SetOrDont<String?> = SetOrDont.DoNotSet,
    val defaultStatus: SetOrDont<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?> = SetOrDont.DoNotSet,
    val errorBody: SetOrDont<String?> = SetOrDont.DoNotSet,
    val errorStatus: SetOrDont<@Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode?> = SetOrDont.DoNotSet,
) {
    companion object {
        fun allUnset(key: String, name: String) = MockDataEntryUpdateRequestDto(
            key = key,
            name = name,
            shouldFail = SetOrDont.DoNotSet,
            delayMs = SetOrDont.DoNotSet,
            headers = SetOrDont.DoNotSet,
            defaultBody = SetOrDont.DoNotSet,
            defaultStatus = SetOrDont.DoNotSet,
            errorBody = SetOrDont.DoNotSet,
            errorStatus = SetOrDont.DoNotSet,
        )

        fun allSet(mockDataEntry: MockDataEntryDto) = MockDataEntryUpdateRequestDto(
            key = mockDataEntry.key,
            name = mockDataEntry.name,
            shouldFail = SetOrDont.Set(mockDataEntry.shouldFail),
            delayMs = SetOrDont.Set(mockDataEntry.delayMs),
            headers =SetOrDont.Set(mockDataEntry.headers),
            defaultBody = SetOrDont.Set(mockDataEntry.defaultBody),
            defaultStatus =SetOrDont.Set(mockDataEntry.defaultStatus),
            errorBody =SetOrDont.Set(mockDataEntry.errorBody),
            errorStatus =SetOrDont.Set(mockDataEntry.errorStatus),

        )
    }
}

/**
 * @property entries
 */
@Serializable
data class MockDataResponseDto(
    val entries: List<MockDataEntryDto>
)
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
    serializer: KSerializer<T>
) : KSerializer<SetOrDont<T>> {
    private val surrogateSerializer = ServiceResultSurrogate.serializer(serializer)
    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): SetOrDont<T> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            ServiceResultSurrogate.Type.Set ->
                surrogate.value?.let {
                    SetOrDont.Set(surrogate.value)
                } ?: throw SerializationException("Missing data for set result")
            ServiceResultSurrogate.Type.UnSet ->
                SetOrDont.DoNotSet
        }
    }

    override fun serialize(encoder: Encoder, value: SetOrDont<T>) {
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
