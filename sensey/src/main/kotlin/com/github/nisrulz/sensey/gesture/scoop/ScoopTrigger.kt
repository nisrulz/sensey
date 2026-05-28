
package com.github.nisrulz.sensey.gesture.scoop

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

internal class ScoopTrigger(
    private val impulseThreshold: Float = 10f,
    private val minPeakJerk: Float = 3.0f,
    private val minSustainedSamples: Int = 3,
    private val debounceMs: Long = 1000L,
) : GestureTrigger<ScoopEvent> {
    private var accelBaseline = GRAVITY_EARTH
    private var previousAccelMag = GRAVITY_EARTH
    private var lastEventTime = 0L
    private var samplesAboveThreshold = 0
    private var peakJerkInWindow = 0f

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ScoopEvent? {
        val accelMag = computeMagnitude(values)
        updateBaseline(accelMag)
        val impulse = abs(accelMag - accelBaseline)
        val jerk = abs(accelMag - previousAccelMag)
        previousAccelMag = accelMag

        if (impulse > impulseThreshold) {
            samplesAboveThreshold++
            if (jerk > peakJerkInWindow) peakJerkInWindow = jerk
        } else {
            samplesAboveThreshold = 0
            peakJerkInWindow = 0f
        }

        return if (isScoopDetected(timestamp)) {
            samplesAboveThreshold = 0
            peakJerkInWindow = 0f
            lastEventTime = timestamp
            ScoopEvent.Scooped
        } else {
            null
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun updateBaseline(accelMag: Float) {
        accelBaseline = accelBaseline * SMOOTHING_ALPHA + accelMag * (1f - SMOOTHING_ALPHA)
    }

    private fun isScoopDetected(timestamp: Long): Boolean =
        samplesAboveThreshold >= minSustainedSamples &&
            peakJerkInWindow > minPeakJerk &&
            timestamp - lastEventTime > debounceMs

    companion object {
        private const val GRAVITY_EARTH = 9.81f
        private const val SMOOTHING_ALPHA = 0.95f
    }
}
