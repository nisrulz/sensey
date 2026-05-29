
package com.github.nisrulz.sensey.gesture.wristtwist

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects a wrist-twisting gesture.
 *
 * Algorithm: Monitors the linear acceleration (total magnitude minus gravity).
 * When a single impulse exceeds the threshold the gesture window starts.
 * The gesture is considered complete when no further impulses occur within
 * the configured timeout.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
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
        val linearAccel = computeLinearAcceleration(values) // Compute linear accel (total magnitude minus gravity)
        if (linearAccel > threshold) {
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

    private fun computeLinearAcceleration(values: FloatArray): Float {
        // Remove the gravity component from raw acceleration to get linear acceleration magnitude
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        return abs(magnitude - GRAVITY_EARTH)
    }

    private fun hasGestureCompleted(timestamp: Long): Boolean {
        // True if enough time has elapsed since the last impulse while a gesture was in progress
        val timeSinceLastMotion = timestamp - lastTwistTime
        return timeSinceLastMotion > timeForWristTwistGesture && isGestureInProgress
    }
}
