
package com.github.nisrulz.sensey.gesture.wave

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class WaveDetectorTest {
    @Test
    fun dispatchesWavedOnNearThenFarSequence() {
        val events = mutableListOf<WaveEvent>()
        val detector =
            TypedSensorDetector(
                WaveTrigger(minNearDurationMs = 0L),
                dispatcher = { events.add(it) },
                Sensor.TYPE_PROXIMITY,
            )
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(0f), Sensor.TYPE_PROXIMITY))
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(5f), Sensor.TYPE_PROXIMITY))
        assertTrue(events.contains(WaveEvent.Waved))
    }

    @Test
    fun dispatchesNothingOnFirstReading() {
        val events = mutableListOf<WaveEvent>()
        val detector = TypedSensorDetector(WaveTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_PROXIMITY)
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(5f), Sensor.TYPE_PROXIMITY))
        assertTrue(events.isEmpty())
    }
}
