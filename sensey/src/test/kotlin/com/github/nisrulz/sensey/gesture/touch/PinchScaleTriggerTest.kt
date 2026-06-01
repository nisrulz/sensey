
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PinchScaleTriggerTest {
    private val trigger = PinchScaleTrigger()

    @Test
    fun pinchScaleInDetectedAfterConfirmation() {
        assertNull(trigger.evaluate(scaleFactor = 1.05f))
        assertNull(trigger.evaluate(scaleFactor = 1.05f))
        val result = trigger.evaluate(scaleFactor = 1.05f)
        assertTrue(result is TouchEvent.PinchScale)
        assertEquals(false, (result as TouchEvent.PinchScale).isScalingOut)
    }

    @Test
    fun pinchScaleOutDetectedAfterConfirmation() {
        trigger.evaluate(scaleFactor = 1.05f)
        trigger.evaluate(scaleFactor = 1.05f)
        trigger.evaluate(scaleFactor = 1.05f)
        trigger.reset()

        assertNull(trigger.evaluate(scaleFactor = 0.95f))
        assertNull(trigger.evaluate(scaleFactor = 0.95f))
        val result = trigger.evaluate(scaleFactor = 0.95f)
        assertTrue(result is TouchEvent.PinchScale)
        assertEquals(true, (result as TouchEvent.PinchScale).isScalingOut)
    }

    @Test
    fun stableScaleReturnsNull() {
        assertNull(trigger.evaluate(scaleFactor = 1.0f))
        assertNull(trigger.evaluate(scaleFactor = 1.0f))
        assertNull(trigger.evaluate(scaleFactor = 1.0f))
    }
}
