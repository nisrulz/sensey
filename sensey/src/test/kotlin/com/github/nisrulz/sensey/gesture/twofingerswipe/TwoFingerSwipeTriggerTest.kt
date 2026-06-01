
package com.github.nisrulz.sensey.gesture.twofingerswipe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TwoFingerSwipeTriggerTest {
    private val trigger = TwoFingerSwipeTrigger(minDragDistance = 40f)

    @Test
    fun rightSwipe() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 200f, 0f, 200f, 0f, 1.0f, 0f), 0L)
        assertEquals(TwoFingerSwipeEvent(TwoFingerSwipeEvent.Direction.RIGHT), result)
    }

    @Test
    fun leftSwipe() {
        val result = trigger.evaluate(floatArrayOf(200f, 0f, 0f, 0f, -200f, 0f, 1.0f, 0f), 0L)
        assertEquals(TwoFingerSwipeEvent(TwoFingerSwipeEvent.Direction.LEFT), result)
    }

    @Test
    fun downSwipe() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f, 200f, 0f, 200f, 1.0f, 0f), 0L)
        assertEquals(TwoFingerSwipeEvent(TwoFingerSwipeEvent.Direction.DOWN), result)
    }

    @Test
    fun upSwipe() {
        val result = trigger.evaluate(floatArrayOf(0f, 200f, 0f, 0f, 0f, -200f, 1.0f, 0f), 0L)
        assertEquals(TwoFingerSwipeEvent(TwoFingerSwipeEvent.Direction.UP), result)
    }

    @Test
    fun belowMinDistanceReturnsNull() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 30f, 0f, 30f, 0f, 1.0f, 0f), 0L))
    }

    @Test
    fun nullForEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }
}
