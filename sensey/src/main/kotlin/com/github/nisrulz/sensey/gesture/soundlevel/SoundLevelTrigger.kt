
package com.github.nisrulz.sensey.gesture.soundlevel

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Computes the ambient sound level in decibels from raw audio samples.
 *
 * Algorithm: Computes the root-mean-square (RMS) of the audio sample buffer,
 * normalised by the maximum amplitude (32768 for 16-bit PCM). The RMS is then
 * converted to decibels using the formula 20 × log10(rms). A configurable
 * positive offset is added to produce a human-friendly range.
 * Expected sensor: Audio recording (MediaRecorder or AudioRecord).
 * State: None (stateless computation).
 */
internal class SoundLevelTrigger(
    private val offset: Float = 100f,
) : GestureTrigger<SoundLevelEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): SoundLevelEvent? {
        if (values.isEmpty()) return null // No audio data to process
        val rms = computeRms(values) // Compute root-mean-square of the audio samples
        val soundLevel = computeDecibels(rms) // Convert RMS to a decibel value
        return SoundLevelEvent(soundLevel + offset) // Emit sound level event with configurable offset
    }

    private fun computeRms(samples: FloatArray): Double {
        // Compute RMS normalized by the maximum 16-bit PCM amplitude (32768)
        var sumSquares = 0.0
        for (sample in samples) {
            sumSquares += (sample / MAX_AMPLITUDE) * (sample / MAX_AMPLITUDE)
        }
        return sqrt(abs(sumSquares / samples.size))
    }

    private fun computeDecibels(rms: Double): Float {
        // Convert the RMS value to decibels: 20 × log10(rms)
        val db = 20.0 * log10(rms.coerceAtLeast(MIN_POWER))
        return if (db.isNaN() || db.isInfinite()) 0f else db.toFloat() // Guard against degenerate RMS values
    }

    companion object {
        private const val MAX_AMPLITUDE = 32768.0 // Maximum amplitude for 16-bit PCM audio
        private const val MIN_POWER = 1e-10 // Floor to avoid log10(0) which is -infinity
    }
}
