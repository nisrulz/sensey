
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
        detector.onSensorChanged(
            SensorUtils.testSensorWithRange(
                floatArrayOf(10f),
                Sensor.TYPE_PROXIMITY,
                10f,
            ),
        )
        assertTrue(events.contains(ProximityEvent.Far))
    }

    @Test
    fun dispatchesNearWhenDistanceLessThanMaxRange() {
        val events = mutableListOf<ProximityEvent>()
        val detector = ProximityDetector(ProximityTrigger()) { events.add(it) }
        detector.onSensorChanged(
            SensorUtils.testSensorWithRange(
                floatArrayOf(1f),
                Sensor.TYPE_PROXIMITY,
                10f,
            ),
        )
        assertTrue(events.contains(ProximityEvent.Near))
    }
}
