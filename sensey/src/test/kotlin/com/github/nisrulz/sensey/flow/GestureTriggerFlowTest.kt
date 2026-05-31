
package com.github.nisrulz.sensey.flow

import app.cash.turbine.test
import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GestureTriggerFlowTest {
    @Test
    fun observeReturnsEmptyFlowByDefault() =
        runBlocking {
            val trigger = TestTrigger()
            val result =
                withTimeout(100) {
                    trigger.observe().firstOrNull()
                }
            assertNull(result)
        }

    @Test
    fun callbackFlowBridgesDispatcherEmissions() =
        runBlocking {
            val events = mutableListOf<Int>()
            val flow =
                kotlinx.coroutines.flow.callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(1)
                    dispatcher(2)
                    dispatcher(3)
                    close()
                }
            flow.collect { events.add(it) }
            assertEquals(listOf(1, 2, 3), events)
        }

    @Test
    fun overriddenObserveReturnsCustomFlow() =
        runBlocking {
            val trigger = CustomFlowTrigger()
            trigger.observe().test {
                assertEquals("a", awaitItem())
                assertEquals("b", awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun overriddenObserveCanBeEmpty() =
        runBlocking {
            val trigger = EmptyObserveTrigger()
            val result =
                withTimeout(100) {
                    trigger.observe().firstOrNull()
                }
            assertNull(result)
        }

    @Test
    fun triggerEvaluateStillWorksWithOverriddenObserve() {
        val trigger = CustomFlowTrigger()
        assertEquals("event", trigger.evaluate(floatArrayOf(1f, 2f, 3f), 1000L))
    }

    @Test
    fun callbackFlowCleansUpOnCancellation() =
        runBlocking {
            var cleanedUp = false
            val flow =
                kotlinx.coroutines.flow.callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(42)
                    awaitClose { cleanedUp = true }
                }
            val items = mutableListOf<Int>()
            val job =
                launch {
                    flow.collect { items.add(it) }
                }
            delay(10)
            job.cancel()
            job.join()
            assertEquals(listOf(42), items)
            assertEquals(true, cleanedUp)
        }

    private class TestTrigger : GestureTrigger<Unit> {
        override fun evaluate(
            values: FloatArray,
            timestamp: Long,
        ): Unit? = Unit
    }

    private class CustomFlowTrigger : GestureTrigger<String> {
        override fun evaluate(
            values: FloatArray,
            timestamp: Long,
        ): String? = "event"

        override fun observe() = flowOf("a", "b")
    }

    private class EmptyObserveTrigger : GestureTrigger<String> {
        override fun evaluate(
            values: FloatArray,
            timestamp: Long,
        ): String? = null

        override fun observe() = emptyFlow<String>()
    }
}
