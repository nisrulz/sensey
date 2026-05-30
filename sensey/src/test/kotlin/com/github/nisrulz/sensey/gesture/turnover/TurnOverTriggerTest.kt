
package com.github.nisrulz.sensey.gesture.turnover

import com.github.nisrulz.sensey.internal.GyroIntegrator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TurnOverTriggerTest {
    private val trigger = TurnOverTrigger(angleThreshold = 130f)

    @Test
    fun noEventOnSmallRotation() {
        // 5 rad/s * 0.1s = ~29° per reading, three readings = ~86°, below 130°
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(5f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(5f, 0f, 0f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(5f, 0f, 0f), 300L))
    }

    @Test
    fun flippedWhenNetRotationExceedsThreshold() {
        // 15 rad/s * 0.2s = ~172° net rotation — exceeds 130°
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertEquals(TurnOverEvent.Flipped, trigger.evaluate(floatArrayOf(15f, 0f, 0f), 200L))
    }

    @Test
    fun flippedOnCombinedAxes() {
        // Rotation distributed across X and Y: sqrt(100²+100²) = 141°, above 130°
        val trigger = TurnOverTrigger(angleThreshold = 130f)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertEquals(TurnOverEvent.Flipped, trigger.evaluate(floatArrayOf(10f, 10f, 0f), 200L))
    }

    @Test
    fun flippedOnceThenResets() {
        val trigger = TurnOverTrigger(angleThreshold = 50f)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertEquals(TurnOverEvent.Flipped, trigger.evaluate(floatArrayOf(5f, 0f, 0f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 300L))
    }

    @Test
    fun usesSharedIntegrator() {
        val integrator = GyroIntegrator()
        val triggerA = TurnOverTrigger(angleThreshold = 100f, integrator = integrator)
        val triggerB = TurnOverTrigger(angleThreshold = 100f, integrator = integrator)

        triggerA.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        triggerB.evaluate(floatArrayOf(5f, 0f, 0f), 200L)
        assertNull(triggerA.evaluate(floatArrayOf(0f, 0f, 0f), 300L))
        assertEquals(TurnOverEvent.Flipped, triggerA.evaluate(floatArrayOf(5f, 0f, 0f), 500L))
        assertNull(triggerB.evaluate(floatArrayOf(0f, 0f, 0f), 600L))
    }
}
