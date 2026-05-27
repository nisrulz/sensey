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
package com.github.nisrulz.sensey.gesture.movement

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

class MovementTrigger(
    private val threshold: Float = 0.3f,
    private val timeBeforeDeclaringStationary: Long = 5000L,
    private val gravityEarth: Float = 9.81f,
) : GestureTrigger<MovementEvent> {

    private var mAccelCurrent = gravityEarth
    private var isMoving = false
    private var lastTimeMovementDetected = 0L

    override fun evaluate(values: FloatArray, timestamp: Long): MovementEvent? {
        val (x, y, z) = values
        val mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt(x * x + y * y + z * z)
        val delta = abs(mAccelCurrent - mAccelLast)

        return if (delta > threshold) {
            lastTimeMovementDetected = timestamp
            isMoving = true
            MovementEvent.Moved
        } else {
            val timeDelta = timestamp - lastTimeMovementDetected
            if (timeDelta > timeBeforeDeclaringStationary && isMoving) {
                isMoving = false
                MovementEvent.Stationary
            } else {
                null
            }
        }
    }
}
