
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
    fun dispatchesOnDoubleTap() {
        val events = mutableListOf<TapOnBackEvent>()
        val trigger =
            TapOnBackTrigger(
                accelThreshold = 1.5f,
                minJerk = 2f,
                preSettleMs = 100L,
                reboundGuardMs = 100L,
                tapIntervalMs = 500L,
                cooldownMs = 2000L,
            )
        val detector = TapOnBackDetector(trigger, dispatcher = { events.add(it) })

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))

        // Quiet period before first tap
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 50_000_000L))
        // Tap 1 at t=300ms — Z-dominant (20 Z, gravity 9.81 → linear Z ≈10)
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 20f, tsMicros = 300_000_000L))
        // Settle tap 1
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 400_000_000L))
        // Guard passes
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 550_000_000L))
        // Tap 2 at t=600ms — Z-dominant
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 18f, tsMicros = 600_000_000L))
        // Settle tap 2
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 700_000_000L))

        assertTrue("Double tap should fire", events.contains(TapOnBackEvent.Detected))
    }

    @Test
    fun noEventOnSingleTap() {
        val events = mutableListOf<TapOnBackEvent>()
        val detector =
            TapOnBackDetector(
                TapOnBackTrigger(accelThreshold = 1.5f, minJerk = 2f, preSettleMs = 100L, tapIntervalMs = 500L),
                dispatcher = { events.add(it) },
            )

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 50_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 20f, tsMicros = 300_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 500_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 900_000_000L))

        assertTrue("Single tap should not fire", events.isEmpty())
    }

    @Test
    fun noEventDuringContinuousShaking() {
        val events = mutableListOf<TapOnBackEvent>()
        val detector =
            TapOnBackDetector(
                TapOnBackTrigger(
                    accelThreshold = 1.5f,
                    minJerk = 2f,
                    preSettleMs = 100L,
                    settleWindowMs = 50L,
                    reboundGuardMs = 100L,
                    tapIntervalMs = 500L,
                ),
                dispatcher = { events.add(it) },
            )

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))

        for (i in 1..20) {
            val t = i * 20_000_000L
            val x = if (i % 2 == 0) 15f else -15f
            detector.onSensorChanged(accelEvent(x = x, y = 0f, z = 10f, tsMicros = t))
        }

        assertTrue("Continuous shaking should not fire", events.isEmpty())
    }

    @Test
    fun noEventWithStableValues() {
        val events = mutableListOf<TapOnBackEvent>()
        val detector = TapOnBackDetector(TapOnBackTrigger(preSettleMs = 100L), dispatcher = { events.add(it) })

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 100_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 9.81f, tsMicros = 200_000_000L))

        assertTrue("Stable values should not fire", events.isEmpty())
    }

    @Test
    fun noEventWithLowJerk() {
        val events = mutableListOf<TapOnBackEvent>()
        val detector =
            TapOnBackDetector(
                TapOnBackTrigger(accelThreshold = 1.5f, minJerk = 2f, preSettleMs = 100L),
                dispatcher = { events.add(it) },
            )

        detector.onSensorChanged(gravityEvent(z = 9.81f, tsMicros = 0L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 14f, tsMicros = 100_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 14f, tsMicros = 200_000_000L))
        detector.onSensorChanged(accelEvent(x = 0f, y = 0f, z = 14f, tsMicros = 300_000_000L))

        assertTrue("Low-jerk movement should not fire", events.isEmpty())
    }
}
