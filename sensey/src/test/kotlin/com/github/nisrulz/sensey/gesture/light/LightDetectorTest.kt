
package com.github.nisrulz.sensey.gesture.light

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class LightDetectorTest {
    @Test
    fun dispatchesDarkOnLowLux() {
        val events = mutableListOf<LightEvent>()
        val detector = TypedSensorDetector(LightTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_LIGHT)
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(1f), Sensor.TYPE_LIGHT))
        assertTrue(events.contains(LightEvent.Dark))
    }

    @Test
    fun dispatchesLightOnHighLux() {
        val events = mutableListOf<LightEvent>()
        val detector = TypedSensorDetector(LightTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_LIGHT)
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(10f), Sensor.TYPE_LIGHT))
        assertTrue(events.contains(LightEvent.Light))
    }
}
