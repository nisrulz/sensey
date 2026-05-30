
package com.github.nisrulz.sensey.gesture.audio.clap

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects a clap sound from microphone audio samples.
 *
 * Algorithm: Computes RMS energy (dBFS) from raw PCM samples. A clap is
 * characterized by a sharp rise in energy: the current buffer must exceed
 * [thresholdDb] AND be at least [riseDb] dB louder than the previous buffer.
 * This prevents false triggers from sustained sounds like music or speech.
 * Expected source: AudioRecord (VOICE_RECOGNITION) with PCM float samples.
 * State: previousRmsDb (dBFS of the previous buffer for rise comparison).
 */
internal class ClapTrigger(
    private val thresholdDb: Float = -10f,
    private val riseDb: Float = 10f,
) : GestureTrigger<ClapEvent> {
    private var previousRmsDb = Float.NEGATIVE_INFINITY // dBFS of previous buffer

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ClapEvent? {
        val rms = computeRms(values)
        val rmsDb = 20f * kotlin.math.log10((rms / 32768f).coerceAtLeast(1e-10f))
        val isClap =
            rmsDb > thresholdDb &&
                // Buffer is loud enough
                previousRmsDb > Float.NEGATIVE_INFINITY &&
                // We have a baseline
                rmsDb - previousRmsDb >= riseDb // Sharp energy rise
        previousRmsDb = rmsDb
        return if (isClap) ClapEvent.Clapped else null
    }

    private fun computeRms(samples: FloatArray): Float {
        var sum = 0f
        for (s in samples) sum += s * s
        return sqrt(sum / samples.size.coerceAtLeast(1))
    }
}
