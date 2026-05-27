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
package com.github.nisrulz.sensey.gesture.wave

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class WaveTrigger(
    private val timeWindowMillis: Float = 2000f,
    private val debounceMillis: Long = 1000L,
    private val minNearDurationMs: Long = 300L,
) : GestureTrigger<WaveEvent> {
    private var lastProximityEventTime = 0L
    private var lastProximityState = FAR
    private var lastWaveTime = 0L
    private var nearStateStartTime = 0L

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): WaveEvent? {
        val proximityState = if (values[0] == 0f) NEAR else FAR

        if (proximityState == NEAR && lastProximityState == FAR) {
            nearStateStartTime = timestamp
        }

        val result =
            if (isWaveDetected(timestamp, proximityState)) {
                lastWaveTime = timestamp
                WaveEvent.Waved
            } else {
                null
            }

        lastProximityEventTime = timestamp
        lastProximityState = proximityState
        return result
    }

    private fun isWaveDetected(
        timestamp: Long,
        proximityState: Int,
    ): Boolean {
        val nearDuration = timestamp - nearStateStartTime
        val isNearHeld = proximityState == FAR && nearDuration >= minNearDurationMs
        val isPastDebounce = lastWaveTime == 0L || timestamp - lastWaveTime > debounceMillis
        val isWithinTimeWindow = timestamp - lastProximityEventTime < timeWindowMillis
        val isFarTransition = lastProximityState == NEAR && proximityState == FAR
        return isPastDebounce && isWithinTimeWindow && isFarTransition && isNearHeld
    }

    private companion object {
        const val FAR = 0
        const val NEAR = 1
    }
}
