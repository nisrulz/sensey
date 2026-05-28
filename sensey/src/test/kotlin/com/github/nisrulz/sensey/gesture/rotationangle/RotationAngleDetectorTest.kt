
package com.github.nisrulz.sensey.gesture.rotationangle

import com.github.nisrulz.sensey.contract.GestureTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RotationAngleDetectorTest {
    @Test
    fun dispatchesNothingOnEmptyEvents() {
        val events = mutableListOf<RotationAngleEvent>()
        val alwaysNullTrigger =
            object : GestureTrigger<RotationAngleEvent> {
                override fun evaluate(
                    values: FloatArray,
                    timestamp: Long,
                ) = null
            }
        val detector = RotationAngleDetector(alwaysNullTrigger) { events.add(it) }
        assertTrue(events.isEmpty())
    }

    @Test
    fun triggerEvaluatesValues() {
        val trigger = RotationAngleTrigger()
        val result = trigger.evaluate(floatArrayOf(45f, 30f, 90f), 0L)
        assertEquals(RotationAngleEvent(45f, 30f, 90f), result)
    }

    @Test
    fun triggerEvaluatesZeroValues() {
        val trigger = RotationAngleTrigger()
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(RotationAngleEvent(0f, 0f, 0f), result)
    }
}
