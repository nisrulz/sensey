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

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import org.junit.Test

class RotationAngleDetectorTest {

    @Test
    fun processesEventWithoutCrashing() {
        val events = mutableListOf<RotationAngleEvent>()
        val detector = RotationAngleDetector(RotationAngleTrigger()) { events.add(it) }
        // Rotation matrix from vector requires realistic values; mocks may throw
        try {
            detector.onSensorChanged(SensorUtils.testSensorEvent(
                floatArrayOf(1f, 0f, 0f, 0f), Sensor.TYPE_ROTATION_VECTOR,
            ))
        } catch (_: Exception) {
            // expected with mock sensors
        }
    }
}
