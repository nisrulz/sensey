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
package com.github.nisrulz.sensey.gesture.pinchscale

import com.github.nisrulz.sensey.contract.GestureTrigger

data class PinchScaleState(
    val eventOccurred: Int = 0,
    val countOfScaleIn: Int = 0,
    val countOfScaleOut: Int = 0,
)

class PinchScaleTrigger : GestureTrigger<PinchScaleEvent> {

    private var state = PinchScaleState()

    override fun evaluate(values: FloatArray, timestamp: Long): PinchScaleEvent? {
        val scaleFactor = values.getOrNull(0) ?: return null

        val (eventOccurred, countOfScaleIn, countOfScaleOut) = state
        var newEventOccurred = eventOccurred
        var newCountOfScaleIn = countOfScaleIn
        var newCountOfScaleOut = countOfScaleOut
        var result: PinchScaleEvent? = null

        if (scaleFactor > 1) {
            newCountOfScaleIn += 1
            if (newEventOccurred != 1 && newCountOfScaleIn > 2) {
                newEventOccurred = 1
                result = PinchScaleEvent(scaleFactor, false)
            }
        } else {
            newCountOfScaleOut += 1
            if (newEventOccurred != 2 && newCountOfScaleOut > 2) {
                newEventOccurred = 2
                result = PinchScaleEvent(scaleFactor, true)
            }
        }

        state = PinchScaleState(newEventOccurred, newCountOfScaleIn, newCountOfScaleOut)
        return result
    }

    fun reset() {
        state = PinchScaleState()
    }
}
