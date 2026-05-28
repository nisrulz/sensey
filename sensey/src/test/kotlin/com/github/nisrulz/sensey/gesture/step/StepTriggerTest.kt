
package com.github.nisrulz.sensey.gesture.step

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StepTriggerTest {
    @Test
    fun stepCounterRegistersBaseValue() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        val result = trigger.evaluate(floatArrayOf(10f), 0L)
        assertEquals(0, result?.steps)
    }

    @Test
    fun stepCounterCalculatesSteps() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        trigger.evaluate(floatArrayOf(10f), 0L)
        val result = trigger.evaluate(floatArrayOf(15f), 1000L)
        assertEquals(5, result?.steps)
    }

    @Test
    fun accelerometerDetectsStepOnMagnitudeChange() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(0, result?.steps)
    }

    @Test
    fun accelerometerDetectsStepsOnSignificantMagnitudeChange() {
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        val result = trigger.evaluate(floatArrayOf(5f, 0f, 0f), 1000L)
        assertTrue((result?.steps ?: 0) > 0)
    }
}
