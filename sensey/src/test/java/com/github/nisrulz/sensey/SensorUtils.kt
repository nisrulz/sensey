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
import org.mockito.Mockito

object SensorUtils {
    fun testAccelerometerEvent(values: FloatArray?): SensorEvent = testSensorEvent(values, Sensor.TYPE_ACCELEROMETER)

    fun testSensorEvent(
        values: FloatArray?,
        type: Int,
    ): SensorEvent {
        val sensorEvent = Mockito.mock(SensorEvent::class.java)

        try {
            val valuesField = SensorEvent::class.java.getField("values")
            valuesField.isAccessible = true
            try {
                valuesField[sensorEvent] = values
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        sensorEvent.sensor = testSensor(type)

        return sensorEvent
    }

    private fun testSensor(type: Int): Sensor {
        val sensor = Mockito.mock(Sensor::class.java)
        Mockito.`when`(sensor.type).thenReturn(type)
        return sensor
    }
}
