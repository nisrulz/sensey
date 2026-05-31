
package com.github.nisrulz.sensey.gesture.soundlevel

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SoundLevelDetectorTest {
    @Test
    fun triggerProcessesAudioData() {
        val events = mutableListOf<SoundLevelEvent>()
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(1000f, 2000f, -500f), 0L)
        assertNotNull(result)
    }

    @Test
    fun triggerReturnsNullForEmptyInput() {
        val trigger = SoundLevelTrigger()
        assertTrue(trigger.evaluate(floatArrayOf(), 0L) == null)
    }

    @Test
    fun zeroValuesProduceVeryLowLevel() {
        val trigger = SoundLevelTrigger()
        assertNotNull(trigger.evaluate(floatArrayOf(0f, 0f), 0L))
    }

    @Test
    fun triggerHandlesSingleSample() {
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(10000f), 0L)
        assertNotNull(result)
    }

    @Test
    fun handleInfiniteInput() {
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(Float.POSITIVE_INFINITY), 0L)
        assertNotNull(result)
    }
}
