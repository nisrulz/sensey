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

internal class SoundLevelTrigger(
    private val offset: Float = 100f,
) : GestureTrigger<SoundLevelEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): SoundLevelEvent? {
        if (values.isEmpty()) return null
        val rms = computeRms(values)
        val soundLevel = computeDecibels(rms)
        return SoundLevelEvent(soundLevel + offset)
    }

    private fun computeRms(samples: FloatArray): Double {
        var sumSquares = 0.0
        for (sample in samples) {
            sumSquares += (sample / MAX_AMPLITUDE) * (sample / MAX_AMPLITUDE)
        }
        return sqrt(abs(sumSquares / samples.size))
    }

    private fun computeDecibels(rms: Double): Float {
        val db = 20.0 * log10(rms.coerceAtLeast(MIN_POWER))
        return if (db.isNaN() || db.isInfinite()) 0f else db.toFloat()
    }

    companion object {
        private const val MAX_AMPLITUDE = 32768.0
        private const val MIN_POWER = 1e-10
    }
}
