/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.sensey.gesture.wave

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WaveTriggerTest {
    private val trigger = WaveTrigger(timeWindowMillis = 1000f, debounceMillis = 1000L)

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
        val customTrigger = WaveTrigger(timeWindowMillis = 500f)
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
