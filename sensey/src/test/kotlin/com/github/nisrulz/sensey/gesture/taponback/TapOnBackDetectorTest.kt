
package com.github.nisrulz.sensey.gesture.taponback

import android.hardware.Sensor
import com.github.nisrulz.sensey.SensorUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class TapOnBackDetectorTest {
    private fun gravityEvent(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 9.81f,
        tsMicros: Long = 0L,
    ) = SensorUtils.testSensorEvent(floatArrayOf(x, y, z), Sensor.TYPE_GRAVITY).also { it.timestamp = tsMicros }

    private fun accelEvent(
        x: Float,
        y: Float,
        z: Float,
        tsMicros: Long,
    ) = SensorUtils.testSensorEvent(floatArrayOf(x, y, z), Sensor.TYPE_ACCELEROMETER).also { it.timestamp = tsMicros }

    @Test
    fun dispatchesOnSecondTapWithinInterval() {
        val events = mutableListOf<TapOnBackEvent>()
        val trigger = TapOnBackTrigger(accelThreshold = 2f, minJerk = 2f, tapIntervalMs = 500L, cooldownMs = 2000L)
        val detector = TapOnBackDetector(trigger, dispatcher = { events.add(it) })

        // Establish gravity baseline at t=0
        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))

        // Tap 1 at t=300ms: accel [20,0,10] → linearMag=|22.36-9.81|=12.55, jerk=|12.55-1.26|=11.3
        detector.onSensorChanged(accelEvent(x = 20f, y = 0f, z = 10f, tsMicros = 300_000_000L))

        // Tap 2 at t=600ms: accel [15,15,5] → linearMag=|21.79-9.81|=11.98, jerk=|11.98-1.20|=10.8
        // Both jerks > 3 ✓
        detector.onSensorChanged(accelEvent(x = 15f, y = 15f, z = 5f, tsMicros = 600_000_000L))

        assertTrue("TapOnBack should fire on second tap", events.contains(TapOnBackEvent))
    }

    @Test
    fun noEventOnSingleTap() {
        val events = mutableListOf<TapOnBackEvent>()
        val detector = TapOnBackDetector(TapOnBackTrigger(), dispatcher = { events.add(it) })

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))
        detector.onSensorChanged(accelEvent(x = 10f, y = 0f, z = 10f, tsMicros = 300_000_000L))

        assertTrue("Single tap should not fire", events.isEmpty())
    }
}
