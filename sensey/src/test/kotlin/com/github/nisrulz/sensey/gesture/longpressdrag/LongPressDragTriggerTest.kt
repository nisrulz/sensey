
package com.github.nisrulz.sensey.gesture.longpressdrag

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LongPressDragTriggerTest {
    private val trigger = LongPressDragTrigger(minDragDistance = 20f)

    @Test
    fun rightDrag() {
        val result = trigger.evaluate(floatArrayOf(100f, 0f), 0L)
        assertEquals(LongPressDragEvent(LongPressDragEvent.Direction.RIGHT, 100f), result)
    }

    @Test
    fun leftDrag() {
        val result = trigger.evaluate(floatArrayOf(-100f, 0f), 0L)
        assertEquals(LongPressDragEvent(LongPressDragEvent.Direction.LEFT, 100f), result)
    }

    @Test
    fun downDrag() {
        val result = trigger.evaluate(floatArrayOf(0f, 100f), 0L)
        assertEquals(LongPressDragEvent(LongPressDragEvent.Direction.DOWN, 100f), result)
    }

    @Test
    fun upDrag() {
        val result = trigger.evaluate(floatArrayOf(0f, -100f), 0L)
        assertEquals(LongPressDragEvent(LongPressDragEvent.Direction.UP, 100f), result)
    }

    @Test
    fun horizontalDominatesOverVertical() {
        val result = trigger.evaluate(floatArrayOf(80f, 30f), 0L)
        assertEquals(LongPressDragEvent.Direction.RIGHT, result!!.direction)
    }

    @Test
    fun belowMinDistanceReturnsNull() {
        assertNull(trigger.evaluate(floatArrayOf(10f, 0f), 0L))
    }

    @Test
    fun nullForEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }
}
