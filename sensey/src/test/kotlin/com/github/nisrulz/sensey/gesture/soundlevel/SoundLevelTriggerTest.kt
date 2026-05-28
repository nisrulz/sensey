
package com.github.nisrulz.sensey.gesture.soundlevel

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SoundLevelTriggerTest {
    private val trigger = SoundLevelTrigger(offset = 100f)

    @Test
    fun detectsSoundWithNormalValues() {
        val result = trigger.evaluate(floatArrayOf(1000f, 2000f, -500f), 0L)
        assertNotNull(result)
    }

    @Test
    fun nullForEmptyInput() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }

    @Test
    fun zeroValuesProduceVeryLowLevel() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f), 0L)
        assertNotNull(result)
    }

    @Test
    fun handlesSingleSample() {
        val result = trigger.evaluate(floatArrayOf(10000f), 0L)
        assertNotNull(result)
    }
}
