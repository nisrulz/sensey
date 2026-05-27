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
package com.github.nisrulz.sensey.gesture.soundlevel

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt

class SoundLevelTrigger(
    private val offset: Float = 100f,
) : GestureTrigger<SoundLevelEvent> {

    override fun evaluate(values: FloatArray, timestamp: Long): SoundLevelEvent? {
        if (values.isEmpty()) return null

        var sumLevel = 0.0
        for (value in values) {
            sumLevel += value / 32768.0
        }
        val numberOfSamples = values.size
        val meanSquare = abs(sumLevel / numberOfSamples).coerceAtLeast(1e-10)
        val rms = sqrt(meanSquare)
        var soundLevel = (20.0 * log10(rms)).toFloat()

        if (soundLevel.isNaN() || soundLevel.isInfinite()) return null

        soundLevel += offset
        return SoundLevelEvent(soundLevel)
    }
}
