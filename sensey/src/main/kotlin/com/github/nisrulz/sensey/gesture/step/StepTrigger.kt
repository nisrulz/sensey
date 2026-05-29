
package com.github.nisrulz.sensey.gesture.step

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects steps using either the hardware step-counter sensor or
 * accelerometer-based peak detection.
 *
 * Algorithm: Supports two input modes:
 * 1. Step-counter sensor (single value): subtracts the initial baseline and
 *    dispatches the cumulative step count, deduplicating against the last
 *    dispatched value.
 * 2. Accelerometer (3 values): detects steps by monitoring acceleration
 *    magnitude peaks above a threshold.
 * Each event includes distance and activity type computed via StepDetectorUtil.
 * Expected sensor: Step counter (TYPE_STEP_COUNTER) or accelerometer
 * (TYPE_ACCELEROMETER).
 * State: steps (accumulated count), previousMagnitude (accelerometer mode),
 * startTime (for activity type), baseStepCount (step-counter mode),
 * lastDispatchedSteps (deduplication).
 */
internal class StepTrigger(
    private val gender: Int = StepDetectorUtil.MALE,
    private val threshold: Float = 3f,
) : GestureTrigger<StepEvent> {
    private var steps = 0 // Accumulated step count
    private var previousMagnitude = 0f // Previous acceleration magnitude for delta detection (accelerometer mode)
    private var startTime = 0L // Timestamp of the last step event (for activity-type calculation)
    private var baseStepCount = 0 // Baseline step-counter value at start (for step-counter sensor mode)
    private var lastDispatchedSteps = -1 // Last dispatched step count (for deduplication)

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? =
        when {
            values.size == 1 -> evaluateStepCounter(values[0], timestamp) // Single value: step-counter sensor mode
            values.size >= 3 -> evaluateAccelerometer(values, timestamp) // Three values: accelerometer mode
            else -> null // Unsupported input size
        }

    private fun evaluateStepCounter(
        sensorValue: Float,
        timestamp: Long,
    ): StepEvent? {
        // Process hardware step-counter sensor: subtract baseline and deduplicate
        if (baseStepCount < 1) {
            baseStepCount = sensorValue.toInt() // First reading: establish baseline
            return null
        }
        val currentSteps = sensorValue.toInt() - baseStepCount
        if (currentSteps == lastDispatchedSteps) return null // No new steps since last dispatch
        lastDispatchedSteps = currentSteps
        steps = currentSteps
        return buildStepEvent(timestamp) // Emit step event with current count
    }

    private fun evaluateAccelerometer(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? {
        // Process accelerometer-based step detection via magnitude peaks
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        val stepDetected = abs(magnitude - previousMagnitude) > threshold // Delta exceeds threshold → step
        previousMagnitude = magnitude
        if (!stepDetected) return null
        steps++
        return buildStepEvent(timestamp) // Emit step event with incremented count
    }

    private fun buildStepEvent(timestamp: Long): StepEvent {
        // Build the step event with distance and activity type from the utility
        val distance = StepDetectorUtil.getDistanceCovered(steps, gender)
        val timeDelta = timestamp - startTime
        startTime = timestamp
        val activityType = StepDetectorUtil.getStepActivityType(distance, timeDelta)
        return StepEvent(steps, distance, activityType)
    }
}
