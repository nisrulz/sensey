package com.github.nisrulz.sensey.gesture.nodgesture

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NodGestureTriggerTest {
    @Test
    fun firesOnPositiveThenNegativeXPitch() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(-15f, 0f, 0f), 150L))
        assertEquals(NodGestureEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun firesOnNegativeThenPositiveXPitch() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(-15f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 150L))
        assertEquals(NodGestureEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun noEventOnSmallRotation() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(2f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(2f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventOnTimeout() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 100L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 250L))
    }

    @Test
    fun blocksWithinCooldown() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 500L, cooldownMs = 1000L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(-15f, 0f, 0f), 150L))
        assertEquals(NodGestureEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 300L))
    }

    @Test
    fun ignoresYAndZAxisRotation() {
        val trigger = NodGestureTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 15f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }
}
