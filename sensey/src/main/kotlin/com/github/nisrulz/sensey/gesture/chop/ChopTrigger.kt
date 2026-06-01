
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
    private val threshold: Float = 35f,
    private val timeForChopGesture: Long = 700L,
) : GestureTrigger<ChopEvent> {
    private var isGestureInProgress = false
    private var lastChopTime = 0L
    private var lastFireTime = -1L

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ChopEvent? {
        if (lastFireTime != -1L && timestamp < lastFireTime + timeForChopGesture) return null

        val magnitude = computeMagnitude(values)
        if (magnitude > threshold) {
            lastChopTime = timestamp
            isGestureInProgress = true
            return null
        }
        return if (hasGestureCompleted(timestamp)) {
            isGestureInProgress = false
            lastFireTime = timestamp
            ChopEvent.Chopped
        } else {
            return null
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
