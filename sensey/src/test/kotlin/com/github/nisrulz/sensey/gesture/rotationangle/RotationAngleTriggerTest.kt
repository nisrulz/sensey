
package com.github.nisrulz.sensey.gesture.rotationangle

import org.junit.Assert.assertEquals
import org.junit.Test

class RotationAngleTriggerTest {
    private val trigger = RotationAngleTrigger()

    @Test
    fun returnsRotationEvent() {
        val result = trigger.evaluate(floatArrayOf(45f, 30f, 90f), 0L)
        assertEquals(RotationAngleEvent(45f, 30f, 90f), result)
    }

    @Test
    fun returnsEventWithZeroAngles() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(RotationAngleEvent(0f, 0f, 0f), result)
    }

    @Test
    fun handlesNegativeAngles() {
        val result = trigger.evaluate(floatArrayOf(-45f, -30f, -90f), 0L)
        assertEquals(RotationAngleEvent(-45f, -30f, -90f), result)
    }
}
