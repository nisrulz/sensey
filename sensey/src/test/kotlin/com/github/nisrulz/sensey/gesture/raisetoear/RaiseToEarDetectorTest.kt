
package com.github.nisrulz.sensey.gesture.raisetoear

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class RaiseToEarDetectorTest {
    @Test
    fun dispatchesAtEarWhenNearAndUpright() {
        val events = mutableListOf<RaiseToEarEvent>()
        val detector = RaiseToEarDetector(RaiseToEarTrigger(), dispatcher = { events.add(it) })

        // Gravity along -Y (phone upright, not flat)
        val gravityEvent = SensorUtils.testSensorEvent(floatArrayOf(0f, -9.81f, 0f), Sensor.TYPE_GRAVITY)
        gravityEvent.timestamp = 0L
        detector.onSensorChanged(gravityEvent)

        // Proximity near
        val proximityEvent = SensorUtils.testSensorEvent(floatArrayOf(1f), Sensor.TYPE_PROXIMITY)
        proximityEvent.timestamp = 1_000_000_000L
        detector.onSensorChanged(proximityEvent)

        assertTrue(events.contains(RaiseToEarEvent.AtEar))
    }
}
