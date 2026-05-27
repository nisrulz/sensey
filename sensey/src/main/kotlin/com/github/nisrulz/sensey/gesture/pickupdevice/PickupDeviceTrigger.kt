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
package com.github.nisrulz.sensey.gesture.pickupdevice

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

class PickupDeviceTrigger(
    private val stableRange: Float = 0.5f,
    private val movingRange: Float = 1.5f,
    private val gravityLower: Float = 9.0f,
    private val gravityUpper: Float = 10.5f,
    private val windowSize: Int = 8,
    private val settleReadings: Int = 6,
) : GestureTrigger<PickupDeviceEvent> {

    private val buffer = ArrayDeque<Float>(windowSize + 1)
    private var isHeld = false
    private var settleCount = 0

    override fun evaluate(values: FloatArray, timestamp: Long): PickupDeviceEvent? {
        val vm = sqrt(
            (values[0] * values[0] + values[1] * values[1] + values[2] * values[2]).toDouble(),
        ).toFloat()

        buffer.addLast(vm)
        if (buffer.size > windowSize) buffer.removeFirst()

        if (buffer.size < 3) return null

        val range = buffer.max() - buffer.min()
        val meanVm = buffer.sum() / buffer.size

        return when {
            !isHeld && range > movingRange -> {
                isHeld = true
                settleCount = 0
                PickupDeviceEvent.PickedUp
            }

            isHeld && meanVm in gravityLower..gravityUpper && range <= stableRange -> {
                settleCount++
                if (settleCount >= settleReadings) {
                    isHeld = false
                    PickupDeviceEvent.PutDown
                } else {
                    null
                }
            }

            else -> {
                if (isHeld) settleCount = 0
                null
            }
        }
    }
}
