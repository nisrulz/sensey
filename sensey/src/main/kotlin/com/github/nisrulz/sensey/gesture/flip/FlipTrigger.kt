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
package com.github.nisrulz.sensey.gesture.flip

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class FlipTrigger(
    private val faceUpLowerBound: Float = 8f,
    private val faceUpUpperBound: Float = 10.5f,
    private val faceDownLowerBound: Float = -10.5f,
    private val faceDownUpperBound: Float = -8f,
) : GestureTrigger<FlipEvent> {
    private var eventOccurred = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): FlipEvent? {
        val z = values[2]
        return when {
            isFaceUp(z) && eventOccurred != 1 -> {
                eventOccurred = 1
                FlipEvent.FaceUp
            }
            isFaceDown(z) && eventOccurred != 2 -> {
                eventOccurred = 2
                FlipEvent.FaceDown
            }
            else -> null
        }
    }

    private fun isFaceUp(z: Float): Boolean = z in faceUpLowerBound..faceUpUpperBound

    private fun isFaceDown(z: Float): Boolean = z in faceDownLowerBound..faceDownUpperBound
}
