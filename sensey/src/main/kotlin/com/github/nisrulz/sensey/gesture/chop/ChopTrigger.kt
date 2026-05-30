
package com.github.nisrulz.sensey.gesture.chop

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects a chopping gesture.
 *
 * Algorithm: Monitors the linear acceleration magnitude. When a single
 * impulse exceeds the threshold the gesture window starts. The gesture
 * is considered complete when no further impulses occur within the
 * configured timeout.
 * Expected sensor: Linear Acceleration (TYPE_LINEAR_ACCELERATION).
 * State: isGestureInProgress (window active flag), lastChopTime
 * (timestamp of the last impulse that exceeded threshold).
 */
internal class ChopTrigger(
    private val threshold: Float = 25f,
    private val timeForChopGesture: Long = 700L,
) : GestureTrigger<ChopEvent> {
    private var isGestureInProgress = false // Whether a chop gesture window is currently active
    private var lastChopTime = 0L // Timestamp of the last impulse that exceeded the threshold

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ChopEvent? {
        val magnitude = computeMagnitude(values) // Euclidean norm of linear acceleration
        if (magnitude > threshold) {
            lastChopTime = timestamp
            isGestureInProgress = true
            return null // Impulse detected, start/refresh the gesture window
        }
        return if (hasGestureCompleted(timestamp)) {
            isGestureInProgress = false
            ChopEvent.Chopped // No further impulses within timeout → emit chop
        } else {
            null // Still within the gesture window or nothing detected
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun hasGestureCompleted(timestamp: Long): Boolean {
        // True if enough time has elapsed since the last impulse while a gesture was in progress
        val timeSinceLastMotion = timestamp - lastChopTime
        return timeSinceLastMotion > timeForChopGesture && isGestureInProgress
    }
}
