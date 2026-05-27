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
package com.github.nisrulz.sensey.gesture.orientation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.nisrulz.sensey.SensorDetector

class OrientationDetector(
    private val trigger: OrientationTrigger,
    private val dispatcher: (OrientationEvent) -> Unit,
) : SensorDetector(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD) {

    private var mGravity: FloatArray? = null
    private var mGeomagnetic: FloatArray? = null

    override fun onSensorEvent(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values
        }
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values
        }
        val gravity = mGravity ?: return
        val geomagnetic = mGeomagnetic ?: return

        val R = FloatArray(9)
        val I = FloatArray(9)
        if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
            val orientationData = FloatArray(3)
            SensorManager.getOrientation(R, orientationData)
            val pitch = Math.toDegrees(orientationData[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientationData[2].toDouble()).toFloat()
            val result = trigger.evaluate(floatArrayOf(pitch, roll), event.timestamp / 1_000_000)
            result?.let(dispatcher)
        }
    }
}
