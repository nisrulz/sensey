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

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class StepDetectorTest {
    @Test
    fun postKitKatDetectorDispatchesStepEvent() {
        val events = mutableListOf<StepEvent>()
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        val detector = TypedSensorDetector(trigger, dispatcher = { events.add(it) }, Sensor.TYPE_STEP_COUNTER)
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(10f),
                Sensor.TYPE_STEP_COUNTER,
            ),
        )
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(15f),
                Sensor.TYPE_STEP_COUNTER,
            ),
        )
        assertTrue(events.isNotEmpty())
    }
}
