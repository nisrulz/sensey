
package com.github.nisrulz.sensey.gesture.raisetoear

import android.hardware.Sensor
import android.hardware.SensorEvent
import com.github.nisrulz.sensey.SensorDetector

/**
 * Fuses proximity and gravity sensor data for raise-to-ear detection.
 *
 * Listens to [TYPE_PROXIMITY] for distance and [TYPE_GRAVITY] for the
 * gravity vector. On each proximity event, combines both into a 4-element
 * array [proximity, gx, gy, gz] for [RaiseToEarTrigger.evaluate].
 * State: proximity (last known distance), gravity (last known gravity vector).
 */
internal class RaiseToEarDetector(
    private val trigger: RaiseToEarTrigger,
    private val dispatcher: (RaiseToEarEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_PROXIMITY, Sensor.TYPE_GRAVITY) {
    private var proximity = Float.MAX_VALUE // Last known proximity distance (cm)
    private var gravity = FloatArray(3) // Last known gravity vector

    override fun onSensorEvent(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_GRAVITY -> gravity = event.values.clone() // Cache the latest gravity vector
            Sensor.TYPE_PROXIMITY -> {
                proximity = event.values[0]
                val result =
                    trigger.evaluate(
                        values = floatArrayOf(proximity, gravity[0], gravity[1], gravity[2]),
                        timestamp = event.timestamp / 1_000_000,
                    )
                result?.let(dispatcher)
            }
        }
    }
}
