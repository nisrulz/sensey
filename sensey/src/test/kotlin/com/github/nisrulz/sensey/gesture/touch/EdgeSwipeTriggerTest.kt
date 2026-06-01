
package com.github.nisrulz.sensey.gesture.touch

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EdgeSwipeTriggerTest {
    private val allEdgesTrigger =
        EdgeSwipeTrigger(
            edgeThresholdPx = 48f,
            enabledEdges = TouchEvent.EdgeType.entries.toSet(),
            screenW = 1080f,
            screenH = 1920f,
            minDistance = 40f,
        )

    @Test
    fun leftEdgeSwipeDetected() {
        val result = allEdgesTrigger.evaluate(startX = 10f, startY = 200f, endX = 200f, endY = 220f)
        assertTrue(result is TouchEvent.Swipe)
        val swipe = result as TouchEvent.Swipe
        assertTrue(swipe.origin is TouchEvent.SwipeOrigin.Edge)
        assertEquals(TouchEvent.EdgeType.LEFT, (swipe.origin as TouchEvent.SwipeOrigin.Edge).type)
    }

    @Test
    fun rightEdgeSwipeDetected() {
        val result = allEdgesTrigger.evaluate(startX = 1050f, startY = 200f, endX = 800f, endY = 220f)
        assertTrue(result is TouchEvent.Swipe)
        val swipe = result as TouchEvent.Swipe
        assertEquals(TouchEvent.EdgeType.RIGHT, (swipe.origin as TouchEvent.SwipeOrigin.Edge).type)
    }

    @Test
    fun topEdgeSwipeDetected() {
        val result = allEdgesTrigger.evaluate(startX = 200f, startY = 10f, endX = 300f, endY = 300f)
        assertTrue(result is TouchEvent.Swipe)
        assertEquals(TouchEvent.EdgeType.TOP, ((result as TouchEvent.Swipe).origin as TouchEvent.SwipeOrigin.Edge).type)
    }

    @Test
    fun bottomEdgeSwipeDetected() {
        val result = allEdgesTrigger.evaluate(startX = 200f, startY = 1900f, endX = 300f, endY = 1000f)
        assertTrue(result is TouchEvent.Swipe)
        assertEquals(
            TouchEvent.EdgeType.BOTTOM,
            ((result as TouchEvent.Swipe).origin as TouchEvent.SwipeOrigin.Edge).type,
        )
    }

    @Test
    fun nonEdgeStartReturnsNull() {
        assertNull(allEdgesTrigger.evaluate(startX = 200f, startY = 200f, endX = 300f, endY = 300f))
    }

    @Test
    fun respectsEnabledEdges() {
        val rightOnlyTrigger =
            EdgeSwipeTrigger(
                edgeThresholdPx = 48f,
                enabledEdges = setOf(TouchEvent.EdgeType.RIGHT),
                screenW = 1080f,
                screenH = 1920f,
                minDistance = 40f,
            )
        assertNull(
            "LEFT edge should be filtered when only RIGHT is enabled",
            rightOnlyTrigger.evaluate(startX = 10f, startY = 200f, endX = 200f, endY = 220f),
        )
        assertNotNull(
            "RIGHT edge should be detected",
            rightOnlyTrigger.evaluate(startX = 1050f, startY = 200f, endX = 800f, endY = 220f),
        )
    }
}
