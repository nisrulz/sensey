
package com.github.nisrulz.sensey.gesture.movement

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class MovementDetectorTest {
    @Test
    fun dispatchesMovedOnAccelerationChange() {
        val events = mutableListOf<MovementEvent>()
        val detector =
            TypedSensorDetector(
                MovementTrigger(),
                dispatcher = { events.add(it) },
                Sensor.TYPE_ACCELEROMETER,
            )
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertTrue(events.any { it is MovementEvent.Moved })
    }

    @Test
    fun dispatchesNothingOnStableGravity() {
        val events = mutableListOf<MovementEvent>()
        val detector =
            TypedSensorDetector(
                MovementTrigger(),
                dispatcher = { events.add(it) },
                Sensor.TYPE_ACCELEROMETER,
            )
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.81f)))
        assertTrue(events.isEmpty())
    }
}
