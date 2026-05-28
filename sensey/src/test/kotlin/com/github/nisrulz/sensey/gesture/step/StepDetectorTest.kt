
package com.github.nisrulz.sensey.gesture.step

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import com.github.nisrulz.sensey.TypedSensorDetector
import org.junit.Assert.assertTrue
import org.junit.Test

class StepDetectorTest {
    @Test
    fun postKitKatDetectorDispatchesStepEvent() {
        val events = mutableListOf<StepEvent>()
        val trigger = StepTrigger(gender = StepDetectorUtil.MALE)
        val detector = TypedSensorDetector(trigger, dispatcher = { events.add(it) }, Sensor.TYPE_STEP_COUNTER)
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(10f),
                Sensor.TYPE_STEP_COUNTER,
            ),
        )
        detector.onSensorChanged(
            SensorUtils.testSensorEvent(
                floatArrayOf(15f),
                Sensor.TYPE_STEP_COUNTER,
            ),
        )
        assertTrue(events.isNotEmpty())
    }
}
