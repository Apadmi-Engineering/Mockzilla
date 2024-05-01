package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla
import com.apadmi.mockzilla.management.MockzillaManagement
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import kotlinx.coroutines.runBlocking

private typealias TestBlock = suspend (
    sut: MockzillaManagement,
    connection: MockzillaManagement.ConnectionConfig,
    runtimeParams: MockzillaRuntimeParams
) -> Unit

internal fun runIntegrationTest(
    appName: String = "dummy-app-name",
    appVersion: String = "dummy-app-version",
    config: MockzillaConfig = MockzillaConfig.Builder().setPort(0).addEndpoint(
        EndpointConfiguration.Builder("Id").build()
    )
        .build(),
    testBlock: TestBlock
) = runBlocking {
    /* Setup */
    val sut = MockzillaManagement.create(logger = Logger.SIMPLE)
    val runtimeParams = startMockzilla(appName, appVersion, config)

    /* Run Test */
    testBlock(
        sut,
        ConnectionConfigImpl("127.0.0.1", port = runtimeParams.port.toString()),
        runtimeParams
    )

    /* Cleanup */
    stopMockzilla()
}
