
package com.github.nisrulz.sensey.gesture.wave

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WaveTriggerTest {
    private val trigger = WaveTrigger(timeWindowMillis = 1000L, debounceMillis = 1000L)

    @Test
    fun noEventOnFirstFarReading() {
        assertNull(trigger.evaluate(floatArrayOf(5f), 0L))
    }

    @Test
    fun waveDetectedWhenNearThenFarWithinThreshold() {
        trigger.evaluate(floatArrayOf(0f), 0L)
        assertEquals(WaveEvent.Waved, trigger.evaluate(floatArrayOf(5f), 500L))
    }

    @Test
    fun noWaveWhenFarThenFar() {
        trigger.evaluate(floatArrayOf(5f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(10f), 500L))
    }

    @Test
    fun noWaveWhenNearThenFarExceedsThreshold() {
        trigger.evaluate(floatArrayOf(0f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(5f), 2000L))
    }

    @Test
    fun waveDetectedWithCustomThreshold() {
        val customTrigger = WaveTrigger(timeWindowMillis = 500L)
        customTrigger.evaluate(floatArrayOf(0f), 0L)
        assertEquals(WaveEvent.Waved, customTrigger.evaluate(floatArrayOf(5f), 300L))
    }

    @Test
    fun waveDebouncePreventsTooRapidWaves() {
        trigger.evaluate(floatArrayOf(0f), 0L)
        trigger.evaluate(floatArrayOf(5f), 500L)
        trigger.evaluate(floatArrayOf(0f), 1000L)
        assertNull(trigger.evaluate(floatArrayOf(5f), 1100L))
    }

    @Test
    fun secondWaveWorksAfterDebouncePeriod() {
        trigger.evaluate(floatArrayOf(0f), 0L)
        trigger.evaluate(floatArrayOf(5f), 500L)
        trigger.evaluate(floatArrayOf(0f), 2000L)
        assertEquals(WaveEvent.Waved, trigger.evaluate(floatArrayOf(5f), 2500L))
    }
}
