@file:Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.State

import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.yield

class CreateEditPresetViewModelTests : CoroutineTest() {
    private val dummyKey = EndpointConfiguration.Key("my-endpoint")

    @RelaxedMockK
    lateinit var endpointsServiceMock: MockzillaManagement.EndpointsService

    @RelaxedMockK
    lateinit var updateServiceMock: MockzillaManagement.UpdateService

    @RelaxedMockK
    lateinit var eventBusMock: EventBus

    private fun createSut(variant: State.Editing.Variant = State.Editing.Variant.Edit) =
        CreateEditPresetViewModel(
            key = dummyKey,
            device = Device.dummy(),
            variant = variant,
            endpointsService = endpointsServiceMock,
            updateService = updateServiceMock,
            eventBus = eventBusMock,
            scope = testScope.backgroundScope
        )

    private fun dummyConfig(
        body: String? = """{"key":"value"}""",
        statusCode: HttpStatusCode = HttpStatusCode.OK,
        headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    ) = SerializableEndpointConfig.allNulls(dummyKey, "My Endpoint", 1).copy(
        appliedPresetOverride = DashboardOverridePreset(
            name = "Preset",
            description = null,
            type = null,
            response = PartialMockzillaHttpResponse(
                body = body,
                statusCode = statusCode,
                headers = headers
            ),
            isManagementUiDefinedCustomPreset = true
        )
    )

    @Test
    fun `init - edit variant - loads config and populates state with committed values`() = runBlockingTest {
        /* Setup */
        val config = dummyConfig()
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(config)))

        /* Run Test */
        val sut = createSut(State.Editing.Variant.Edit)
        yield()

        /* Verify */
        val state = sut.state.value as State.Editing
        assertEquals("""{"key":"value"}""", state.body)
        assertEquals("""{"key":"value"}""", state.committedBody)
        assertEquals(HttpStatusCode.OK, state.statusCode)
        assertEquals(HttpStatusCode.OK, state.committedStatusCode)
        assertEquals(
            listOf(State.Editing.RequestHeader("Content-Type", "application/json")),
            state.headers
        )
        assertEquals(
            listOf(State.Editing.RequestHeader("Content-Type", "application/json")),
            state.committedHeaders
        )
        assertEquals("My Endpoint", state.endpointName)
        assertEquals(1L, state.syncToken)
        assertFalse(state.isDirty)
    }

    @Test
    fun `init - create variant - body and status code are null`() = runBlockingTest {
        /* Setup */
        val config = dummyConfig()
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(config)))

        /* Run Test */
        val sut = createSut(State.Editing.Variant.Create)
        yield()

        /* Verify */
        val state = sut.state.value as State.Editing
        assertNull(state.body)
        assertNull(state.statusCode)
        assertEquals(emptyList(), state.headers)
        assertNull(state.committedBody)
        assertNull(state.committedStatusCode)
        assertFalse(state.isDirty)
    }

    @Test
    fun `isDirty - false after init, true after statusCode change`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        val sut = createSut()
        yield()

        assertFalse((sut.state.value as State.Editing).isDirty)

        /* Run Test */
        sut.onNewStatusCode(HttpStatusCode.NotFound)

        /* Verify */
        assertTrue((sut.state.value as State.Editing).isDirty)
    }

    @Test
    fun `isDirty - true after body change, false when reverted`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig(body = """{"a":1}"""))))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onNewResponseBody("changed")
        assertTrue((sut.state.value as State.Editing).isDirty)

        sut.onNewResponseBody("""{"a":1}""")
        assertFalse((sut.state.value as State.Editing).isDirty)
    }

    @Test
    fun `isDirty - true after header added`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig(headers = emptyMap()))))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onAddHeader("X-Custom", "foo")

        /* Verify */
        assertTrue((sut.state.value as State.Editing).isDirty)
    }

    @Test
    fun `onNewResponseBody - updates body and clears bodyParseError for valid JSON`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onNewResponseBody("""{"new":true}""")

        /* Verify */
        val state = sut.state.value as State.Editing
        assertEquals("""{"new":true}""", state.body)
        assertNull(state.bodyParseError)
    }

    @Test
    fun `onNewResponseBody - sets bodyParseError for invalid JSON`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onNewResponseBody("not json at all")

        /* Verify */
        val state = sut.state.value as State.Editing
        assertEquals("not json at all", state.body)
        assertTrue(state.bodyParseError != null)
    }

    @Test
    fun `onAddHeader - appends header to list`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig(headers = emptyMap()))))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onAddHeader("Authorization", "Bearer token")

        /* Verify */
        val state = sut.state.value as State.Editing
        assertEquals(
            listOf(State.Editing.RequestHeader("Authorization", "Bearer token")),
            state.headers
        )
    }

    @Test
    fun `onRemoveHeader - removes header from list`() = runBlockingTest {
        /* Setup */
        val header = State.Editing.RequestHeader("Content-Type", "application/json")
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig(headers = mapOf("Content-Type" to "application/json")))))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.onRemoveHeader(header)

        /* Verify */
        val state = sut.state.value as State.Editing
        assertEquals(emptyList(), state.headers)
    }

    @Test
    fun `onFormatResponseBody - formats JSON body and increments syncToken`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig(body = """{"a":1,"b":2}"""))))
        val sut = createSut()
        yield()

        val tokenBefore = (sut.state.value as State.Editing).syncToken

        /* Run Test */
        sut.onFormatResponseBody()

        /* Verify */
        val state = sut.state.value as State.Editing
        assertTrue(state.body!!.contains("\n"), "Expected formatted (multi-line) JSON")
        assertEquals(tokenBefore + 1, state.syncToken)
    }

    @Test
    fun `save - success - sets navigateUp true and clears dirty state`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        coEvery { updateServiceMock.applyPreset(Device.dummy(), dummyKey, any()) }
            .returns(Result.success(Unit))
        val sut = createSut()
        yield()

        sut.onNewStatusCode(HttpStatusCode.NotFound)
        assertTrue((sut.state.value as State.Editing).isDirty)

        /* Run Test */
        sut.save(shouldNavigateOnCompletion = true)
        yield()

        /* Verify */
        val state = sut.state.value as State.Editing
        assertTrue(state.navigateUp)
        assertFalse(state.isSaving)
        assertFalse(state.isDirty)
        assertEquals(HttpStatusCode.NotFound, state.committedStatusCode)
    }

    @Test
    fun `save - success - does not increment syncToken`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        coEvery { updateServiceMock.applyPreset(Device.dummy(), dummyKey, any()) }
            .returns(Result.success(Unit))
        val sut = createSut()
        yield()

        val tokenBefore = (sut.state.value as State.Editing).syncToken

        /* Run Test */
        sut.save(shouldNavigateOnCompletion = true)
        yield()

        /* Verify */
        assertEquals(tokenBefore, (sut.state.value as State.Editing).syncToken)
    }

    @Test
    fun `save - failure - sends GenericError and navigateUp stays false`() = runBlockingTest {
        val dummyException = RuntimeException("network error")
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        coEvery { updateServiceMock.applyPreset(Device.dummy(), dummyKey, any()) }
            .returns(Result.failure(dummyException))
        val sut = createSut()
        yield()

        /* Run Test */
        sut.save(shouldNavigateOnCompletion = true)
        yield()

        /* Verify */
        coVerify {
            eventBusMock.send(
                EventBus.Event.GenericError(
                    GenericErrorableOperation.ApplyPreset,
                    dummyException
                )
            )
        }
        assertFalse((sut.state.value as State.Editing).navigateUp)
    }

    @Test
    fun `consumeNavigateUp - sets navigateUp to false`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))
        coEvery { updateServiceMock.applyPreset(Device.dummy(), dummyKey, any()) }
            .returns(Result.success(Unit))
        val sut = createSut()
        yield()

        sut.save(shouldNavigateOnCompletion = true)
        yield()

        assertTrue((sut.state.value as State.Editing).navigateUp)

        /* Run Test */
        sut.consumeNavigateUp()

        /* Verify */
        assertFalse((sut.state.value as State.Editing).navigateUp)
    }

    @Test
    fun `PresetApplied event - reloads data and increments syncToken`() = runBlockingTest {
        /* Setup */
        val presetAppliedFlow = flowOf(EventBus.Event.PresetApplied)
        every { eventBusMock.events }.returns(presetAppliedFlow)
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(listOf(dummyConfig())))

        /* Run Test */
        val sut = createSut()
        yield()
        yield()

        /* Verify — init fires syncToken=1, PresetApplied fires syncToken=2 */
        val state = sut.state.value as State.Editing
        assertEquals(2L, state.syncToken)
        assertFalse(state.isDirty)
    }
}
