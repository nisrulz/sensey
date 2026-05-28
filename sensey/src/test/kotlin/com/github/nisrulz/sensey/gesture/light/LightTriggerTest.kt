
package com.github.nisrulz.sensey.gesture.light

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LightTriggerTest {
    @Test
    fun darkWhenLuxBelowDefaultThreshold() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Dark, trigger.evaluate(floatArrayOf(1f), 0L))
    }

    @Test
    fun lightWithExtraValues() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(10f, 0f, 43f, 3f, -423f), 0L))
    }

    @Test
    fun lightWhenLuxAboveDefaultThreshold() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(10f), 0L))
    }

    @Test
    fun darkWithCustomThreshold() {
        val trigger = LightTrigger(darkThreshold = 5f, lightThreshold = 10f)
        assertEquals(LightEvent.Dark, trigger.evaluate(floatArrayOf(3f), 0L))
    }

    @Test
    fun hysteresisPreventsOscillation() {
        val trigger = LightTrigger(darkThreshold = 8f, lightThreshold = 12f)
        trigger.evaluate(floatArrayOf(1f), 0L) // Dark
        assertNull(trigger.evaluate(floatArrayOf(10f), 100L)) // Hysteresis: stays Dark
    }

    @Test
    fun transitionToLightAfterExceedingLightThreshold() {
        val trigger = LightTrigger(darkThreshold = 8f, lightThreshold = 12f)
        trigger.evaluate(floatArrayOf(1f), 0L) // Dark
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(15f), 100L))
    }

    @Test
    fun transitionToDarkAfterDroppingBelowDarkThreshold() {
        val trigger = LightTrigger(darkThreshold = 8f, lightThreshold = 12f)
        trigger.evaluate(floatArrayOf(15f), 0L) // Light
        assertEquals(LightEvent.Dark, trigger.evaluate(floatArrayOf(1f), 100L))
    }
}
