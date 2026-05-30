
package com.github.nisrulz.sensey.gesture.chop

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChopDetectorTest {
    @Test
    fun dispatchesNothingOnStableValues() {
        val events = mutableListOf<ChopEvent>()
        val detector =
            TypedSensorDetector(ChopTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_LINEAR_ACCELERATION)
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(0f, 0f, 0f), Sensor.TYPE_LINEAR_ACCELERATION))
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesNothingOnPartialCondition() {
        val events = mutableListOf<ChopEvent>()
        val detector =
            TypedSensorDetector(ChopTrigger(), dispatcher = { events.add(it) }, Sensor.TYPE_LINEAR_ACCELERATION)
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(floatArrayOf(40f, -5f, 40f), Sensor.TYPE_LINEAR_ACCELERATION),
        )
        assertTrue(events.isEmpty())
    }

    @Test
    fun dispatchesEventWhenTriggerReturnsNonNull() {
        val events = mutableListOf<ChopEvent>()
        val alwaysTrigger =
            object : GestureTrigger<ChopEvent> {
                override fun evaluate(
                    values: FloatArray,
                    timestamp: Long,
                ) = ChopEvent.Chopped
            }
        val detector =
            TypedSensorDetector(alwaysTrigger, dispatcher = { events.add(it) }, Sensor.TYPE_LINEAR_ACCELERATION)
        detector.onSensorChanged(SensorUtils.testSensorEvent(floatArrayOf(0f, 0f, 0f), Sensor.TYPE_LINEAR_ACCELERATION))
        assertEquals(listOf(ChopEvent.Chopped), events)
    }
}
