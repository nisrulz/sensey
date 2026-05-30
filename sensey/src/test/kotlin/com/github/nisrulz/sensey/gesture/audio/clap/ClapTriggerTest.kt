
package com.github.nisrulz.sensey.gesture.audio.clap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ClapTriggerTest {
    @Test
    fun noEventOnSilence() {
        val trigger = ClapTrigger()
        assertNull(trigger.evaluate(FloatArray(128), 0L))
    }

    @Test
    fun noEventOnSustainedNoise() {
        val trigger = ClapTrigger()
        // Both buffers equally loud — no rise
        assertNull(trigger.evaluate(FloatArray(128) { 10000f }, 0L))
        assertNull(trigger.evaluate(FloatArray(128) { 10000f }, 100L))
    }

    @Test
    fun clappedOnSharpRise() {
        val trigger = ClapTrigger(thresholdDb = -20f, riseDb = 10f)
        // Buffer 1: quiet baseline (~150 RMS → -47 dBFS)
        assertNull(trigger.evaluate(FloatArray(128) { 150f }, 0L))
        // Buffer 2: loud clap (~30000 RMS → -0.77 dBFS, rise >20 dB)
        assertEquals(ClapEvent.Clapped, trigger.evaluate(FloatArray(128) { 30000f }, 100L))
    }

    @Test
    fun noEventOnGradualRise() {
        val trigger = ClapTrigger(thresholdDb = -40f, riseDb = 5f)
        // Both buffers above threshold but rise from -30 to -26 is only 4 dB — below 5 dB
        assertNull(trigger.evaluate(FloatArray(128) { 1000f }, 0L)) // ~-30 dBFS
        assertNull(trigger.evaluate(FloatArray(128) { 1600f }, 100L)) // ~-26 dBFS (4 dB rise)
    }
}
