
package com.github.nisrulz.sensey.gesture.taponback

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.linearAccelMag
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects quick double-taps on the back of the device.
 *
 * Algorithm — 5-phase state machine with noise rejection:
 *
 *  IDLE → SETTLING_FIRST → GUARD → LISTENING → SETTLING_SECOND → emit
 *
 * Noise rejection:
 *  - **Pre-settle gate** in IDLE: the signal must be quiet (below
 *    [SETTLE_THRESHOLD]) for [preSettleMs] before a spike is accepted.
 *    Shaking and continuous motion never sustain quiet long enough.
 *  - **Guard reset on spike**: any spike during the guard period
 *    resets the guard timer, so shaking never reaches the listening
 *    stage.
 *  - **Spike direction coherence**: both tap spikes must be in the
 *    same direction (dot product of linear acceleration vectors > 0).
 *    Back-and-forth movement produces opposite-direction spikes.
 *  - **Double settling**: both spikes must each settle within
 *    [settleWindowMs]. A stream of noise can't pass two settling gates.
 */
internal class TapOnBackTrigger(
    private val accelThreshold: Float = 1.5f,
    private val minJerk: Float = 2.0f,
    private val preSettleMs: Long = 200L,
    private val settleWindowMs: Long = 100L,
    private val reboundGuardMs: Long = 180L,
    private val tapIntervalMs: Long = 400L,
    private val cooldownMs: Long = 1000L,
) : GestureTrigger<TapOnBackEvent> {
    private companion object {
        const val TOLERANCE = 0.95f
        const val SETTLE_THRESHOLD = 2.0f

        /** Minimum dot product for two tap vectors to be coherent (same direction). */
        const val DIR_COHERENCE = 0.2f
    }

    private enum class State { IDLE, SETTLING_FIRST, GUARD, LISTENING, SETTLING_SECOND }

    private var state = State.IDLE
    private var firstTapTime = 0L
    private var lastEventTime = 0L
    private var quietSince = 0L
    private var settled = false
    private var cooldownUntil = 0L
    private var lastLinearMag = -1f

    /** Linear acceleration vector of the first spike, for direction coherence. */
    private var firstAccelDir = FloatArray(3) { 0f }

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TapOnBackEvent? {
        if (timestamp < cooldownUntil) return null

        val linearMag = computeLinearMag(values)
        val jerk = computeJerk(linearMag)

        return when (state) {
            State.IDLE -> handleIdle(values, linearMag, jerk, timestamp)
            State.SETTLING_FIRST -> handleSettling(linearMag, jerk, timestamp, isSecond = false)
            State.GUARD -> handleGuard(linearMag, jerk, timestamp)
            State.LISTENING -> handleListening(values, linearMag, jerk, timestamp)
            State.SETTLING_SECOND -> handleSettling(linearMag, jerk, timestamp, isSecond = true)
        }
    }

    // ── IDLE ───────────────────────────────────────────────────────────

    private fun handleIdle(
        values: FloatArray,
        linearMag: Float,
        jerk: Float,
        timestamp: Long,
    ): TapOnBackEvent? {
        if (isSpike(linearMag, jerk)) {
            if (quietSince > 0L && timestamp - quietSince >= preSettleMs && isBackTap(values)) {
                firstAccelDir = accelDir(values)
                beginSettling(timestamp, isSecond = false)
            }
            quietSince = 0L
        } else if (linearMag < SETTLE_THRESHOLD) {
            if (quietSince == 0L) quietSince = timestamp
        } else {
            quietSince = 0L
        }
        return null
    }

    // ── SETTLING (shared by first and second spike) ─────────────────────

    private fun handleSettling(
        linearMag: Float,
        jerk: Float,
        timestamp: Long,
        isSecond: Boolean,
    ): TapOnBackEvent? {
        if (!settled && linearMag < SETTLE_THRESHOLD) settled = true

        if (!settled) {
            if (timestamp - lastEventTime > settleWindowMs) {
                resetToIdle()
            }
            return null
        }

        if (isSecond) {
            cooldownUntil = timestamp + cooldownMs
            resetToIdle()
            return TapOnBackEvent.Detected
        } else {
            state = State.GUARD
            lastEventTime = timestamp
            settled = false
            lastLinearMag = -1f
            return null
        }
    }

    // ── GUARD ──────────────────────────────────────────────────────────

    private fun handleGuard(
        linearMag: Float,
        jerk: Float,
        timestamp: Long,
    ): TapOnBackEvent? {
        if (timestamp - firstTapTime > tapIntervalMs) {
            resetToIdle()
            return null
        }

        if (isSpike(linearMag, jerk)) {
            lastEventTime = timestamp
            return null
        }

        if (timestamp - lastEventTime > reboundGuardMs) {
            state = State.LISTENING
            lastEventTime = timestamp
            lastLinearMag = -1f
        }
        return null
    }

    // ── LISTENING: wait for second spike ───────────────────────────────

    private fun handleListening(
        values: FloatArray,
        linearMag: Float,
        jerk: Float,
        timestamp: Long,
    ): TapOnBackEvent? {
        if (timestamp - firstTapTime > tapIntervalMs) {
            resetToIdle()
            return null
        }

        if (isSpike(linearMag, jerk) && isBackTap(values)) {
            // Check direction coherence: second spike must be in same direction as first
            val secondDir = accelDir(values)
            val dot = dotProduct(firstAccelDir, secondDir)
            if (dot > DIR_COHERENCE) {
                beginSettling(timestamp, isSecond = true)
            }
        }
        return null
    }

    // ── Helpers ────────────────────────────────────────────────────────

    private fun isSpike(
        linearMag: Float,
        jerk: Float,
    ): Boolean = linearMag > accelThreshold * TOLERANCE && jerk > minJerk * TOLERANCE

    private fun computeLinearMag(values: FloatArray): Float =
        linearAccelMag(values[0], values[1], values[2], values[3], values[4], values[5])

    private fun computeJerk(linearMag: Float): Float {
        val prev = lastLinearMag
        lastLinearMag = linearMag
        if (prev < 0f) return linearMag
        return abs(linearMag - prev)
    }

    /** Linear acceleration vector (accel − gravity). */
    private fun accelDir(values: FloatArray): FloatArray =
        floatArrayOf(values[0] - values[3], values[1] - values[4], values[2] - values[5])

    /** True if the linear acceleration is primarily along Z (back-tap axis). */
    private fun isBackTap(values: FloatArray): Boolean {
        val dir = accelDir(values)
        val mag = sqrt(dir[0] * dir[0] + dir[1] * dir[1] + dir[2] * dir[2])
        return mag > 0.1f && abs(dir[2]) / mag > 0.5f
    }

    /** Dot product of two 3D vectors. */
    private fun dotProduct(
        a: FloatArray,
        b: FloatArray,
    ): Float = a[0] * b[0] + a[1] * b[1] + a[2] * b[2]

    private fun beginSettling(
        timestamp: Long,
        isSecond: Boolean,
    ) {
        state = if (isSecond) State.SETTLING_SECOND else State.SETTLING_FIRST
        if (!isSecond) firstTapTime = timestamp
        lastEventTime = timestamp
        settled = false
        lastLinearMag = -1f
    }

    private fun resetToIdle() {
        state = State.IDLE
        firstTapTime = 0L
        lastEventTime = 0L
        quietSince = 0L
        settled = false
        lastLinearMag = -1f
        firstAccelDir = FloatArray(3) { 0f }
    }
}
