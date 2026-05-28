
package com.github.nisrulz.sensey.gesture.flip

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FlipTriggerTest {
    private val trigger = FlipTrigger()

    @Test
    fun faceUpWithMiddleValue() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
    }

    @Test
    fun faceDownWithMiddleValue() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 0L))
    }

    @Test
    fun notDetectFlipWithOtherValue() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun notDetectFlipWithMaxFaceUp() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 11f), 0L))
    }

    @Test
    fun notDetectFlipWithMinFaceUp() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 7f), 0L))
    }

    @Test
    fun notDetectFlipWithMaxFaceDown() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -11f), 0L))
    }

    @Test
    fun notDetectFlipWithMinFaceDown() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -7f), 0L))
    }

    @Test
    fun faceUpAtWideBoundary() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 8.5f), 0L))
    }

    @Test
    fun faceDownAtWideBoundary() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -8.5f), 0L))
    }

    @Test
    fun faceUpOnlyOnceUntilReset() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 100L))
    }

    @Test
    fun faceDownOnlyOnceUntilNewOrientation() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 100L))
    }

    @Test
    fun faceUpThenFaceDownWorks() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 200L))
    }
}
