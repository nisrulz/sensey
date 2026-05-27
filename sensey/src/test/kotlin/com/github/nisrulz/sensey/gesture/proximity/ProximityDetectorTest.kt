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
package com.github.nisrulz.sensey.gesture.proximity

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class ProximityDetectorTest {

    @Test
    fun dispatchesFarWhenDistanceEqualsMaxRange() {
        val events = mutableListOf<ProximityEvent>()
        val detector = ProximityDetector(ProximityTrigger()) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testSensorWithRange(
            floatArrayOf(10f), Sensor.TYPE_PROXIMITY, 10f,
        ))
        assertTrue(events.contains(ProximityEvent.Far))
    }

    @Test
    fun dispatchesNearWhenDistanceLessThanMaxRange() {
        val events = mutableListOf<ProximityEvent>()
        val detector = ProximityDetector(ProximityTrigger()) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testSensorWithRange(
            floatArrayOf(1f), Sensor.TYPE_PROXIMITY, 10f,
        ))
        assertTrue(events.contains(ProximityEvent.Near))
    }
}
