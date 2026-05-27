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
package com.github.nisrulz.sensey.gesture.pickupdevice

import com.github.nisrulz.sensey.SensorUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class PickupDeviceDetectorTest {

    @Test
    fun dispatchesPickedUpFromStableToUnstable() {
        val events = mutableListOf<PickupDeviceEvent>()
        val detector = PickupDeviceDetector(
            PickupDeviceTrigger(windowSize = 4, settleReadings = 3),
        ) { events.add(it) }

        // 3 stable readings to fill buffer
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.82f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.80f)))
        // 4th reading creates spread > movingRange
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(5f, 0f, 5f)))
        assertTrue(events.contains(PickupDeviceEvent.PickedUp))
    }
}
