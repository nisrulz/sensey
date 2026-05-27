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
    private val vectorSumThreshold: Double = 11.0,
    private val settledThreshold: Double = 10.2,
) : GestureTrigger<PickupDeviceEvent> {

    private var wasPickedUp = false

    override fun evaluate(values: FloatArray, timestamp: Long): PickupDeviceEvent? {
        val (x, y, z) = values
        val vectorSum = sqrt(x * x + y * y + z * z)

        return if (vectorSum > vectorSumThreshold) {
            wasPickedUp = true
            PickupDeviceEvent.PickedUp
        } else if (wasPickedUp && vectorSum < settledThreshold) {
            wasPickedUp = false
            PickupDeviceEvent.PutDown
        } else {
            null
        }
    }
}
