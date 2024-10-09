package com.apadmi.mockzilla.testutils

import io.mockk.MockKAnnotations
import org.junit.Before

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

open class CoroutineTest {
    protected val testScope = TestScope()
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    protected fun runBlockingTest(
        testBody: suspend TestScope.() -> Unit
    ) {
        testDispatcher = StandardTestDispatcher(testScope.testScheduler)
        Dispatchers.setMain(testDispatcher)
        // Ideally should use testScope.runBlockingTest() but it does not work.
        testScope.runTest(dispatchTimeoutMs = 1234, testBody = testBody)
        Dispatchers.resetMain()
    }
}
