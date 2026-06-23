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
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.prettyPrintJson
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

internal class CreateEditPresetViewModel(
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
    private var syncCounter = 0L

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

        val token = ++syncCounter
        state.value = endpoint.mapCatching { config ->
            val current = config?.appliedPresetOverride ?: config?.deriveLegacyPreset()
            val isEditing = variant == State.Editing.Variant.Edit
            val body = current?.response?.body.takeIf { isEditing }
            val statusCode = current?.response?.statusCode.takeIf { isEditing }
            val headers = current?.response?.headers
                ?.map { State.Editing.RequestHeader(key = it.key, value = it.value) }
                .takeIf { isEditing } ?: emptyList()
            State.Editing(
                isSaving = false,
                syncToken = token,
                statusCode = statusCode,
                body = body,
                bodyParseError = null,
                headers = headers,
                responseType = inferResponseTypeFromBody(body),
                variant = variant,
                endpointName = config?.name ?: key.raw,
                committedBody = body,
                committedStatusCode = statusCode,
                committedHeaders = headers,
            )
        }.fold(
            onSuccess = { it },
            onFailure = {
                eventBus.send(
                    Event.GenericError(
                        GenericErrorableOperation.FetchEndpointConfigs,
                        it
                    )
                )
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
            // Update committed values to match what was just saved — clears dirty state
            // without incrementing syncToken (text fields already show the right content)
            state.value = currentState.copy(
                isSaving = false,
                committedBody = currentState.body,
                committedStatusCode = currentState.statusCode,
                committedHeaders = currentState.headers,
                navigateUp = true
            )
        }.onFailure {
            eventBus.send(
                Event.GenericError(
                    GenericErrorableOperation.ApplyPreset,
                    it
                )
            )
        }
    }

    fun onNewStatusCode(newStatusCode: HttpStatusCode) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(statusCode = newStatusCode)
    }

    fun onNewResponseType(newResponseType: State.Editing.ResponseType) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(responseType = newResponseType)
    }

    fun onNewResponseBody(newBody: String) {
        val currentState = state.value as? State.Editing ?: return
        state.value = try {
            Json.parseToJsonElement(newBody)
            currentState.copy(
                body = newBody,
                bodyParseError = null,
            )
        } catch (e: Exception) {
            currentState.copy(
                body = newBody,
                bodyParseError = e.message?.substringBefore("\nJSON input:")?.trim(),
            )
        }
    }

    fun onFormatResponseBody() {
        val currentState = state.value as? State.Editing ?: return
        val bodyResponse = currentState.body ?: return

        val formatted = when (currentState.responseType) {
            State.Editing.ResponseType.Json -> bodyResponse.prettyPrintJson()
            State.Editing.ResponseType.Html,
            State.Editing.ResponseType.PlainText,
            State.Editing.ResponseType.None -> return
        }

        // Increment syncToken so the text field picks up the formatted body
        state.value = currentState.copy(body = formatted, syncToken = ++syncCounter)
    }

    fun onAddHeader(key: String, value: String) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(
            headers = currentState.headers.plus(State.Editing.RequestHeader(key = key, value = value))
        )
    }

    fun onRemoveHeader(header: State.Editing.RequestHeader) {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(
            headers = currentState.headers.minus(header)
        )
    }

    fun consumeNavigateUp() {
        val currentState = state.value as? State.Editing ?: return
        state.value = currentState.copy(navigateUp = false)
    }

    sealed class State {
        data object Loading : State()

        /**
         * @property isSaving
         * @property syncToken Incremented on server reload or format-apply; drives LaunchedEffect in the UI.
         * Committed values alone would suffice for server reload, but format changes [body] without
         * touching [committedBody], so syncToken is the only signal available to push the reformatted
         * content into the text field.
         * @property statusCode
         * @property body
         * @property headers
         * @property responseType
         * @property variant
         * @property endpointName The display name of the endpoint shown in the list
         * @property bodyParseError
         * @property committedBody Last body value synced from the server
         * @property committedStatusCode Last status code synced from the server
         * @property committedHeaders Last headers synced from the server
         * @property navigateUp
         */
        data class Editing(
            val isSaving: Boolean,
            val syncToken: Long,
            val statusCode: HttpStatusCode?,
            val body: String? = null,
            val bodyParseError: String? = null,
            val headers: List<RequestHeader> = emptyList(),
            val responseType: ResponseType,
            val variant: Variant,
            val endpointName: String = "",
            val committedBody: String? = null,
            val committedStatusCode: HttpStatusCode? = null,
            val committedHeaders: List<RequestHeader> = emptyList(),
            val navigateUp: Boolean = false
        ) : State() {
            val isDirty: Boolean
                get() = body != committedBody ||
                        statusCode != committedStatusCode ||
                        headers != committedHeaders

            @Suppress("EnumEntryOrder")
            enum class ResponseType {
                Html,
                Json,
                None,
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
    val response = PartialMockzillaHttpResponse(
        statusCode = defaultStatus,
        headers = defaultHeaders,
        body = defaultBody
    ).takeIf {
        listOf(defaultStatus, defaultHeaders, defaultBody).any {
            it != null
        }
    } ?: PartialMockzillaHttpResponse(
        statusCode = errorStatus,
        headers = errorHeaders,
        body = errorBody
    ).takeIf {
        listOf(errorStatus, errorHeaders, errorBody).any {
            it != null
        }
    }

    response?.let {
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
