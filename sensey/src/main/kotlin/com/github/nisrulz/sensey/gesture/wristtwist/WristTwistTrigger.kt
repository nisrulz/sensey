
package com.github.nisrulz.sensey.gesture.wristtwist

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects a wrist-twisting gesture.
 *
 * Algorithm: Monitors the linear acceleration magnitude. When a single
 * impulse exceeds the threshold the gesture window starts. The gesture
 * is considered complete when no further impulses occur within the
 * configured timeout.
 * Expected sensor: Linear Acceleration (TYPE_LINEAR_ACCELERATION).
 * State: isGestureInProgress (window active flag), lastTwistTime
 * (timestamp of the last impulse that exceeded threshold).
 */
internal class WristTwistTrigger(
    private val threshold: Float = 12f,
    private val timeForWristTwistGesture: Long = 1000L,
) : GestureTrigger<WristTwistEvent> {
    private var isGestureInProgress = false // Whether a wrist-twist gesture window is currently active
    private var lastTwistTime = 0L // Timestamp of the last impulse that exceeded the threshold

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): WristTwistEvent? {
        val magnitude = computeMagnitude(values) // Euclidean norm of linear acceleration
        if (magnitude > threshold) {
            lastTwistTime = timestamp
            isGestureInProgress = true
            return null // Impulse detected, start/refresh the gesture window
        }
        return if (hasGestureCompleted(timestamp)) {
            isGestureInProgress = false
            WristTwistEvent.Twisted // No further impulses within timeout → emit twist
        } else {
            null // Still within the gesture window or nothing detected
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun hasGestureCompleted(timestamp: Long): Boolean {
        // True if enough time has elapsed since the last impulse while a gesture was in progress
        val timeSinceLastMotion = timestamp - lastTwistTime
        return timeSinceLastMotion > timeForWristTwistGesture && isGestureInProgress
    }
}
