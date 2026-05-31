
package com.github.nisrulz.sensey.gesture.scoop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScoopTriggerTest {
    @Test
    fun scoopedWhenValuesExceedThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertEquals(ScoopEvent.Scooped, trigger.evaluate(floatArrayOf(20f, -20f, 20f), 0L))
    }

    @Test
    fun notScoopedWhenBelowThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun scoopedWhenPartialConditionMet() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertEquals(ScoopEvent.Scooped, trigger.evaluate(floatArrayOf(20f, -5f, 20f), 0L))
    }

    @Test
    fun scoopedWithCustomThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 5f)
        assertEquals(ScoopEvent.Scooped, trigger.evaluate(floatArrayOf(10f, -10f, 10f), 0L))
    }
}
