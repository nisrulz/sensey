
package com.github.nisrulz.sensey.gesture.shake

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class ShakeDetectorTest {
    @Test
    fun dispatchesShakeDetectedOnAccelerometerEvent() {
        val events = mutableListOf<ShakeEvent>()
        val detector = TypedSensorDetector(ShakeTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 2 * 9.81f)))
        assertTrue(events.contains(ShakeEvent.Detected))
    }

    @Test
    fun dispatchesNothingOnStableValues() {
        val events = mutableListOf<ShakeEvent>()
        val detector = TypedSensorDetector(ShakeTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        assertTrue(events.isEmpty())
    }
}
