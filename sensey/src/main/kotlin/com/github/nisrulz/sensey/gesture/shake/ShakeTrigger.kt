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
package com.github.nisrulz.sensey.gesture.shake

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

class ShakeTrigger(
    private val threshold: Float = 3f,
    private val timeBeforeDeclaringShakeStopped: Long = 1000L,
) : GestureTrigger<ShakeEvent> {

    private var mAccel = 0f
    private var mAccelCurrent = 0f
    private var isShaking = false
    private var lastTimeShakeDetected = 0L
    private var ready = false

    override fun evaluate(values: FloatArray, timestamp: Long): ShakeEvent? {
        val (x, y, z) = values
        val newMag = sqrt(x * x + y * y + z * z)

        if (!ready) {
            mAccelCurrent = newMag
            mAccel = 0f
            ready = true
            return null
        }

        val mAccelLast = mAccelCurrent
        mAccelCurrent = newMag
        val delta = mAccelCurrent - mAccelLast
        mAccel = mAccel * 0.9f + delta

        return if (mAccel > threshold) {
            lastTimeShakeDetected = timestamp
            isShaking = true
            ShakeEvent.Detected
        } else {
            val timeDelta = timestamp - lastTimeShakeDetected
            if (timeDelta > timeBeforeDeclaringShakeStopped && isShaking) {
                isShaking = false
                ShakeEvent.Stopped
            } else {
                null
            }
        }
    }
}
