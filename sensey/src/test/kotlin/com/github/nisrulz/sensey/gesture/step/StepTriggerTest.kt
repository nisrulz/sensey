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
