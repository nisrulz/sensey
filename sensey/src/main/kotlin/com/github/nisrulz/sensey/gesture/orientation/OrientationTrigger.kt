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

class OrientationTrigger(
    private val smoothness: Int = 1,
) : GestureTrigger<OrientationEvent> {

    private val windowSize = smoothness.coerceAtLeast(1)
    private var eventOccurred = 0
    private var currentOrientation = ORIENTATION_PORTRAIT
    private val pitches = FloatArray(windowSize)
    private val rolls = FloatArray(windowSize)
    private var pitchSum = 0f
    private var rollSum = 0f
    private var bufferIndex = 0
    private var bufferInitialized = false

    override fun evaluate(values: FloatArray, timestamp: Long): OrientationEvent? {
        val pitch = values.getOrNull(0) ?: return null
        val roll = values.getOrNull(1) ?: return null

        if (!bufferInitialized) {
            pitches.fill(pitch)
            rolls.fill(roll)
            pitchSum = pitch * windowSize
            rollSum = roll * windowSize
            bufferInitialized = true
        }

        val oldPitch = pitches[bufferIndex]
        pitches[bufferIndex] = pitch
        pitchSum = pitchSum - oldPitch + pitch
        val averagePitch = pitchSum / windowSize

        val oldRoll = rolls[bufferIndex]
        rolls[bufferIndex] = roll
        rollSum = rollSum - oldRoll + roll
        val averageRoll = rollSum / windowSize

        bufferIndex = (bufferIndex + 1) % windowSize

        currentOrientation = calculateOrientation(averagePitch, averageRoll, currentOrientation)

        val result = when (currentOrientation) {
            ORIENTATION_PORTRAIT -> if (eventOccurred != 1) {
                eventOccurred = 1; OrientationEvent.TopSideUp
            } else null
            ORIENTATION_LANDSCAPE -> if (eventOccurred != 2) {
                eventOccurred = 2; OrientationEvent.RightSideUp
            } else null
            ORIENTATION_PORTRAIT_REVERSE -> if (eventOccurred != 3) {
                eventOccurred = 3; OrientationEvent.BottomSideUp
            } else null
            ORIENTATION_LANDSCAPE_REVERSE -> if (eventOccurred != 4) {
                eventOccurred = 4; OrientationEvent.LeftSideUp
            } else null
            else -> null
        }
        return result
    }

    private fun calculateOrientation(
        averagePitch: Float,
        averageRoll: Float,
        previousOrientation: Int,
    ): Int {
        return if ((previousOrientation == ORIENTATION_PORTRAIT ||
                    previousOrientation == ORIENTATION_PORTRAIT_REVERSE) &&
            averageRoll in -30f..30f
        ) {
            if (averagePitch > 0) ORIENTATION_PORTRAIT_REVERSE else ORIENTATION_PORTRAIT
        } else {
            if (kotlin.math.abs(averagePitch) >= 30) {
                if (averagePitch > 0) ORIENTATION_PORTRAIT_REVERSE else ORIENTATION_PORTRAIT
            } else {
                if (averageRoll > 0) ORIENTATION_LANDSCAPE_REVERSE else ORIENTATION_LANDSCAPE
            }
        }
    }

    private companion object {
        const val ORIENTATION_PORTRAIT = 1
        const val ORIENTATION_LANDSCAPE = 2
        const val ORIENTATION_PORTRAIT_REVERSE = 3
        const val ORIENTATION_LANDSCAPE_REVERSE = 4
    }
}
