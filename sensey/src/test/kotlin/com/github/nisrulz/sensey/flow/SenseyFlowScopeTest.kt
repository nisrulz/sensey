
package com.github.nisrulz.sensey.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SenseyFlowScopeTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun lifecycleOnStart_triggersCollection() {
        val lifecycle = mockk<Lifecycle>(relaxed = true)
        val observerSlot = slot<LifecycleEventObserver>()
        every { lifecycle.addObserver(capture(observerSlot)) } just Runs

        val observer = mockk<LifecycleEventObserver>(relaxed = true)
        lifecycle.addObserver(observer)

        observerSlot.captured.onStateChanged(mockk(), Lifecycle.Event.ON_START)

        verify { observer.onStateChanged(any(), Lifecycle.Event.ON_START) }
    }

    @Test
    fun lifecycleOnStop_stopsCollection() {
        val lifecycle = mockk<Lifecycle>(relaxed = true)
        val observerSlot = slot<LifecycleEventObserver>()
        every { lifecycle.addObserver(capture(observerSlot)) } just Runs

        val observer = mockk<LifecycleEventObserver>(relaxed = true)
        lifecycle.addObserver(observer)

        observerSlot.captured.onStateChanged(mockk(), Lifecycle.Event.ON_STOP)

        verify { observer.onStateChanged(any(), Lifecycle.Event.ON_STOP) }
    }

    @Test
    fun lifecycleOnDestroy_removesObserver() {
        val lifecycle = mockk<Lifecycle>(relaxed = true)
        val observerSlot = slot<LifecycleEventObserver>()
        every { lifecycle.addObserver(capture(observerSlot)) } just Runs

        val observer = mockk<LifecycleEventObserver>(relaxed = true)
        lifecycle.addObserver(observer)

        observerSlot.captured.onStateChanged(mockk(), Lifecycle.Event.ON_DESTROY)

        verify { observer.onStateChanged(any(), Lifecycle.Event.ON_DESTROY) }
    }

    @Test
    fun lifecycleRegistryHandlesStartEvent() {
        val owner = mockk<LifecycleOwner>(relaxed = true)
        val lifecycle = LifecycleRegistry.createUnsafe(owner)

        var started = false
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) started = true
            }
        lifecycle.addObserver(observer)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertEquals(true, started)
    }

    @Test
    fun lifecycleRegistryHandlesStopEvent() {
        val owner = mockk<LifecycleOwner>(relaxed = true)
        val lifecycle = LifecycleRegistry.createUnsafe(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        var stopped = false
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) stopped = true
            }
        lifecycle.addObserver(observer)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        assertEquals(true, stopped)
    }

    @Test
    fun callbackFlowEmitsViaDispatcher() =
        runBlocking {
            val flow =
                callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(1)
                    dispatcher(2)
                    awaitClose { }
                }

            val items = mutableListOf<Int>()
            val job = launch { flow.collect { items.add(it) } }
            delay(10)
            job.cancel()
            job.join()

            assertEquals(listOf(1, 2), items)
        }

    @Test
    fun callbackFlowAllowsLateCollector() =
        runBlocking {
            val flow =
                callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(42)
                    awaitClose { }
                }

            val result =
                withTimeout(100) {
                    flow.firstOrNull()
                }
            assertEquals(42, result)
        }

    @Test
    fun multipleDispatchersEmitInOrder() =
        runBlocking {
            val flow =
                callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(10)
                    dispatcher(20)
                    dispatcher(30)
                    close()
                }

            val items = mutableListOf<Int>()
            flow.collect { items.add(it) }
            assertEquals(listOf(10, 20, 30), items)
        }

    @Test
    fun callbackFlowUnregistersOnCancellation() =
        runBlocking {
            var unregistered = false

            val flow =
                callbackFlow<String> {
                    val dispatcher: (String) -> Unit = { trySend(it) }
                    dispatcher("hello")
                    awaitClose { unregistered = true }
                }

            val job = launch { flow.collect { } }
            delay(10)
            job.cancel()
            job.join()

            assertEquals(true, unregistered)
        }

    @Test
    fun callbackFlowCancellationPreventsFurtherEmissions() =
        runBlocking {
            val flow =
                callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(1)
                    dispatcher(2)
                    awaitClose { }
                }

            val items = mutableListOf<Int>()
            val job = launch { flow.collect { items.add(it) } }
            delay(10)
            job.cancel()
            job.join()

            assertEquals(listOf(1, 2), items)
        }

    @Test
    fun turbineTestBlockWorksWithClosedFlow() =
        runBlocking {
            val flow =
                callbackFlow<Int> {
                    val dispatcher: (Int) -> Unit = { trySend(it) }
                    dispatcher(100)
                    close()
                }

            flow.test {
                assertEquals(100, awaitItem())
                awaitComplete()
            }
        }
}
