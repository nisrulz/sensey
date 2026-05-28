
package com.github.nisrulz.sensey

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import com.github.nisrulz.sensey.contract.GestureTrigger

abstract class SensorDetector(
    vararg sensorTypes: Int,
) : SensorEventListener {
    val sensorTypes: IntArray = sensorTypes
    var sensorDataLoggingEnabled: Boolean = false

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int,
    ) = Unit

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type !in sensorTypes) return
        logSensorEvent(event)
        onSensorEvent(event)
    }

    private fun logSensorEvent(event: SensorEvent) {
        if (!sensorDataLoggingEnabled) return
        val tag = SENSOR_TAGS[event.sensor.type] ?: return
        Log.d(tag, event.values.joinToString(","))
    }

    protected open fun onSensorEvent(sensorEvent: SensorEvent) = Unit

    private companion object {
        val SENSOR_TAGS =
            mapOf(
                Sensor.TYPE_ACCELEROMETER to "===Accelerometer===",
                Sensor.TYPE_GYROSCOPE to "===Gyroscope===",
                Sensor.TYPE_MAGNETIC_FIELD to "===Magnetometer===",
                Sensor.TYPE_ROTATION_VECTOR to "===RotationVector===",
                Sensor.TYPE_LIGHT to "===Light===",
                Sensor.TYPE_PROXIMITY to "===Proximity===",
                Sensor.TYPE_PRESSURE to "===Pressure===",
                Sensor.TYPE_STEP_COUNTER to "===StepCounter===",
                Sensor.TYPE_GRAVITY to "===Gravity===",
                Sensor.TYPE_LINEAR_ACCELERATION to "===LinAccel===",
            )
    }
}

open class TypedSensorDetector<T>(
    val trigger: GestureTrigger<T>,
    val dispatcher: (T) -> Unit,
    vararg sensorTypes: Int,
) : SensorDetector(*sensorTypes) {
    override fun onSensorEvent(sensorEvent: SensorEvent) {
        val event =
            trigger.evaluate(
                values = getValues(sensorEvent),
                timestamp = sensorEvent.timestamp / 1_000_000,
            )
        event?.let(dispatcher)
    }

    protected open fun getValues(sensorEvent: SensorEvent): FloatArray = sensorEvent.values
}
