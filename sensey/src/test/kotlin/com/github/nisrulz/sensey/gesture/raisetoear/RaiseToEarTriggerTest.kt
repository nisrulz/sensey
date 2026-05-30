
package com.github.nisrulz.sensey.gesture.raisetoear

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RaiseToEarTriggerTest {
    private val trigger = RaiseToEarTrigger(maxProximityCm = 5f, minGzRatio = 0.3f, debounceMs = 500L)

    @Test
    fun firesWhenNearAndNotFlat() {
        // proximity=1cm, gravity along -Y (phone upright in portrait) → |gz|≈0, not flat
        assertEquals(RaiseToEarEvent.AtEar, trigger.evaluate(floatArrayOf(1f, 0f, -9.81f, 0f), 1000L))
    }

    @Test
    fun noEventWhenFar() {
        assertNull(trigger.evaluate(floatArrayOf(10f, 0f, -9.81f, 0f), 1000L))
    }

    @Test
    fun noEventWhenFlatOnTable() {
        // proximity=1cm, gravity along +Z (phone flat on table, screen up) → |gz|/g≈1 → flat
        assertNull(trigger.evaluate(floatArrayOf(1f, 0f, 0f, 9.81f), 1000L))
    }

    @Test
    fun debouncesRepeatedTriggers() {
        assertEquals(RaiseToEarEvent.AtEar, trigger.evaluate(floatArrayOf(1f, 0f, -9.81f, 0f), 1000L))
        // Within debounce window (500ms)
        assertNull(trigger.evaluate(floatArrayOf(1f, 0f, -9.81f, 0f), 1200L))
        // After debounce window
        assertEquals(RaiseToEarEvent.AtEar, trigger.evaluate(floatArrayOf(1f, 0f, -9.81f, 0f), 2000L))
    }

    @Test
    fun noEventWithEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 1000L))
    }
}
