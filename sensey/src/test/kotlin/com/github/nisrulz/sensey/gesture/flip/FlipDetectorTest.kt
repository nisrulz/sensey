
package com.github.nisrulz.sensey.gesture.flip

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class FlipDetectorTest {
    @Test
    fun dispatchesFaceDownOnAccelerometerEvent() {
        val events = mutableListOf<FlipEvent>()
        val detector = TypedSensorDetector(FlipTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, -9.5f)))
        assertTrue(events.contains(FlipEvent.FaceDown))
    }

    @Test
    fun dispatchesFaceUpOnAccelerometerEvent() {
        val events = mutableListOf<FlipEvent>()
        val detector = TypedSensorDetector(FlipTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 9.5f)))
        assertTrue(events.contains(FlipEvent.FaceUp))
    }
}
