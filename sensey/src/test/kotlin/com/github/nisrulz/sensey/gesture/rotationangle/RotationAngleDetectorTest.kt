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
package com.github.nisrulz.sensey.gesture.rotationangle

import com.github.nisrulz.sensey.contract.GestureTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RotationAngleDetectorTest {

    @Test
    fun dispatchesNothingOnEmptyEvents() {
        val events = mutableListOf<RotationAngleEvent>()
        val alwaysNullTrigger = object : GestureTrigger<RotationAngleEvent> {
            override fun evaluate(values: FloatArray, timestamp: Long) = null
        }
        val detector = RotationAngleDetector(alwaysNullTrigger) { events.add(it) }
        assertTrue(events.isEmpty())
    }

    @Test
    fun triggerEvaluatesValues() {
        val trigger = RotationAngleTrigger()
        val result = trigger.evaluate(floatArrayOf(45f, 30f, 90f), 0L)
        assertEquals(RotationAngleEvent(45f, 30f, 90f), result)
    }

    @Test
    fun triggerEvaluatesZeroValues() {
        val trigger = RotationAngleTrigger()
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(RotationAngleEvent(0f, 0f, 0f), result)
    }
}
