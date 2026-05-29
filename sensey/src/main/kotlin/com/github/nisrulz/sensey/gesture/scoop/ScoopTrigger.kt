
package com.github.nisrulz.sensey.gesture.scoop

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

internal class ScoopTrigger(
    private val impulseThreshold: Float = 10f,
    private val minPeakJerk: Float = 3.0f,
    private val minSustainedSamples: Int = 3,
    private val debounceMs: Long = 1000L,
    private val baselineSamples: Int = 10,
) : GestureTrigger<ScoopEvent> {
    private var accelBaseline = GRAVITY_EARTH
    private var previousAccelMag = GRAVITY_EARTH
    private var lastEventTime = 0L
    private var samplesAboveThreshold = 0
    private var peakJerkInWindow = 0f
    private var baselineReadings = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ScoopEvent? {
        val accelMag = computeMagnitude(values)
        if (baselineReadings < baselineSamples) {
            accelBaseline = accelBaseline * SMOOTHING_ALPHA + accelMag * (1f - SMOOTHING_ALPHA)
            previousAccelMag = accelMag
            baselineReadings++
            return null
        }
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
        private const val SMOOTHING_ALPHA = 0.95f
    }
}
