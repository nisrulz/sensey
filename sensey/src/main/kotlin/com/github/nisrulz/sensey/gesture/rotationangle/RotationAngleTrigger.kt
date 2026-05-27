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

import com.github.nisrulz.sensey.contract.GestureTrigger

class RotationAngleTrigger(
    private val minAngleChange: Float = 1f,
) : GestureTrigger<RotationAngleEvent> {

    private var lastEvent: RotationAngleEvent? = null

    override fun evaluate(values: FloatArray, timestamp: Long): RotationAngleEvent? {
        val (ax, ay, az) = values
        val event = RotationAngleEvent(ax, ay, az)

        return if (lastEvent == null ||
            kotlin.math.abs(ax - lastEvent!!.angleInAxisX) > minAngleChange ||
            kotlin.math.abs(ay - lastEvent!!.angleInAxisY) > minAngleChange ||
            kotlin.math.abs(az - lastEvent!!.angleInAxisZ) > minAngleChange
        ) {
            lastEvent = event
            event
        } else {
            null
        }
    }
}
