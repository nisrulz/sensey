
package com.github.nisrulz.sensey.gesture.wristtwist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WristTwistTriggerTest {
    private val trigger = WristTwistTrigger(threshold = 15f, timeForWristTwistGesture = 1000L)

    @Test
    fun gestureStartedButNotCompletedWithThresholdValues() {
        assertNull(trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L))
    }

    @Test
    fun twistCompletedAfterTimeout() {
        trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L)
        assertEquals(WristTwistEvent.Twisted, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 2000L))
    }

    @Test
    fun noTwistBeforeTimeout() {
        trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventWithStableValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun noEventWhenMagnitudeBelowThreshold() {
        assertNull(trigger.evaluate(floatArrayOf(-10f, -2f, -20f), 0L))
    }
}
