
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CornerSwipeTriggerTest {
    private val allCornersTrigger =
        CornerSwipeTrigger(
            cornerRadiusPx = 48f,
            enabledCorners = TouchEvent.CornerType.entries.toSet(),
            screenW = 1080f,
            screenH = 1920f,
            minDistance = 40f,
        )

    @Test
    fun topLeftCornerSwipeDetected() {
        val result = allCornersTrigger.evaluate(startX = 10f, startY = 10f, endX = 200f, endY = 200f)
        assertTrue(result is TouchEvent.Swipe)
        val swipe = result as TouchEvent.Swipe
        assertTrue(swipe.origin is TouchEvent.SwipeOrigin.Corner)
        assertEquals(TouchEvent.CornerType.TOP_LEFT, (swipe.origin as TouchEvent.SwipeOrigin.Corner).type)
    }

    @Test
    fun topRightCornerSwipeDetected() {
        val result = allCornersTrigger.evaluate(startX = 1050f, startY = 10f, endX = 800f, endY = 200f)
        assertTrue(result is TouchEvent.Swipe)
        val swipe = result as TouchEvent.Swipe
        assertEquals(TouchEvent.CornerType.TOP_RIGHT, (swipe.origin as TouchEvent.SwipeOrigin.Corner).type)
    }

    @Test
    fun nonCornerStartReturnsNull() {
        assertNull(allCornersTrigger.evaluate(startX = 200f, startY = 200f, endX = 300f, endY = 300f))
    }

    @Test
    fun respectsEnabledCorners() {
        val topRightOnlyTrigger =
            CornerSwipeTrigger(
                cornerRadiusPx = 48f,
                enabledCorners = setOf(TouchEvent.CornerType.TOP_RIGHT),
                screenW = 1080f,
                screenH = 1920f,
                minDistance = 40f,
            )
        assertNull(
            "TOP_LEFT should be filtered when only TOP_RIGHT is enabled",
            topRightOnlyTrigger.evaluate(startX = 10f, startY = 10f, endX = 200f, endY = 200f),
        )
        assertNotNull(
            "TOP_RIGHT should be detected",
            topRightOnlyTrigger.evaluate(startX = 1050f, startY = 10f, endX = 800f, endY = 200f),
        )
    }
}
