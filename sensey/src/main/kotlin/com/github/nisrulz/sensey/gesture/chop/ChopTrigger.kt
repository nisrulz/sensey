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
package com.github.nisrulz.sensey.gesture.chop

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

internal class ChopTrigger(
    private val threshold: Float = 25f,
    private val timeForChopGesture: Long = 700L,
) : GestureTrigger<ChopEvent> {
    private var isGestureInProgress = false
    private var lastChopTime = 0L

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ChopEvent? {
        val linearAccel = computeLinearAcceleration(values)
        if (linearAccel > threshold) {
            lastChopTime = timestamp
            isGestureInProgress = true
            return null
        }
        return if (hasGestureCompleted(timestamp)) {
            isGestureInProgress = false
            ChopEvent.Chopped
        } else {
            null
        }
    }

    private fun computeLinearAcceleration(values: FloatArray): Float {
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        return abs(magnitude - GRAVITY_EARTH)
    }

    private fun hasGestureCompleted(timestamp: Long): Boolean {
        val timeSinceLastMotion = timestamp - lastChopTime
        return timeSinceLastMotion > timeForChopGesture && isGestureInProgress
    }

    companion object {
        private const val GRAVITY_EARTH = 9.81f
    }
}
