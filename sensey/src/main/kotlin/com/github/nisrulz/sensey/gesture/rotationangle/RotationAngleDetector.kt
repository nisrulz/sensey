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
package com.github.nisrulz.sensey.gesture.rotationangle

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.nisrulz.sensey.SensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger

class RotationAngleDetector(
    private val trigger: GestureTrigger<RotationAngleEvent>,
    private val dispatcher: (RotationAngleEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_ROTATION_VECTOR) {

    override fun onSensorEvent(sensorEvent: SensorEvent) {
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values)

        val remappedRotationMatrix = FloatArray(16)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            remappedRotationMatrix,
        )

        val orientations = FloatArray(3)
        SensorManager.getOrientation(remappedRotationMatrix, orientations)

        for (i in orientations.indices) {
            orientations[i] = Math.toDegrees(orientations[i].toDouble()).toFloat()
        }

        val event = trigger.evaluate(
            values = orientations,
            timestamp = sensorEvent.timestamp / 1_000_000,
        )
        event?.let(dispatcher)
    }
}
