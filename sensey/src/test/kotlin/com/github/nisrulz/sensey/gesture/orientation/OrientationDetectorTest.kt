
package com.github.nisrulz.sensey.gesture.orientation

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class OrientationDetectorTest {
    @Test
    fun dispatchesNothingWithOnlyAccelerometer() {
        val events = mutableListOf<OrientationEvent>()
        val detector = OrientationDetector(OrientationTrigger()) { events.add(it) }
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(1f, 2f, 3f)))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesNothingWithOnlyMagnetic() {
        val events = mutableListOf<OrientationEvent>()
        val detector = OrientationDetector(OrientationTrigger()) { events.add(it) }
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(1f, 2f, 3f),
                Sensor.TYPE_MAGNETIC_FIELD,
            ),
        )
        assertTrue(events.isEmpty())
    }
}
