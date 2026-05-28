
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
            valuesField[sensorEvent] = values
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sensorEvent.sensor = testSensor(type)
        return sensorEvent
    }

    fun testSensorWithRange(
        values: FloatArray?,
        type: Int,
        maxRange: Float,
    ): SensorEvent {
        val sensorEvent = Mockito.mock(SensorEvent::class.java)
        try {
            val valuesField = SensorEvent::class.java.getField("values")
            valuesField.isAccessible = true
            valuesField[sensorEvent] = values
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val sensor = Mockito.mock(Sensor::class.java)
        Mockito.`when`(sensor.type).thenReturn(type)
        Mockito.`when`(sensor.maximumRange).thenReturn(maxRange)
        sensorEvent.sensor = sensor
        return sensorEvent
    }

    private fun testSensor(type: Int): Sensor {
        val sensor = Mockito.mock(Sensor::class.java)
        Mockito.`when`(sensor.type).thenReturn(type)
        Mockito.`when`(sensor.maximumRange).thenReturn(Float.MAX_VALUE)
        return sensor
    }
}
