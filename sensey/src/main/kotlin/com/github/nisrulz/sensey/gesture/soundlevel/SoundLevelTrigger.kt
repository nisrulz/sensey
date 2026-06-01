
package com.github.nisrulz.sensey.gesture.soundlevel

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Measures the ambient sound level in dBFS + [offset] from microphone audio.
 *
 * Algorithm (stateless, per‑buffer):
 * 1. [Validate][hasValidBuffer] — reject empty buffers.
 * 2. [RMS][computeNormalizedRms] — power in the 16‑bit PCM buffer, normalised
 *    by the maximum possible amplitude (32768).
 * 3. [dB conversion][toDecibels] — 20 × log₁₀(normalised RMS) with a
 *    configurable [offset] to shift into a human‑friendly range (default
 *    0–100 scale, where 0 ≈ silence and 100 ≈ full scale).
 *
 * No audio data is stored, transmitted, or persisted — the measurement is
 * emitted immediately via [SoundLevelEvent] and discarded.
 * Expected source: AudioRecord (VOICE_RECOGNITION) with PCM 16‑bit mono.
 */
internal class SoundLevelTrigger(
    private val offset: Float = 100f,
) : GestureTrigger<SoundLevelEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): SoundLevelEvent? {
        if (hasInvalidBuffer(values)) return null
        val decibels = computeDecibelLevel(values)
        return SoundLevelEvent(decibels + offset)
    }

    private fun hasInvalidBuffer(values: FloatArray): Boolean = values.isEmpty()

    private fun computeDecibelLevel(samples: FloatArray): Float {
        val normalizedRms = computeNormalizedRms(samples)
        return toDecibels(normalizedRms)
    }

    private fun computeNormalizedRms(samples: FloatArray): Double {
        var sumSquares = 0.0
        for (sample in samples) {
            val normalized = sample / MAX_AMPLITUDE
            sumSquares += normalized * normalized
        }
        return sqrt(sumSquares / samples.size)
    }

    private fun toDecibels(rms: Double): Float {
        val db = MAX_DB_GAIN * log10(rms.coerceAtLeast(MIN_POWER))
        return if (db.isNaN() || db.isInfinite()) 0f else db.toFloat()
    }

    companion object {
        private const val MAX_AMPLITUDE = 32768.0
        private const val MIN_POWER = 1e-10
        private const val MAX_DB_GAIN = 20.0
    }
}
