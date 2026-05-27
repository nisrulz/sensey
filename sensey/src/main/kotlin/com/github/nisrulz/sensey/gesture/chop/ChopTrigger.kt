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

class ChopTrigger(
    private val threshold: Float = 25f,
    private val timeForChopGesture: Long = 700L,
) : GestureTrigger<ChopEvent> {

    private var isGestureInProgress = false
    private var lastTimeChopDetected = 0L

    override fun evaluate(values: FloatArray, timestamp: Long): ChopEvent? {
        val (x, y, z) = values
        val magnitude = sqrt(x * x + y * y + z * z)
        val linearMagnitude = abs(magnitude - GRAVITY_EARTH)

        if (linearMagnitude > threshold) {
            lastTimeChopDetected = timestamp
            isGestureInProgress = true
            return null
        }

        val timeDelta = timestamp - lastTimeChopDetected
        return if (timeDelta > timeForChopGesture && isGestureInProgress) {
            isGestureInProgress = false
            ChopEvent.Chopped
        } else {
            null
        }
    }

    companion object {
        private const val GRAVITY_EARTH = 9.8f
    }
}
