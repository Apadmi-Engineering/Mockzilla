package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.runtime.mutableStateOf

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.utils.Platform
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import io.ktor.http.HttpStatusCode

import kotlin.time.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    }

    private suspend fun loadIncumbentValues(key: EndpointConfiguration.Key) {
        val endpoint = endpointsService.fetchAllEndpointConfigs(device).map { endpoint ->
            endpoint.firstOrNull { it.key == key }
        }

        state.value = endpoint.mapCatching { config ->
            val current = config?.appliedPresetOverride
            val isEditing = variant == State.Editing.Variant.Edit
            State.Editing(
                isSaving = false,
                statusCode = current?.response?.statusCode.takeIf { isEditing },
                body = current?.response?.body.takeIf { isEditing },
                headers = current?.response?.headers.takeIf { isEditing },
                responseType = State.Editing.ResponseType.PlainText,
                variant = variant
            )
        }.fold(
            onSuccess = { it },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Loading
            }
        )
    }

    fun save() = viewModelScope.launch {
        val currentState = state.value as? State.Editing ?: return@launch
        val appName = when (Platform.current) {
            Platform.Desktop -> "Mockzilla Desktop"
            else -> "Mockzilla Embedded UI"
        }
        updateService.applyPreset(device,
            key, DashboardOverridePreset(
                name = "Custom Preset",
                description = "Edited through $appName on: ${Clock.System.now()}",
                type = DashboardOverridePreset.Type.Other,
                response = PartialMockzillaHttpResponse(
                    body = currentState.body,
                    statusCode = currentState.statusCode,
                    headers = currentState.headers
                ),
                isManagementUiDefinedCustomPreset = true
            )
        )
    }

    fun clearStatusCode() = viewModelScope.launch {
        val current = state.value as? State.Editing ?: return@launch
    }

    sealed class State {
        data object Loading : State()
        /**
         * @property isSaving
         * @property statusCode
         * @property body
         * @property headers
         * @property responseType
         * @property hasBodyError
         * @property variant
         */
        data class Editing(
            val isSaving: Boolean,
            val statusCode: HttpStatusCode?,
            val body: String? = null,
            val hasBodyError: Boolean = false,
            val headers: Map<String, String>? = null,
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
        }
    }
}
