
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TwoFingerSwipeTriggerTest {
    private val trigger = TwoFingerSwipeTrigger(minDistance = 40f)

    @Test
    fun swipeDetected() {
        val result = trigger.evaluate(panX = 100f, panY = 0f) as? TouchEvent.Swipe
        assertEquals(TouchEvent.Direction.RIGHT, result?.direction)
        assertEquals(2, result?.fingerCount)
    }

    @Test
    fun belowMinDistanceReturnsNull() {
        assertNull(trigger.evaluate(panX = 5f, panY = 5f))
    }

    @Test
    fun directionFromDominantAxis() {
        assertEquals(
            TouchEvent.Direction.LEFT,
            (trigger.evaluate(panX = -50f, panY = 0f) as TouchEvent.Swipe?)?.direction,
        )
        assertEquals(
            TouchEvent.Direction.DOWN,
            (trigger.evaluate(panX = 0f, panY = 50f) as TouchEvent.Swipe?)?.direction,
        )
        assertEquals(
            TouchEvent.Direction.UP,
            (trigger.evaluate(panX = 0f, panY = -50f) as TouchEvent.Swipe?)?.direction,
        )
    }
}
