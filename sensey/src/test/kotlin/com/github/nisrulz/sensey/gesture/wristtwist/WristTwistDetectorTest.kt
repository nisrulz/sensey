
package com.github.nisrulz.sensey.gesture.wristtwist

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WristTwistDetectorTest {
    @Test
    fun dispatchesNothingOnStableValues() {
        val events = mutableListOf<WristTwistEvent>()
        val detector =
            TypedSensorDetector(WristTwistTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesNothingOnPartialCondition() {
        val events = mutableListOf<WristTwistEvent>()
        val detector =
            TypedSensorDetector(WristTwistTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(-5f, -2f, -20f)))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesEventWhenTriggerReturnsNonNull() {
        val events = mutableListOf<WristTwistEvent>()
        val alwaysTrigger =
            object : GestureTrigger<WristTwistEvent> {
                override fun evaluate(
                    values: FloatArray,
                    timestamp: Long,
                ) = WristTwistEvent.Twisted
            }
        val detector = TypedSensorDetector(alwaysTrigger, dispatcher = { events.add(it) }, Sensor.TYPE_ACCELEROMETER)
        detector.onSensorChanged(SensorUtils.testAccelerometerEvent(floatArrayOf(0f, 0f, 0f)))
        assertEquals(listOf(WristTwistEvent.Twisted), events)
    }
}
