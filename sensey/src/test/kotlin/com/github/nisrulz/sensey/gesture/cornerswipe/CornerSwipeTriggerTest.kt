
package com.github.nisrulz.sensey.gesture.cornerswipe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CornerSwipeTriggerTest {
    private val trigger = CornerSwipeTrigger(cornerRadiusPx = 48f)

    @Test
    fun topLeftCorner() {
        val event = trigger.evaluate(floatArrayOf(10f, 10f, 100f, 150f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Corner.TOP_LEFT, event!!.corner)
    }

    @Test
    fun topRightCorner() {
        val event = trigger.evaluate(floatArrayOf(1050f, 10f, 900f, 150f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Corner.TOP_RIGHT, event!!.corner)
    }

    @Test
    fun bottomLeftCorner() {
        val event = trigger.evaluate(floatArrayOf(10f, 1880f, 100f, 1700f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Corner.BOTTOM_LEFT, event!!.corner)
    }

    @Test
    fun bottomRightCorner() {
        val event = trigger.evaluate(floatArrayOf(1050f, 1880f, 900f, 1700f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Corner.BOTTOM_RIGHT, event!!.corner)
    }

    @Test
    fun directionRight() {
        val event = trigger.evaluate(floatArrayOf(10f, 10f, 200f, 10f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Direction.RIGHT, event!!.direction)
    }

    @Test
    fun directionDown() {
        val event = trigger.evaluate(floatArrayOf(10f, 10f, 10f, 200f, 1080f, 1920f), 0L)
        assertNotNull(event)
        assertEquals(CornerSwipeEvent.Direction.DOWN, event!!.direction)
    }

    @Test
    fun noEventWhenSwipeStartsFromCenter() {
        val event = trigger.evaluate(floatArrayOf(540f, 960f, 600f, 1000f, 1080f, 1920f), 0L)
        assertNull(event)
    }

    @Test
    fun disabledCornerReturnsNull() {
        val trigger =
            CornerSwipeTrigger(
                cornerRadiusPx = 48f,
                enabledCorners = setOf(CornerSwipeEvent.Corner.TOP_LEFT),
            )
        assertNull(trigger.evaluate(floatArrayOf(1050f, 10f, 900f, 150f, 1080f, 1920f), 0L))
    }

    @Test
    fun nullForEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }
}
