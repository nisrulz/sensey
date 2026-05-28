
package com.github.nisrulz.sensey.gesture.rotationangle

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.nisrulz.sensey.SensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger

internal class RotationAngleDetector(
    private val trigger: GestureTrigger<RotationAngleEvent>,
    private val dispatcher: (RotationAngleEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_ROTATION_VECTOR) {
    override fun onSensorEvent(sensorEvent: SensorEvent) {
        val orientations = computeOrientationAngles(sensorEvent.values)
        val event = trigger.evaluate(values = orientations, timestamp = sensorEvent.timestamp / 1_000_000)
        event?.let(dispatcher)
    }

    private fun computeOrientationAngles(rotationVector: FloatArray): FloatArray {
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

        val remappedMatrix = FloatArray(16)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            remappedMatrix,
        )

        val angles = FloatArray(3)
        SensorManager.getOrientation(remappedMatrix, angles)

        for (i in angles.indices) {
            angles[i] = Math.toDegrees(angles[i].toDouble()).toFloat()
        }
        return angles
    }
}
