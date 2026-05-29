
package com.github.nisrulz.sensey.gesture.taponback

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

/**
 * Detects taps on the back of the device.
 *
 * Algorithm: Maintains an EMA-smoothed gravity baseline from accelerometer
 * readings. Computes the angular deviation from this baseline and the angular
 * jerk (change in angle). A valid tap requires: sufficient angle deviation,
 * minimum jerk, and debounce since the last tap. Accumulates taps within a
 * sequence timeout; emits only when at least two taps occur.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: baselineX/Y/Z (EMA gravity estimate), hasBaseline, previousAngleDeg
 * (for jerk), lastTapTime (debounce), tapCount (sequence accumulator).
 */
internal class TapOnBackTrigger(
    private val angleThreshold: Float = 1.5f,
    private val minAngleJerk: Float = 1.5f,
    private val tapDebounceMs: Long = 250L,
    private val tapSequenceTimeoutMs: Long = 500L,
) : GestureTrigger<TapOnBackEvent> {
    private var baselineX = 0f // EMA-smoothed gravity baseline on X-axis
    private var baselineY = 0f // EMA-smoothed gravity baseline on Y-axis
    private var baselineZ = GRAVITY_EARTH // EMA-smoothed gravity baseline on Z-axis
    private var hasBaseline = false // Whether the gravity baseline has been established
    private var previousAngleDeg = 0f // Previous angle from baseline, used for jerk calculation
    private var lastTapTime = 0L // Timestamp of the last detected tap (for debounce)
    private var tapCount = 0 // Number of taps accumulated in the current sequence

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TapOnBackEvent? {
        val (ax, ay, az) = values // Destructure accelerometer readings
        val accelMag = sqrt(ax * ax + ay * ay + az * az) // Compute total acceleration magnitude
        updateBaseline(ax, ay, az) // Smoothly update the gravity baseline

        val angleDeg = computeAngleFromBaseline(ax, ay, az, accelMag) // Compute angular deviation from baseline
        val angleJerk = abs(angleDeg - previousAngleDeg) // Compute angular jerk (rate of angle change)
        previousAngleDeg = angleDeg

        if (isValidTap(angleDeg, angleJerk, timestamp)) {
            tapCount++
            lastTapTime = timestamp
            return null // Register the tap but do not emit yet (wait for sequence)
        }

        return if (tapCount > 0 && timestamp - lastTapTime > tapSequenceTimeoutMs) {
            // Tap sequence timed out: emit if at least two taps were registered
            val event = if (tapCount >= 2) TapOnBackEvent else null
            tapCount = 0
            event
        } else {
            null // No tap and no timeout
        }
    }

    private fun updateBaseline(
        ax: Float,
        ay: Float,
        az: Float,
    ) {
        // Exponentially smooth accelerometer readings to estimate the gravity vector
        if (hasBaseline) {
            baselineX = baselineX * SMOOTHING_ALPHA + ax * (1f - SMOOTHING_ALPHA)
            baselineY = baselineY * SMOOTHING_ALPHA + ay * (1f - SMOOTHING_ALPHA)
            baselineZ = baselineZ * SMOOTHING_ALPHA + az * (1f - SMOOTHING_ALPHA)
        } else {
            baselineX = ax
            baselineY = ay
            baselineZ = az
            hasBaseline = true
        }
    }

    private fun computeAngleFromBaseline(
        ax: Float,
        ay: Float,
        az: Float,
        accelMag: Float,
    ): Float {
        // Compute the angle between the current reading and the baseline using the dot product
        val baseMag = sqrt(baselineX * baselineX + baselineY * baselineY + baselineZ * baselineZ)
        val dotProduct = ax * baselineX + ay * baselineY + az * baselineZ
        val cosAngle = (dotProduct / (accelMag * baseMag)).coerceIn(-1f, 1f)
        return Math.toDegrees(acos(cosAngle.toDouble())).toFloat()
    }

    private fun isValidTap(
        angleDeg: Float,
        angleJerk: Float,
        timestamp: Long,
    ): Boolean =
        // Validate: angle exceeds threshold, sufficient jerk, and debounce elapsed
        angleDeg > angleThreshold &&
            timestamp - lastTapTime > tapDebounceMs &&
            angleJerk > minAngleJerk

    companion object {
        private const val SMOOTHING_ALPHA = 0.95f // EMA smoothing factor for baseline (higher = slower adaptation)
    }
}
