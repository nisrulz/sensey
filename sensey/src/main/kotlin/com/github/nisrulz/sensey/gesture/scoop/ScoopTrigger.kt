
package com.github.nisrulz.sensey.gesture.scoop

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects a scooping gesture.
 *
 * Algorithm: Maintains an EMA-smoothed acceleration baseline. Computes the
 * impulse (deviation from baseline) and jerk (change between consecutive
 * samples). When the impulse exceeds the threshold consecutive samples are
 * counted. A scoop is emitted when the sustained-sample count and the peak
 * jerk within the window both exceed their respective minima, subject to a
 * global debounce.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: accelBaseline (EMA baseline), previousAccelMag (for jerk),
 * lastEventTime (debounce), samplesAboveThreshold (sustained count),
 * peakJerkInWindow (max jerk), baselineReadings (initialization count).
 */
internal class ScoopTrigger(
    private val impulseThreshold: Float = 10f,
    private val minPeakJerk: Float = 3.0f,
    private val minSustainedSamples: Int = 1,
    private val debounceMs: Long = 0L,
    private val baselineSamples: Int = 0,
) : GestureTrigger<ScoopEvent> {
    private var accelBaseline = GRAVITY_EARTH // EMA-smoothed acceleration baseline
    private var previousAccelMag = GRAVITY_EARTH // Previous acceleration magnitude for jerk computation
    private var lastEventTime = 0L // Timestamp of the last dispatched scoop event (debounce)
    private var samplesAboveThreshold = 0 // Consecutive samples exceeding the impulse threshold
    private var peakJerkInWindow = 0f // Maximum jerk observed in the current impulse window
    private var baselineReadings = 0 // Number of baseline readings collected so far

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ScoopEvent? {
        val accelMag = computeMagnitude(values) // Compute the Euclidean norm of the acceleration vector
        if (baselineReadings < baselineSamples) {
            // Still in the initial baseline collection phase
            accelBaseline = accelBaseline * SMOOTHING_ALPHA + accelMag * (1f - SMOOTHING_ALPHA)
            previousAccelMag = accelMag
            baselineReadings++
            return null
        }
        updateBaseline(accelMag) // Continuously update the EMA baseline
        val impulse = abs(accelMag - accelBaseline) // Compute impulse (deviation from baseline)
        val jerk = abs(accelMag - previousAccelMag) // Compute jerk (change in acceleration)
        previousAccelMag = accelMag

        if (impulse > impulseThreshold) {
            samplesAboveThreshold++ // Above threshold: count this sample
            if (jerk > peakJerkInWindow) peakJerkInWindow = jerk // Track the peak jerk
        } else {
            samplesAboveThreshold = 0 // Below threshold: reset the window
            peakJerkInWindow = 0f
        }

        return if (isScoopDetected(timestamp)) {
            samplesAboveThreshold = 0
            peakJerkInWindow = 0f
            lastEventTime = timestamp
            ScoopEvent.Scooped // All conditions met → emit scoop
        } else {
            null
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        // Euclidean norm of the acceleration vector
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun updateBaseline(accelMag: Float) {
        // Continuously update the EMA baseline with the current magnitude
        accelBaseline = accelBaseline * SMOOTHING_ALPHA + accelMag * (1f - SMOOTHING_ALPHA)
    }

    private fun isScoopDetected(timestamp: Long): Boolean =
        // Validate: enough sustained samples, peak jerk exceeds minimum, and debounce elapsed
        samplesAboveThreshold >= minSustainedSamples &&
            peakJerkInWindow > minPeakJerk &&
            timestamp - lastEventTime >= debounceMs

    companion object {
        private const val SMOOTHING_ALPHA = 0.95f // EMA smoothing factor for baseline
    }
}
