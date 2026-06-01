package com.github.nisrulz.sensey.gesture.audio.clap

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects hand claps from microphone audio in real time.
 *
 * Pipeline per 16-bit PCM buffer:
 *
 * 1. **Power estimation** — RMS energy of the buffer, converted to dBFS.
 * 2. [ZCR weighting][computeWeightedRmsDb] — narrow‑band sounds (speech,
 *    tonal music) are quadratically penalised below [minZcr].
 * 3. [Noise‑floor EMA][adaptNoiseFloor] — effective threshold rises in
 *    noisy environments, bounded below by [thresholdDb].
 * 4. **Peak‑threshold** — buffer's weighted dBFS must exceed the effective
 *    threshold. A 200 ms debounce prevents counting the same transient twice.
 * 5. **Multi‑clap counting** — distinct claps are counted within a rolling
 *    [clapTimeframeMs] window. [ClapEvent.Clapped] fires only when the
 *    count reaches [requiredClaps] (default 2 — prevents single loud
 *    noises like door slams from triggering).
 *
 * Audio source: AudioRecord (VOICE_RECOGNITION, ENCODING_PCM_16BIT, mono).
 */
internal class ClapTrigger(
    private val thresholdDb: Float = -45f,
    private val requiredClaps: Int = 2,
    private val clapTimeframeMs: Long = 800L,
    private val debounceMs: Long = 500L,
    private val minZcr: Float = 0.10f,
    private val noiseFloorAttackAlpha: Float = 0.1f,
    private val noiseFloorReleaseAlpha: Float = 0.005f,
    private val minMarginDb: Float = 1f,
) : GestureTrigger<ClapEvent> {
    private var lastClapTimestamp = -1L
    private var noiseFloorDb = -60f
    private var singleClapTimestamp = -1L
    private val clapTimestamps = ArrayDeque<Long>()

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ClapEvent? {
        val currentRmsDb = computeWeightedRmsDb(values)
        adaptNoiseFloor(currentRmsDb)

        if (isWithinCooldown(timestamp)) return null
        if (isSingleClapDebounceActive(timestamp)) return null
        if (currentRmsDb <= resolveEffectiveThreshold()) return null

        return recordClap(timestamp)
    }

    /** RMS (dBFS) of the buffer, weighted by zero‑crossing rate. */
    private fun computeWeightedRmsDb(samples: FloatArray): Float {
        val n = samples.size.coerceAtLeast(2)
        var sum = 0f
        var prev = samples[0]
        var zeroCrossings = 0
        for (i in 1 until samples.size) {
            val s = samples[i]
            sum += s * s
            if (signChanged(prev, s)) zeroCrossings++
            prev = s
        }
        val rms = sqrt(sum / n)
        val zcr = zeroCrossings.toFloat() / (n - 1).toFloat()
        val zcrWeight = if (zcr >= minZcr) 1f else (zcr / minZcr).let { it * it }
        val weightedRms = rms * zcrWeight
        return 20f * kotlin.math.log10((weightedRms / 32768f).coerceAtLeast(1e-10f))
    }

    /**
     * Fast‑attack / slow‑release EMA tracker.
     * - Rising ambient → noise floor follows quickly (prevents false triggers).
     * - Falling ambient → noise floor decays slowly (avoids rapid threshold changes).
     */
    private fun adaptNoiseFloor(currentRmsDb: Float) {
        val alpha =
            if (currentRmsDb > noiseFloorDb) {
                noiseFloorAttackAlpha
            } else {
                noiseFloorReleaseAlpha
            }
        noiseFloorDb += alpha * (currentRmsDb - noiseFloorDb)
        noiseFloorDb = noiseFloorDb.coerceIn(-90f, -4f)
    }

    /** Debounce after a [ClapEvent.Clapped] was emitted. */
    private fun isWithinCooldown(timestamp: Long): Boolean =
        lastClapTimestamp >= 0L && (timestamp - lastClapTimestamp) < debounceMs

    /**
     * Prevents the same audio transient from being counted as multiple claps.
     * Once a buffer exceeds threshold, subsequent buffers within 200 ms are
     * ignored — this avoids double‑counting a single clap that spans two
     * PCM buffers.
     */
    private fun isSingleClapDebounceActive(timestamp: Long): Boolean =
        singleClapTimestamp >= 0L && (timestamp - singleClapTimestamp) < SINGLE_CLAP_DEBOUNCE_MS

    /** Whichever is stricter: noise‑floor‑based or absolute floor. */
    private fun resolveEffectiveThreshold(): Float = maxOf(noiseFloorDb + minMarginDb, thresholdDb)

    /**
     * Records a detected clap in the rolling time window.
     * Returns [ClapEvent.Clapped] when [requiredClaps] have accumulated
     * within [clapTimeframeMs]; returns null while still counting.
     */
    private fun recordClap(timestamp: Long): ClapEvent? {
        singleClapTimestamp = timestamp
        clapTimestamps.addLast(timestamp)
        pruneExpiredTimestamps(timestamp)
        return if (clapTimestamps.size >= effectiveRequiredClaps()) {
            lastClapTimestamp = timestamp
            clapTimestamps.clear()
            ClapEvent.Clapped
        } else {
            null
        }
    }

    /** Drops timestamps older than [clapTimeframeMs] (keeps the window sliding). */
    private fun pruneExpiredTimestamps(now: Long) {
        val cutoff = now - clapTimeframeMs
        while (clapTimestamps.size > 1 && clapTimestamps.first() < cutoff) {
            clapTimestamps.removeFirst()
        }
    }

    private fun effectiveRequiredClaps(): Int = maxOf(requiredClaps, 1)

    companion object {
        /** Minimum gap between two distinct claps to count them separately. */
        private const val SINGLE_CLAP_DEBOUNCE_MS = 200L

        private fun signChanged(
            a: Float,
            b: Float,
        ): Boolean = (a >= 0f && b < 0f) || (a < 0f && b >= 0f)
    }
}
