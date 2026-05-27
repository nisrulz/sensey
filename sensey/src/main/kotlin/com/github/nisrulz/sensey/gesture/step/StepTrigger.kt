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
package com.github.nisrulz.sensey.gesture.step

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

internal class StepTrigger(
    private val gender: Int = StepDetectorUtil.MALE,
    private val threshold: Float = 3f,
) : GestureTrigger<StepEvent> {
    private var steps = 0
    private var previousMagnitude = 0f
    private var startTime = 0L
    private var baseStepCount = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? =
        when {
            values.size == 1 -> evaluateStepCounter(values[0], timestamp)
            values.size >= 3 -> evaluateAccelerometer(values, timestamp)
            else -> null
        }

    private fun evaluateStepCounter(
        sensorValue: Float,
        timestamp: Long,
    ): StepEvent? {
        if (baseStepCount < 1) baseStepCount = sensorValue.toInt()
        steps = sensorValue.toInt() - baseStepCount
        return buildStepEvent(timestamp)
    }

    private fun evaluateAccelerometer(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? {
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        if (abs(magnitude - previousMagnitude) > threshold) steps++
        previousMagnitude = magnitude
        return buildStepEvent(timestamp)
    }

    private fun buildStepEvent(timestamp: Long): StepEvent {
        val distance = StepDetectorUtil.getDistanceCovered(steps, gender)
        val timeDelta = timestamp - startTime
        startTime = timestamp
        val activityType = StepDetectorUtil.getStepActivityType(distance, timeDelta)
        return StepEvent(steps, distance, activityType)
    }
}
