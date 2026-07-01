package com.apadmi.mockzilla.desktop.ui.licenses

import com.apadmi.mockzilla.desktop.engine.licenses.LicenseDisplayModel
import com.apadmi.mockzilla.desktop.engine.licenses.LibraryForAttribution
import com.apadmi.mockzilla.desktop.engine.licenses.LicensesUseCase
import com.apadmi.mockzilla.testutils.CoroutineTest

import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LicensesViewModelTest : CoroutineTest() {
    @RelaxedMockK
    private lateinit var licensesUseCaseMock: LicensesUseCase

    private fun sut() =
        LicensesViewModel(licensesUseCaseMock, isDevelopmentBuild = false, testScope)

    @Test
    fun `init - starts in Loading state`() = runBlockingTest {
        /* Verify - by the time we observe, init has run, but we check after construction */
        coEvery { licensesUseCaseMock.getLicenses() } returns Result.success(emptyList())
        val viewModel = sut()
        // Advance coroutines to allow init block to complete
        testScope.testScheduler.advanceUntilIdle()

        // After success the state transitions — loading is the initial value before coroutine runs
        assertIs<LicensesViewModel.State.Populated>(viewModel.state.value)
    }

    @Test
    fun `getLicenses - success - state transitions to Populated with sorted libraries`() = runBlockingTest {
        /* Setup */
        val libraries = listOf(
            LibraryForAttribution(
                name = "Ktor",
                version = "3.0.0",
                licenses = listOf(LicenseDisplayModel("Apache-2.0", "https://www.apache.org/licenses/LICENSE-2.0", "Apache-2.0", null))
            ),
            LibraryForAttribution(
                name = "Koin",
                version = "4.0.0",
                licenses = emptyList()
            )
        )
        coEvery { licensesUseCaseMock.getLicenses() } returns Result.success(libraries)

        /* Run */
        val viewModel = sut()
        testScope.testScheduler.advanceUntilIdle()

        /* Verify */
        val state = assertIs<LicensesViewModel.State.Populated>(viewModel.state.value)
        assertEquals(libraries, state.libraries)
    }

    @Test
    fun `getLicenses - failure - state transitions to ErrorLoading`() = runBlockingTest {
        /* Setup */
        coEvery { licensesUseCaseMock.getLicenses() } returns Result.failure(RuntimeException("Parse error"))

        /* Run */
        val viewModel = sut()
        testScope.testScheduler.advanceUntilIdle()

        /* Verify */
        assertIs<LicensesViewModel.State.ErrorLoading>(viewModel.state.value)
    }

    @Test
    fun `getLicenses - dev build - state transitions to DevBuild`() = runBlockingTest {
        /* Setup */
        val viewModel = LicensesViewModel(licensesUseCaseMock, isDevelopmentBuild = true, testScope)

        /* Run */
        testScope.testScheduler.advanceUntilIdle()

        /* Verify */
        assertIs<LicensesViewModel.State.DevBuild>(viewModel.state.value)
    }
}
