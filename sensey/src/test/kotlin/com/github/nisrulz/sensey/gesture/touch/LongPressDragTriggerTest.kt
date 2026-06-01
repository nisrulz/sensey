
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LongPressDragTriggerTest {
    private val trigger = LongPressDragTrigger(minDistance = 20f)

    @Test
    fun dragDetected() {
        val result = trigger.evaluate(deltaX = 50f, deltaY = 0f)
        assertTrue(result is TouchEvent.LongPressDrag)
        val drag = result as TouchEvent.LongPressDrag
        assertEquals(TouchEvent.Direction.RIGHT, drag.direction)
        assertTrue(drag.distance >= 50f)
    }

    @Test
    fun belowMinDistanceReturnsNull() {
        assertNull(trigger.evaluate(deltaX = 5f, deltaY = 5f))
    }

    @Test
    fun directionFromDominantAxis() {
        assertEquals(
            TouchEvent.Direction.DOWN,
            (trigger.evaluate(deltaX = 10f, deltaY = 50f) as TouchEvent.LongPressDrag).direction,
        )
        assertEquals(
            TouchEvent.Direction.LEFT,
            (trigger.evaluate(deltaX = -50f, deltaY = 10f) as TouchEvent.LongPressDrag).direction,
        )
        assertEquals(
            TouchEvent.Direction.UP,
            (trigger.evaluate(deltaX = 10f, deltaY = -50f) as TouchEvent.LongPressDrag).direction,
        )
    }
}
