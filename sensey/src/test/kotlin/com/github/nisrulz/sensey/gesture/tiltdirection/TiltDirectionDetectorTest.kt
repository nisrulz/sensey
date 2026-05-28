
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
