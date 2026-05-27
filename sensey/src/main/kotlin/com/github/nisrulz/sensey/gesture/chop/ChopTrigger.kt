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

class ChopTrigger(
    private val threshold: Float = 35f,
    private val timeForChopGesture: Long = 700L,
) : GestureTrigger<ChopEvent> {

    private var isGestureInProgress = false
    private var lastTimeChopDetected = 0L

    override fun evaluate(values: FloatArray, timestamp: Long): ChopEvent? {
        val (x, y, z) = values

        return if (x > threshold && y < -threshold && z > threshold) {
            lastTimeChopDetected = timestamp
            isGestureInProgress = true
            null
        } else {
            val timeDelta = timestamp - lastTimeChopDetected
            if (timeDelta > timeForChopGesture && isGestureInProgress) {
                isGestureInProgress = false
                ChopEvent.Chopped
            } else {
                null
            }
        }
    }
}
