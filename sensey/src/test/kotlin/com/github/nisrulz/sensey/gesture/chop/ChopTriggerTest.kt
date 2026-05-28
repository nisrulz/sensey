
package com.github.nisrulz.sensey.gesture.chop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChopTriggerTest {
    private val trigger = ChopTrigger(threshold = 35f, timeForChopGesture = 700L)

    @Test
    fun gestureStartedButNotCompletedWithThresholdValues() {
        assertNull(trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L))
    }

    @Test
    fun choppingCompletedAfterTimeout() {
        trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L)
        assertEquals(ChopEvent.Chopped, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 1000L))
    }

    @Test
    fun noChopBeforeTimeout() {
        trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventWithStableValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun noEventWithPartialCondition() {
        assertNull(trigger.evaluate(floatArrayOf(40f, -5f, 40f), 0L))
    }
}
