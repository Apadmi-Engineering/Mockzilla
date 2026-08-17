@file:OptIn(ExperimentalSerializationApi::class)

package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @property key
 * @property name
 * @property shouldFail
 * @property delayMs
 * @property versionCode
 * @property appliedPresetOverride
 */
@InternalMockzillaApi
@Serializable
public data class SerializableEndpointConfig(
    val key: EndpointConfiguration.Key,
    val name: String,
    val versionCode: Int,
    val shouldFail: Boolean?,
    val delayMs: Int?,
    val appliedPresetOverride: DashboardOverridePreset?
) {
    public companion object {
        public fun allNulls(
            key: EndpointConfiguration.Key,
            name: String,
            versionCode: Int
        ): SerializableEndpointConfig = SerializableEndpointConfig(
            key = key,
            name = name,
            versionCode = versionCode,
            shouldFail = null,
            delayMs = null,
            appliedPresetOverride = null
        )
        public fun allNulls(
            key: String,
            name: String,
            versionCode: Int
        ): SerializableEndpointConfig = allNulls(EndpointConfiguration.Key(key), name, versionCode)
    }
}

/**
 * DTO for interaction with the management apis.
 *
 * @property key
 * @property shouldFail
 * @property delayMs
 * @property appliedPresetOverride
 */
@InternalMockzillaApi
@Suppress("TYPE_ALIAS")
@Serializable
public data class SerializableEndpointPatchItemDto(
    val key: EndpointConfiguration.Key,
    val shouldFail: SetOrDont<Boolean?> = SetOrDont.DoNotSet,
    val delayMs: SetOrDont<Int?> = SetOrDont.DoNotSet,
    val appliedPresetOverride: SetOrDont<DashboardOverridePreset> = SetOrDont.DoNotSet
) {
    public companion object {
        public fun allUnset(key: String): SerializableEndpointPatchItemDto = allUnset(EndpointConfiguration.Key(key))

        public fun allUnset(key: EndpointConfiguration.Key): SerializableEndpointPatchItemDto = SerializableEndpointPatchItemDto(
            key = key,
            shouldFail = SetOrDont.DoNotSet,
            delayMs = SetOrDont.DoNotSet,
            appliedPresetOverride = SetOrDont.DoNotSet,
        )

        public fun allSet(config: SerializableEndpointConfig): SerializableEndpointPatchItemDto = SerializableEndpointPatchItemDto(
            key = config.key,
            shouldFail = SetOrDont.Set(config.shouldFail),
            delayMs = SetOrDont.Set(config.delayMs)
        )
    }
}

/**
 * @property entries
 */
@InternalMockzillaApi
@Serializable
public data class MockDataResponseDto(
    val entries: List<SerializableEndpointConfig>
)

/**
 * @property entries
 */
@InternalMockzillaApi
@Serializable
public data class SerializableEndpointConfigPatchRequestDto(
    val entries: List<SerializableEndpointPatchItemDto>
) {
    public constructor(entry: SerializableEndpointPatchItemDto) : this(listOf(entry))
}

@InternalMockzillaApi
@Serializable(with = ServiceResultSerializer::class)
public sealed class SetOrDont<out T> {
    @Serializable
    @SerialName("DoNotSet")
    public data object DoNotSet : SetOrDont<Nothing>()

    /**
     * @property value
     */
    @Serializable
    @SerialName("Set")
    public data class Set<T>(val value: T) : SetOrDont<T>()
}

@InternalMockzillaApi
public class ServiceResultSerializer<T : Any>(
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
    public data class ServiceResultSurrogate<out T : Any>(
        val type: Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // */ for "UnSet" results.
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val value: T? = null,
    ) {
        public enum class Type {
            Set, UnSet
        }
    }
}
