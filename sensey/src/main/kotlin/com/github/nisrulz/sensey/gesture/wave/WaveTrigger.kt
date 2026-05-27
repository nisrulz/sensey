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

class WaveTrigger(
    private val timeWindowMillis: Float = 1000f,
    private val debounceMillis: Long = 1000L,
) : GestureTrigger<WaveEvent> {

    private var lastProximityEventTime = 0L
    private var lastProximityState = FAR
    private var lastWaveTime = 0L

    override fun evaluate(values: FloatArray, timestamp: Long): WaveEvent? {
        val distance = values[0]
        val proximityState = if (distance == 0f) NEAR else FAR

        val eventDeltaMillis = timestamp - lastProximityEventTime
        val result = if ((lastWaveTime == 0L || timestamp - lastWaveTime > debounceMillis)
            && eventDeltaMillis < timeWindowMillis
            && NEAR == lastProximityState
            && FAR == proximityState
        ) {
            lastWaveTime = timestamp
            WaveEvent.Waved
        } else {
            null
        }

        lastProximityEventTime = timestamp
        lastProximityState = proximityState
        return result
    }

    private companion object {
        const val FAR = 0
        const val NEAR = 1
    }
}
