
package com.github.nisrulz.sensey.gesture.pickupdevice

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class PickupDeviceDetectorTest {
    @Test
    fun dispatchesPickedUpFromStableToUnstable() {
        val events = mutableListOf<PickupDeviceEvent>()
        val detector =
            TypedSensorDetector(
                PickupDeviceTrigger(windowSize = 4, settleTimeMs = 0L),
                dispatcher = { events.add(it) },
                Sensor.TYPE_ACCELEROMETER,
            )

        // 3 stable readings to fill buffer
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.82f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.80f)))
        // 4th reading creates spread > movingRange
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(5f, 0f, 5f)))
        assertTrue(events.contains(PickupDeviceEvent.PickedUp))
    }
}
