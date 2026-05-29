
package com.github.nisrulz.sensey.gesture.chop

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects a chopping gesture.
 *
 * Algorithm: Monitors the linear acceleration (total magnitude minus gravity).
 * When a single impulse exceeds the threshold the gesture window starts.
 * The gesture is considered complete when no further impulses occur within
 * the configured timeout.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
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
        val linearAccel = computeLinearAcceleration(values) // Compute linear accel (total magnitude minus gravity)
        if (linearAccel > threshold) {
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

    private fun computeLinearAcceleration(values: FloatArray): Float {
        // Remove the gravity component from raw acceleration to get linear acceleration magnitude
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        return abs(magnitude - GRAVITY_EARTH)
    }

    private fun hasGestureCompleted(timestamp: Long): Boolean {
        // True if enough time has elapsed since the last impulse while a gesture was in progress
        val timeSinceLastMotion = timestamp - lastChopTime
        return timeSinceLastMotion > timeForChopGesture && isGestureInProgress
    }
}
