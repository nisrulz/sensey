/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.sensey

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import com.github.nisrulz.sensey.contract.GestureTrigger

abstract class SensorDetector(vararg sensorTypes: Int) : SensorEventListener {

    val sensorTypes: IntArray = sensorTypes

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (isSensorEventBelongsToPluggedTypes(event)) {
            logSensorEvent(event)
            onSensorEvent(event)
        }
    }

    private fun logSensorEvent(event: SensorEvent) {
        if (!Sensey.sensorDataLoggingEnabled) return
        val tag = TAG_BY_TYPE[event.sensor.type] ?: return
        val values = event.values.joinToString(",")
        Log.d(tag, values)
    }

    companion object {
        private val TAG_BY_TYPE = mapOf(
            Sensor.TYPE_ACCELEROMETER to "Accelerometer",
            Sensor.TYPE_GYROSCOPE to "Gyroscope",
            Sensor.TYPE_MAGNETIC_FIELD to "Magnetometer",
            Sensor.TYPE_ROTATION_VECTOR to "RotationVector",
            Sensor.TYPE_LIGHT to "Light",
            Sensor.TYPE_PROXIMITY to "Proximity",
            Sensor.TYPE_PRESSURE to "Pressure",
            Sensor.TYPE_STEP_COUNTER to "StepCounter",
            Sensor.TYPE_GRAVITY to "Gravity",
            Sensor.TYPE_LINEAR_ACCELERATION to "LinearAcceleration",
        )
    }

    protected open fun onSensorEvent(sensorEvent: SensorEvent) {}

    private fun isSensorEventBelongsToPluggedTypes(event: SensorEvent): Boolean {
        return sensorTypes.any { it == event.sensor.type }
    }
}

open class TypedSensorDetector<T>(
    val trigger: GestureTrigger<T>,
    val dispatcher: (T) -> Unit,
    vararg sensorTypes: Int,
) : SensorDetector(*sensorTypes) {

    override fun onSensorEvent(sensorEvent: SensorEvent) {
        val event = trigger.evaluate(
            values = getValues(sensorEvent),
            timestamp = sensorEvent.timestamp / 1_000_000,
        )
        event?.let(dispatcher)
    }

    protected open fun getValues(sensorEvent: SensorEvent): FloatArray = sensorEvent.values
}
