
package com.github.nisrulz.sensey.gesture.orientation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.nisrulz.sensey.SensorDetector

internal class OrientationDetector(
    private val trigger: OrientationTrigger,
    private val dispatcher: (OrientationEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD) {
    private var gravityValues: FloatArray? = null
    private var geomagneticValues: FloatArray? = null

    override fun onSensorEvent(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravityValues = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> geomagneticValues = event.values
        }

        val gravity = gravityValues ?: return
        val geomagnetic = geomagneticValues ?: return

        val rotationMatrix = FloatArray(9)
        val inclinationMatrix = FloatArray(9)
        if (!SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravity, geomagnetic)) return

        val orientationData = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientationData)

        val pitch = Math.toDegrees(orientationData[1].toDouble()).toFloat()
        val roll = Math.toDegrees(orientationData[2].toDouble()).toFloat()

        val result = trigger.evaluate(floatArrayOf(pitch, roll), event.timestamp / 1_000_000)
        result?.let(dispatcher)
    }
}
