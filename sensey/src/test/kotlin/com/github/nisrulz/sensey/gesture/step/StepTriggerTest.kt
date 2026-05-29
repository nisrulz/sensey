
package com.github.nisrulz.sensey.gesture.step

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StepTriggerTest {
    @Test
    fun stepCounterRegistersBaseValue() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        // First call sets baseline, returns null
        assertEquals(null, trigger.evaluate(floatArrayOf(10f), 0L))
    }

    @Test
    fun stepCounterCalculatesSteps() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        trigger.evaluate(floatArrayOf(10f), 0L) // set baseline
        val result = trigger.evaluate(floatArrayOf(15f), 1000L)
        assertEquals(5, result?.steps)
    }

    @Test
    fun accelerometerDetectsStepOnMagnitudeChange() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        // No change from initial 0 → no step
        assertEquals(null, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun accelerometerDetectsStepsOnSignificantMagnitudeChange() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L) // set baseline
        val result = trigger.evaluate(floatArrayOf(5f, 0f, 0f), 1000L)
        assertTrue((result?.steps ?: 0) > 0)
    }
}
