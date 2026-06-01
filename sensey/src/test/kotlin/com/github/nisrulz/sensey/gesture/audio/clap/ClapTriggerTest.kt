
package com.github.nisrulz.sensey.gesture.audio.clap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ClapTriggerTest {
    private fun noiseLike(amplitude: Float): FloatArray =
        FloatArray(128) { i -> if (i % 2 == 0) amplitude else -amplitude }

    private fun quiet(amplitude: Float = 200f): FloatArray = FloatArray(128) { amplitude }

    @Test
    fun noEventOnSilence() {
        val trigger = ClapTrigger()
        assertNull(trigger.evaluate(FloatArray(128), 0L))
    }

    @Test
    fun clappedOnLoudBuffer() {
        val trigger = ClapTrigger(thresholdDb = -25f, requiredClaps = 1)
        assertEquals(ClapEvent.Clapped, trigger.evaluate(noiseLike(25000f), 0L))
    }

    @Test
    fun doubleClapWithinTimeframe() {
        val trigger = ClapTrigger(thresholdDb = -25f)
        // First clap counted, second fires within 800ms window
        assertNull(trigger.evaluate(noiseLike(25000f), 0L))
        assertEquals(ClapEvent.Clapped, trigger.evaluate(noiseLike(25000f), 400L))
    }

    @Test
    fun debounceSuppressesRetrigger() {
        val trigger = ClapTrigger(thresholdDb = -25f, requiredClaps = 1, debounceMs = 400L)
        assertEquals(ClapEvent.Clapped, trigger.evaluate(noiseLike(25000f), 0L))
        assertNull("Suppressed by cooldown", trigger.evaluate(noiseLike(25000f), 200L))
        assertEquals(ClapEvent.Clapped, trigger.evaluate(noiseLike(25000f), 500L))
    }

    @Test
    fun noiseFloorRisesAndSuppressesSustainedNoise() {
        val trigger = ClapTrigger(thresholdDb = -30f, requiredClaps = 1)
        repeat(40) { i ->
            trigger.evaluate(noiseLike(15000f), 100L * i)
        }
        assertNull("Noise floor raised threshold", trigger.evaluate(noiseLike(15000f), 4100L))
    }
}
