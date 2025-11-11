package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.stopMockzilla
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import kotlinx.coroutines.test.runTest

private typealias TestBlock<Sut> = suspend (
    sut: Sut,
    connection: MockzillaConnectionConfig,
    runtimeParams: MockzillaRuntimeParams
) -> Unit

expect suspend fun startTestingMockzilla(
    appName: String,
    appVersion: String,
    config: MockzillaConfig
): MockzillaRuntimeParams

@Suppress("LAMBDA_IS_NOT_LAST_PARAMETER")
internal fun <Sut> runIntegrationTest(
    appName: String = "dummy-app-name",
    appVersion: String = "dummy-app-version",
    config: MockzillaConfig = MockzillaConfig.Builder().setPort(0).addEndpoint(
        EndpointConfiguration.Builder("Id").build()
    )
        .build(),
    createSut: (MockzillaManagementRepository) -> Sut,
    testBlock: TestBlock<Sut>
) = runTest {
    /* Setup */
    val repo = MockzillaManagementRepositoryImpl.create(logger = Logger.SIMPLE)
    val runtimeParams = startTestingMockzilla(appName, appVersion, config)

    /* Run Test */
    testBlock(
        createSut(repo),
        MockzillaConnectionConfigImpl(runtimeParams.ip, port = runtimeParams.port.toString()),
        runtimeParams
    )

    /* Cleanup */
    stopMockzilla()
}
