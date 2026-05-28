
package com.github.nisrulz.sensey.gesture.scoop

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class ScoopDetectorTest {
    @Test
    fun dispatchesScoopedWhenValuesExceedThreshold() {
        val events = mutableListOf<ScoopEvent>()
        val detector = TypedSensorDetector(ScoopTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(20f, -20f, 20f)))
        assertTrue(events.contains(ScoopEvent.Scooped))
    }

    @Test
    fun dispatchesNothingOnStableValues() {
        val events = mutableListOf<ScoopEvent>()
        val detector = TypedSensorDetector(ScoopTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertTrue(events.isEmpty())
    }
}
