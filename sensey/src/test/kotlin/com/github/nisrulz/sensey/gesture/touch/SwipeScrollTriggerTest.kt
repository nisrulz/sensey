
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SwipeScrollTriggerTest {
    private val trigger = SwipeScrollTrigger(minDistance = 40f, velocityThreshold = 200f)

    @Test
    fun swipeWhenVelocityAboveThreshold() {
        val result = trigger.evaluate(deltaX = 100f, deltaY = 0f, velocityX = 300f, velocityY = 0f)
        assertTrue(result is TouchEvent.Swipe)
        assertEquals(TouchEvent.Direction.RIGHT, (result as TouchEvent.Swipe).direction)
    }

    @Test
    fun scrollWhenVelocityBelowThreshold() {
        val result = trigger.evaluate(deltaX = 100f, deltaY = 0f, velocityX = 50f, velocityY = 0f)
        assertTrue(result is TouchEvent.Scroll)
        assertEquals(TouchEvent.Direction.RIGHT, (result as TouchEvent.Scroll).direction)
    }

    @Test
    fun belowMinDistanceReturnsNull() {
        assertNull(trigger.evaluate(deltaX = 5f, deltaY = 5f, velocityX = 300f, velocityY = 0f))
    }

    @Test
    fun diagonalOnlyFiltersNonDiagonal() {
        val diagTrigger = SwipeScrollTrigger(minDistance = 40f, diagonalOnly = true)
        assertNull(diagTrigger.evaluate(deltaX = 100f, deltaY = 0f, velocityX = 300f, velocityY = 0f))
        assertNotNull(diagTrigger.evaluate(deltaX = 100f, deltaY = 100f, velocityX = 300f, velocityY = 300f))
    }
}
