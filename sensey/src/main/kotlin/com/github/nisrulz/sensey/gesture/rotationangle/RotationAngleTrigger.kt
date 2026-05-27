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

internal class RotationAngleTrigger(
    private val minAngleChange: Float = 1f,
) : GestureTrigger<RotationAngleEvent> {
    private var lastEvent: RotationAngleEvent? = null

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): RotationAngleEvent? {
        val event = RotationAngleEvent(values[0], values[1], values[2])
        val previous = lastEvent
        lastEvent = event

        return if (previous == null || hasSignificantChange(event, previous)) event else null
    }

    private fun hasSignificantChange(
        current: RotationAngleEvent,
        previous: RotationAngleEvent,
    ): Boolean =
        kotlin.math.abs(current.angleInAxisX - previous.angleInAxisX) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisY - previous.angleInAxisY) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisZ - previous.angleInAxisZ) > minAngleChange
}
