package com.github.nisrulz.sensey.gesture.headshake

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HeadShakeTriggerTest {
    @Test
    fun firesOnPositiveThenNegativeZYaw() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -15f), 150L))
        assertEquals(HeadShakeEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun firesOnNegativeThenPositiveZYaw() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -15f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 150L))
        assertEquals(HeadShakeEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun firesOnYAxisRotation() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 15f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, -15f, 0f), 150L))
        assertEquals(HeadShakeEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun firesOnCombinedYZRotationTiltedDevice() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 8f, 8f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, -8f, -8f), 150L))
        assertEquals(HeadShakeEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
    }

    @Test
    fun noEventOnSmallRotation() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 2f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 2f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventOnTimeout() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 100L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 250L))
    }

    @Test
    fun blocksWithinCooldown() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L, cooldownMs = 1000L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -15f), 150L))
        assertEquals(HeadShakeEvent, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 200L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 15f), 300L))
    }

    @Test
    fun ignoresXAxisRotation() {
        val trigger = HeadShakeTrigger(angleThreshold = 30f, timeWindowMs = 500L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(15f, 0f, 0f), 50L))
        assertNull(trigger.evaluate(floatArrayOf(30f, 0f, 0f), 100L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }
}
