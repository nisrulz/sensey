package com.github.nisrulz.senseysample.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

/**
 * Logs all sensor data to logcat with ===Tag=== format.
 * This runs independently of Sensey so the data collector tool
 * can always capture sensor data regardless of which gesture is active.
 */
class SensorLogger(
    private val sensorManager: SensorManager,
) : SensorEventListener {
    fun start() {
        val sensors =
            listOf(
                Sensor.TYPE_ACCELEROMETER,
                Sensor.TYPE_GRAVITY,
                Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_MAGNETIC_FIELD,
                Sensor.TYPE_LINEAR_ACCELERATION,
                Sensor.TYPE_LIGHT,
                Sensor.TYPE_PROXIMITY,
                Sensor.TYPE_ROTATION_VECTOR,
            )
        for (type in sensors) {
            sensorManager.getDefaultSensor(type)?.let { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val tag = SENSOR_TAGS[event.sensor.type] ?: return
        val v = event.values
        val csv =
            when (v.size) {
                1 -> "%.4f".format(v[0])
                2 -> "%.4f,%.4f".format(v[0], v[1])
                else -> "%.4f,%.4f,%.4f".format(v[0], v[1], v[2])
            }
        Log.d(tag, csv)
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int,
    ) = Unit

    private companion object {
        val SENSOR_TAGS =
            mapOf(
                Sensor.TYPE_ACCELEROMETER to "===Accelerometer===",
                Sensor.TYPE_GRAVITY to "===Gravity===",
                Sensor.TYPE_GYROSCOPE to "===Gyroscope===",
                Sensor.TYPE_MAGNETIC_FIELD to "===Magnetometer===",
                Sensor.TYPE_LINEAR_ACCELERATION to "===LinAccel===",
                Sensor.TYPE_LIGHT to "===Light===",
                Sensor.TYPE_PROXIMITY to "===Proximity===",
                Sensor.TYPE_ROTATION_VECTOR to "===RotationVector===",
            )
    }
}
