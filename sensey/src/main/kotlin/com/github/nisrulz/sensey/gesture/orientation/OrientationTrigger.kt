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
package com.github.nisrulz.sensey.gesture.orientation

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class OrientationTrigger(
    private val smoothness: Int = 1,
) : GestureTrigger<OrientationEvent> {
    private val windowSize = smoothness.coerceAtLeast(1)
    private var eventOccurred = 0
    private var currentOrientation = PORTRAIT
    private val pitchBuffer = FloatArray(windowSize)
    private val rollBuffer = FloatArray(windowSize)
    private var pitchSum = 0f
    private var rollSum = 0f
    private var bufferIndex = 0
    private var isBufferInitialized = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): OrientationEvent? {
        val pitch = values.getOrNull(0) ?: return null
        val roll = values.getOrNull(1) ?: return null

        if (!isBufferInitialized) initializeBuffer(pitch, roll)

        val oldPitch = pitchBuffer[bufferIndex]
        pitchBuffer[bufferIndex] = pitch
        pitchSum = pitchSum - oldPitch + pitch
        val averagePitch = pitchSum / windowSize

        val oldRoll = rollBuffer[bufferIndex]
        rollBuffer[bufferIndex] = roll
        rollSum = rollSum - oldRoll + roll
        val averageRoll = rollSum / windowSize

        bufferIndex = (bufferIndex + 1) % windowSize

        currentOrientation = classifyOrientation(averagePitch, averageRoll, currentOrientation)
        return toOrientationEvent(currentOrientation)
    }

    private fun initializeBuffer(
        pitch: Float,
        roll: Float,
    ) {
        pitchBuffer.fill(pitch)
        rollBuffer.fill(roll)
        pitchSum = pitch * windowSize
        rollSum = roll * windowSize
        isBufferInitialized = true
    }

    private fun toOrientationEvent(orientation: Int): OrientationEvent? =
        when (orientation) {
            PORTRAIT -> {
                if (eventOccurred != 1) {
                    eventOccurred = 1
                    OrientationEvent.TopSideUp
                } else {
                    null
                }
            }
            LANDSCAPE -> {
                if (eventOccurred != 2) {
                    eventOccurred = 2
                    OrientationEvent.RightSideUp
                } else {
                    null
                }
            }
            PORTRAIT_REVERSE -> {
                if (eventOccurred != 3) {
                    eventOccurred = 3
                    OrientationEvent.BottomSideUp
                } else {
                    null
                }
            }
            LANDSCAPE_REVERSE -> {
                if (eventOccurred != 4) {
                    eventOccurred = 4
                    OrientationEvent.LeftSideUp
                } else {
                    null
                }
            }
            else -> null
        }

    private fun classifyOrientation(
        avgPitch: Float,
        avgRoll: Float,
        previous: Int,
    ): Int =
        if ((previous == PORTRAIT || previous == PORTRAIT_REVERSE) && avgRoll in -30f..30f) {
            if (avgPitch > 0) PORTRAIT_REVERSE else PORTRAIT
        } else if (kotlin.math.abs(avgPitch) >= 30) {
            if (avgPitch > 0) PORTRAIT_REVERSE else PORTRAIT
        } else if (avgRoll > 0) {
            LANDSCAPE_REVERSE
        } else {
            LANDSCAPE
        }

    private companion object {
        const val PORTRAIT = 1
        const val LANDSCAPE = 2
        const val PORTRAIT_REVERSE = 3
        const val LANDSCAPE_REVERSE = 4
    }
}
