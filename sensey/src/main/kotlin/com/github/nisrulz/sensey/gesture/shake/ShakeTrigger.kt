
package com.github.nisrulz.sensey.gesture.shake

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects shake gestures.
 *
 * Algorithm: Computes the Euclidean magnitude of raw acceleration and tracks
 * it with a single-pole (EMA) smoothed delta. When the smoothed delta
 * exceeds the threshold a shake is reported. If no new shake impulse occurs
 * within the configured timeout a stopped event is emitted.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: accelCurrent (current magnitude), accelDelta (EMA-smoothed delta),
 * isShaking (shake flag), lastShakeTime (timeout), hasBaseline
 * (first-read guard).
 */
internal class ShakeTrigger(
    private val threshold: Float = 3f,
    private val timeBeforeDeclaringShakeStopped: Long = 1000L,
) : GestureTrigger<ShakeEvent> {
    private var accelCurrent = 0f // Current smoothed acceleration magnitude
    private var accelDelta = 0f // EMA-smoothed acceleration delta (for shake detection)
    private var isShaking = false // Whether the device is currently shaking
    private var lastShakeTime = 0L // Timestamp of the last detected shake impulse
    private var hasBaseline = false // Whether the initial baseline has been established

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ShakeEvent? {
        val magnitude = computeMagnitude(values) // Compute the Euclidean norm
        if (!hasBaseline) return initializeBaseline(magnitude) // First reading: establish baseline

        val delta = magnitude - accelCurrent // Compute the raw delta
        accelCurrent = magnitude
        accelDelta = accelDelta * SMOOTHING_FACTOR + delta // Apply EMA smoothing to the delta

        if (isShakingStopped(timestamp)) {
            isShaking = false
            return ShakeEvent.Stopped // No shake for the timeout period → declare stopped
        }

        if (accelDelta > threshold) {
            lastShakeTime = timestamp
            isShaking = true
            return ShakeEvent.Detected // Smoothed delta exceeds threshold → shake detected
        }

        return null // No significant shake activity
    }

    private fun initializeBaseline(magnitude: Float): ShakeEvent? {
        // Set the initial magnitude and reset the smoothed delta
        accelCurrent = magnitude
        accelDelta = 0f
        hasBaseline = true
        return null
    }

    private fun computeMagnitude(values: FloatArray): Float =
        // Euclidean norm of the acceleration vector
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun isShakingStopped(timestamp: Long): Boolean {
        // True if no shake impulse occurred within the timeout while the device was shaking
        val timeSinceLastShake = timestamp - lastShakeTime
        return timeSinceLastShake > timeBeforeDeclaringShakeStopped && isShaking
    }

    companion object {
        private const val SMOOTHING_FACTOR = 0.9f // EMA smoothing factor for the acceleration delta
    }
}
