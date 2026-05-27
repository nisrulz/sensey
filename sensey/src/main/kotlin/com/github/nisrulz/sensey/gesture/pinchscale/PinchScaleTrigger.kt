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

internal class PinchScaleTrigger : GestureTrigger<PinchScaleEvent> {
    private var eventOccurred = 0
    private var scaleInCount = 0
    private var scaleOutCount = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): PinchScaleEvent? {
        val scaleFactor = values.getOrNull(0) ?: return null

        return when {
            isScalingIn(scaleFactor) -> {
                scaleInCount++
                if (eventOccurred != SCALE_IN && scaleInCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_IN
                    PinchScaleEvent(scaleFactor, isScalingOut = false)
                } else {
                    null
                }
            }
            isScalingOut(scaleFactor) -> {
                scaleOutCount++
                if (eventOccurred != SCALE_OUT && scaleOutCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_OUT
                    PinchScaleEvent(scaleFactor, isScalingOut = true)
                } else {
                    null
                }
            }
            else -> null
        }
    }

    fun reset() {
        eventOccurred = 0
        scaleInCount = 0
        scaleOutCount = 0
    }

    private fun isScalingIn(scaleFactor: Float): Boolean = scaleFactor > SCALE_IN_THRESHOLD

    private fun isScalingOut(scaleFactor: Float): Boolean = scaleFactor < SCALE_OUT_THRESHOLD

    companion object {
        private const val SCALE_IN_THRESHOLD = 1.01f
        private const val SCALE_OUT_THRESHOLD = 0.99f
        private const val CONFIRMATION_THRESHOLD = 2
        private const val SCALE_IN = 1
        private const val SCALE_OUT = 2
    }
}
