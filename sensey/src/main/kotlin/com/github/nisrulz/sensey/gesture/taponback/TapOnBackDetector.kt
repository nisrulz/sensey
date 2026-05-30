
package com.github.nisrulz.sensey.gesture.taponback

import android.hardware.Sensor
import android.hardware.SensorEvent
import com.github.nisrulz.sensey.SensorDetector

/**
 * Detects taps on the back of the device using [TYPE_GRAVITY] for a smooth,
 * gyro-stabilized gravity baseline.
 *
 * Registers for both [TYPE_ACCELEROMETER] and [TYPE_GRAVITY]. On each
 * accelerometer event, combines the raw acceleration with the latest
 * gravity vector and passes a 6-element array to [TapOnBackTrigger.evaluate].
 */
internal class TapOnBackDetector(
    private val trigger: TapOnBackTrigger,
    private val dispatcher: (TapOnBackEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GRAVITY) {
    private var gravity = FloatArray(3)

    override fun onSensorEvent(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_GRAVITY -> {
                gravity = event.values.clone()
            }

            Sensor.TYPE_ACCELEROMETER -> {
                val result =
                    trigger.evaluate(
                        values =
                            floatArrayOf(
                                event.values[0],
                                event.values[1],
                                event.values[2],
                                gravity[0],
                                gravity[1],
                                gravity[2],
                            ),
                        timestamp = event.timestamp / 1_000_000,
                    )
                result?.let(dispatcher)
            }
        }
    }
}
