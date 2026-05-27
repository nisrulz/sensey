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
package com.github.nisrulz.sensey.gesture.wristtwist

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

class WristTwistTrigger(
    private val threshold: Float = 12f,
    private val timeForWristTwistGesture: Long = 1000L,
) : GestureTrigger<WristTwistEvent> {

    private var isGestureInProgress = false
    private var lastTimeWristTwistDetected = 0L

    override fun evaluate(values: FloatArray, timestamp: Long): WristTwistEvent? {
        val (x, y, z) = values
        val magnitude = sqrt(x * x + y * y + z * z)
        val linearMagnitude = abs(magnitude - GRAVITY_EARTH)

        if (linearMagnitude > threshold) {
            lastTimeWristTwistDetected = timestamp
            isGestureInProgress = true
            return null
        }

        val timeDelta = timestamp - lastTimeWristTwistDetected
        return if (timeDelta > timeForWristTwistGesture && isGestureInProgress) {
            isGestureInProgress = false
            WristTwistEvent.Twisted
        } else {
            null
        }
    }

    companion object {
        private const val GRAVITY_EARTH = 9.8f
    }
}
