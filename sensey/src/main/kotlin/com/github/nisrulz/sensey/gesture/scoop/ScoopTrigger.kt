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
package com.github.nisrulz.sensey.gesture.scoop

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

class ScoopTrigger(
    private val impulseThreshold: Float = 10f,
    private val minPeakJerk: Float = 3.0f,
    private val minSustainedSamples: Int = 3,
    private val debounceMs: Long = 1000L,
) : GestureTrigger<ScoopEvent> {

    private var accelBaseline = 9.8f
    private var prevAccelMag = 9.8f
    private var lastEventTime = 0L
    private var samplesAboveThreshold = 0
    private var peakJerkInWindow = 0f

    override fun evaluate(values: FloatArray, timestamp: Long): ScoopEvent? {
        val (ax, ay, az) = values

        val accelMag = sqrt(ax * ax + ay * ay + az * az)
        accelBaseline = accelBaseline * 0.95f + accelMag * 0.05f
        val impulse = abs(accelMag - accelBaseline)
        val jerk = abs(accelMag - prevAccelMag)
        prevAccelMag = accelMag

        if (impulse > impulseThreshold) {
            samplesAboveThreshold++
            if (jerk > peakJerkInWindow) peakJerkInWindow = jerk
        } else {
            samplesAboveThreshold = 0
            peakJerkInWindow = 0f
        }

        return if (samplesAboveThreshold >= minSustainedSamples && peakJerkInWindow > minPeakJerk
            && timestamp - lastEventTime > debounceMs
        ) {
            samplesAboveThreshold = 0
            peakJerkInWindow = 0f
            lastEventTime = timestamp
            ScoopEvent.Scooped
        } else {
            null
        }
    }
}
