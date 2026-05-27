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
package com.github.nisrulz.sensey.gesture.wristtwist

import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.contract.GestureTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WristTwistDetectorTest {

    @Test
    fun dispatchesNothingOnStableValues() {
        val events = mutableListOf<WristTwistEvent>()
        val detector = WristTwistDetector(WristTwistTrigger()) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesNothingOnPartialCondition() {
        val events = mutableListOf<WristTwistEvent>()
        val detector = WristTwistDetector(WristTwistTrigger()) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(-5f, -2f, -20f)))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesEventWhenTriggerReturnsNonNull() {
        val events = mutableListOf<WristTwistEvent>()
        val alwaysTrigger = object : GestureTrigger<WristTwistEvent> {
            override fun evaluate(values: FloatArray, timestamp: Long) = WristTwistEvent.Twisted
        }
        val detector = WristTwistDetector(alwaysTrigger) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertEquals(listOf(WristTwistEvent.Twisted), events)
    }
}
