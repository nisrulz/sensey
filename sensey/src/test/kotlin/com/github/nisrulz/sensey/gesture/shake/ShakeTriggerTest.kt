
package com.github.nisrulz.sensey.gesture.shake

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ShakeTriggerTest {
    private val trigger =
        ShakeTrigger(
            threshold = 3f,
            timeBeforeDeclaringShakeStopped = 1000L,
        )

    @Test
    fun noEventWhenValuesAreStable() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 10L)
        assertNull(result)
    }

    @Test
    fun noEventWithZeroGravityForCustomThreshold() {
        val trigger = ShakeTrigger(threshold = 10f)
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 10L)
        assertNull(result)
    }

    @Test
    fun shakeDetectedWhenAccelerationExceedsThreshold() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 2 * 9.81f),
                timestamp = 10L,
            )
        assertEquals(ShakeEvent.Detected, result)
    }

    @Test
    fun shakeDetectedWithDoubleGravityForCustomThreshold() {
        val trigger = ShakeTrigger(threshold = 9f)
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 2 * 9.81f),
                timestamp = 10L,
            )
        assertEquals(ShakeEvent.Detected, result)
    }

    @Test
    fun shakeDetectedMultipleTimesWithStrongAcceleration() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result1 =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 2 * 9.81f),
                timestamp = 10L,
            )
        assertEquals(ShakeEvent.Detected, result1)

        val result2 =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 2 * -9.81f),
                timestamp = 50L,
            )
        assertEquals(ShakeEvent.Detected, result2)
    }

    @Test
    fun shakeStoppedAfterTimeoutWithoutAcceleration() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)

        val result =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 0f),
                timestamp = 2000L,
            )
        assertEquals(ShakeEvent.Stopped, result)
    }

    @Test
    fun noStopEventBeforeTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)

        val result =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 0f),
                timestamp = 500L,
            )
        assertNull(result)
    }

    @Test
    fun shakeDetectedThenStoppedThenDetectedAgain() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), timestamp = 2000L)

        val result =
            trigger.evaluate(
                floatArrayOf(0f, 0f, 2 * -9.81f),
                timestamp = 3000L,
            )
        assertNotNull(result)
    }
}
