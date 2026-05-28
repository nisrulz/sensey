
package com.github.nisrulz.sensey.gesture.pinchscale

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PinchScaleTriggerTest {
    private val trigger = PinchScaleTrigger()

    @Test
    fun noEventBelowScaleCount() {
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun scaleOutDetectedAfterThirdScaleIn() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        assertEquals(PinchScaleEvent(1.5f, false), trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun scaleInDetectedAfterThirdScaleOut() {
        trigger.evaluate(floatArrayOf(0.5f), 0L)
        trigger.evaluate(floatArrayOf(0.5f), 0L)
        assertEquals(PinchScaleEvent(0.5f, true), trigger.evaluate(floatArrayOf(0.5f), 0L))
    }

    @Test
    fun scaleOutOnlyOnce() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun resetClearsState() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.reset()
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }
}
