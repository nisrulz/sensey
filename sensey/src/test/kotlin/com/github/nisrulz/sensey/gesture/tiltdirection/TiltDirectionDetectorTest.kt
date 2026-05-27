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
package com.github.nisrulz.sensey.gesture.tiltdirection

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class TiltDirectionDetectorTest {
    @Test
    fun dispatchesXAxisOnGyroscopeEvent() {
        val events = mutableListOf<TiltDirectionEvent>()
        val detector =
            TypedSensorDetector(TiltDirectionTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_GYROSCOPE)
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(1f, 0f, 0f),
                Sensor.TYPE_GYROSCOPE,
            ),
        )
        assertTrue(events.contains(TiltDirectionEvent.AxisXTilt(TiltDirectionEvent.Direction.ANTICLOCKWISE)))
    }

    @Test
    fun dispatchesNothingBelowThreshold() {
        val events = mutableListOf<TiltDirectionEvent>()
        val detector =
            TypedSensorDetector(TiltDirectionTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_GYROSCOPE)
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(0.1f, 0.1f, 0.1f),
                Sensor.TYPE_GYROSCOPE,
            ),
        )
        assertTrue(events.isEmpty())
    }
}
