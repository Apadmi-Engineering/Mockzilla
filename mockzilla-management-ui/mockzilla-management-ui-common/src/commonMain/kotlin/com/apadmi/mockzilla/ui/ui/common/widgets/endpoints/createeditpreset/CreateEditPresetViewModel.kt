package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.runtime.mutableStateOf
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.EventBus.Event
import com.apadmi.mockzilla.ui.utils.Platform
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import io.ktor.http.HttpStatusCode

import kotlin.time.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CreateEditPresetViewModel(
    private val key: EndpointConfiguration.Key,
    private val device: Device,
    private val variant: State.Editing.Variant,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    // using mutableStateOf here to avoid latency issues with text input
    // see https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
    // for reasons
    val state = mutableStateOf<State>(State.Loading)

    init {
        viewModelScope.launch {
            loadIncumbentValues(key)
        }
        eventBus.events.filter {
            it is Event.PresetApplied
        }
            .onEach {
                loadIncumbentValues(key)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun loadIncumbentValues(key: EndpointConfiguration.Key) {
        val endpoint = endpointsService.fetchAllEndpointConfigs(device).map { endpoint ->
            endpoint.firstOrNull { it.key == key }
        }

        state.value = endpoint.mapCatching { config ->
            val current = config?.appliedPresetOverride ?: config?.deriveLegacyPreset()
            val isEditing = variant == State.Editing.Variant.Edit
            State.Editing(
                isSaving = false,
                statusCode = current?.response?.statusCode.takeIf { isEditing },
                body = current?.response?.body.takeIf { isEditing },
                // Always starts as false because we assume plaintext if parsing
                // the body as JSON fails
                hasBodyError = false,
                headers = current?.response?.headers
                    ?.map { State.Editing.RequestHeader(key = it.key, value = it.value) }
                    .takeIf { isEditing } ?: emptyList(),
                responseType = inferResponseTypeFromBody(
                    current?.response?.body.takeIf { isEditing }
                ),
                variant = variant
            )
        }.fold(
            onSuccess = { it },
            onFailure = {
                eventBus.send(Event.GenericError)
                State.Loading
            }
        )
    }

    private fun inferResponseTypeFromBody(
        body: String?
    ): State.Editing.ResponseType {
        return try {
            Json.parseToJsonElement(body ?: return State.Editing.ResponseType.PlainText)
            State.Editing.ResponseType.Json
        } catch (_: Exception) {
            State.Editing.ResponseType.PlainText
        }
    }

    fun save() = viewModelScope.launch {
        val currentState = state.value as? State.Editing ?: return@launch
        val appName = when (Platform.current) {
            Platform.Desktop -> "Mockzilla Desktop"
            else -> "Mockzilla Embedded UI"
        }

        state.value = currentState.copy(isSaving = true)
        updateService.applyPreset(
            connection = device,
            key = key,
            dashboardOverridePreset = DashboardOverridePreset(
                name = "Custom Preset",
                description = "Edited through $appName on: ${Clock.System.now()}",
                type = DashboardOverridePreset.Type.Other,
                response = PartialMockzillaHttpResponse(
                    body = currentState.body,
                    statusCode = currentState.statusCode,
                    headers = currentState.headers?.associate { it.key to it.value }
                ),
                isManagementUiDefinedCustomPreset = true
            )
        ).onSuccess {
            eventBus.send(Event.EndpointDataChanged(listOf(key)))
            state.value = currentState.copy(isSaving = false)
        }.onFailure {
            eventBus.send(Event.GenericError)
        }
    }

    fun onNewStatusCode(newStatusCode: HttpStatusCode) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(statusCode = newStatusCode)
    }

    fun clearStatusCode() {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(statusCode = null)
    }

    fun onNewResponseType(newResponseType: State.Editing.ResponseType) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(responseType = newResponseType)
    }

    fun onNewResponseBody(newBody: String) {
        val currentState = state.value as? State.Editing ?: return
        val hasBodyError = try {
            Json.parseToJsonElement(newBody)
            false
        } catch (_: Exception) {
            true
        }
        state.value = currentState.copy(body = newBody, hasBodyError = hasBodyError)
    }

    fun onFormatResponseBody() {
        val currentState = state.value as? State.Editing ?: return
        val bodyResponse = currentState.body ?: return

        val prettyJson = Json { prettyPrint = true }
        val formatted = prettyJson.encodeToString(Json.parseToJsonElement(bodyResponse))

        state.value = currentState.copy(body = formatted)
    }

    fun clearResponseBody() {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(body = null)
    }

    fun onUpdateNewHeader(key: String? = null, value: String? = null) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(
            newHeader = currentState.newHeader.copy(
                key = key ?: currentState.newHeader.key,
                value = value ?: currentState.newHeader.value
            )
        )
    }

    fun onUpdateHeader(
        header: State.Editing.RequestHeader,
        key: String? = null,
        value: String? = null
    ) {
        val currentState = state.value as? State.Editing ?: return
        val updatedHeader = currentState.headers.firstOrNull { it == header } ?: return

        state.value = currentState.copy(
            headers = currentState.headers.filter { it != header }.plus(
                State.Editing.RequestHeader(
                    key = key ?: updatedHeader.key,
                    value = value ?: updatedHeader.value
                )
            )
        )
    }

    fun onAddHeader() {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(
            headers = currentState.headers.plus(currentState.newHeader),
            newHeader = State.Editing.RequestHeader()
        )
    }

    fun onRemoveHeader(header: State.Editing.RequestHeader) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(
            headers = currentState.headers.minus(header)
        )
    }

    fun clearHeaders() {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(headers = emptyList())
    }

    sealed class State {
        data object Loading : State()

        /**
         * @property isSaving
         * @property statusCode
         * @property body
         * @property headers
         * @property newHeader The header currently being edited by the user in the UI
         * @property responseType
         * @property hasBodyError
         * @property variant
         */
        data class Editing(
            val isSaving: Boolean,
            val statusCode: HttpStatusCode?,
            val body: String? = null,
            val hasBodyError: Boolean = false,
            val headers: List<RequestHeader> = emptyList(),
            val newHeader: RequestHeader = RequestHeader(),
            val responseType: ResponseType,
            val variant: Variant,
        ) : State() {
            enum class ResponseType {
                Json,
                PlainText,
                ;
            }

            enum class Variant {
                Create,
                Edit,
                ;
            }

            /**
             * @property key
             * @property value
             */
            data class RequestHeader(
                val key: String = "",
                val value: String = ""
            )
        }
    }
}

// Used for backward compatibility so that old versions of the Mockzilla SDK used with new desktop app
// where they're not sending the `appliedPresetOverride` field.
internal fun SerializableEndpointConfig.deriveLegacyPreset(): DashboardOverridePreset? {
    val response =  PartialMockzillaHttpResponse(
        statusCode = defaultStatus,
        headers = defaultHeaders,
        body = defaultBody
    ).takeIf { listOf(defaultStatus, defaultHeaders, defaultBody).any {
        it != null }
    } ?:  PartialMockzillaHttpResponse(
        statusCode = errorStatus,
        headers = errorHeaders,
        body = errorBody
    ).takeIf { listOf(errorStatus, errorHeaders, errorBody).any {
        it != null }
    }

    if (response != null) {
        return DashboardOverridePreset(
            name = "Derived preset",
            description = null,
            type = null,
            response = response,
            isManagementUiDefinedCustomPreset = false
        )
    }

    return null
}
