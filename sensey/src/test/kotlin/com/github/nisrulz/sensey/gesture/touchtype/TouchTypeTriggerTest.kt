
package com.github.nisrulz.sensey.gesture.touchtype

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TouchTypeTriggerTest {
    private val trigger =
        TouchTypeTrigger(
            swipeMinDistance = 120f,
            swipeThresholdVelocity = 200f,
        )

    @Test
    fun swipeRight() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.RIGHT),
            trigger.evaluate(floatArrayOf(200f, 0f, 300f, 0f), 0L),
        )
    }

    @Test
    fun swipeLeft() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.LEFT),
            trigger.evaluate(floatArrayOf(-200f, 0f, -300f, 0f), 0L),
        )
    }

    @Test
    fun swipeDown() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.DOWN),
            trigger.evaluate(floatArrayOf(0f, 200f, 0f, 300f), 0L),
        )
    }

    @Test
    fun swipeUp() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.UP),
            trigger.evaluate(floatArrayOf(0f, -200f, 0f, -300f), 0L),
        )
    }

    @Test
    fun scrollRight() {
        assertEquals(
            TouchTypeEvent.Scroll(TouchTypeEvent.Direction.RIGHT),
            trigger.evaluate(floatArrayOf(200f, 0f, 0f, 0f), 0L),
        )
    }

    @Test
    fun noEventBelowMinDistance() {
        assertNull(trigger.evaluate(floatArrayOf(10f, 0f, 0f, 0f), 0L))
    }

    @Test
    fun nullForEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }

    @Test
    fun swipeDownRightDiagonal() {
        val result = trigger.evaluate(floatArrayOf(150f, 150f, 250f, 250f), 0L)
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.DOWN_RIGHT),
            result,
        )
    }

    @Test
    fun swipeUpLeftDiagonal() {
        val result = trigger.evaluate(floatArrayOf(-150f, -150f, -250f, -250f), 0L)
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.UP_LEFT),
            result,
        )
    }

    @Test
    fun swipeDownLeftDiagonal() {
        val result = trigger.evaluate(floatArrayOf(-150f, 150f, -250f, 250f), 0L)
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.DOWN_LEFT),
            result,
        )
    }

    @Test
    fun swipeUpRightDiagonal() {
        val result = trigger.evaluate(floatArrayOf(150f, -150f, 250f, -250f), 0L)
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeEvent.Direction.UP_RIGHT),
            result,
        )
    }

    @Test
    fun scrollDownFromDiagonalMainComponent() {
        val result = trigger.evaluate(floatArrayOf(50f, 200f, 0f, 0f), 0L)
        assertEquals(
            TouchTypeEvent.Scroll(TouchTypeEvent.Direction.DOWN),
            result,
        )
    }
}
