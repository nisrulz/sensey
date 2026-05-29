
package com.github.nisrulz.sensey.gesture.movement

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Detects device movement and stationary states.
 *
 * Algorithm: Computes the Euclidean norm (magnitude) of the raw acceleration vector.
 * Tracks the magnitude change between consecutive readings; if the absolute delta
 * exceeds a threshold the device is considered moving. If no movement occurs within
 * a configurable timeout the device is declared stationary. Also reports the
 * dominant spatial direction of the movement.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: currentAccel (current magnitude), isMoving (motion flag), lastMovementTime
 * (timestamp of last motion), hasBaseline (first-read guard).
 */
internal class MovementTrigger(
    private val threshold: Float = 0.3f,
    private val timeBeforeDeclaringStationary: Long = 5000L,
) : GestureTrigger<MovementEvent> {
    private var currentAccel = GRAVITY_EARTH // Current filtered acceleration magnitude
    private var isMoving = false // Whether the device is currently in motion
    private var lastMovementTime = 0L // Timestamp of the last detected movement
    private var hasBaseline = false // Whether the first sensor reading has been established

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): MovementEvent? {
        val previousAccel = currentAccel // Save the previous magnitude for delta calculation
        currentAccel = computeMagnitude(values) // Compute the current acceleration magnitude
        if (!hasBaseline) {
            hasBaseline = true
            return null // First reading: skip, establish baseline
        }
        val delta = abs(currentAccel - previousAccel) // Compute the change in acceleration

        return if (delta > threshold) {
            lastMovementTime = timestamp
            isMoving = true
            MovementEvent.Moved(dominantDirection(values)) // Movement detected, emit with dominant direction
        } else if (hasBecomeStationary(timestamp)) {
            isMoving = false
            MovementEvent.Stationary // No movement for timeout → emit stationary
        } else {
            null // No transition
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        // Euclidean norm of the acceleration vector
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun dominantDirection(values: FloatArray): MovementEvent.Direction {
        // Determine the axis with the largest absolute acceleration component
        val absValues = floatArrayOf(kotlin.math.abs(values[0]), kotlin.math.abs(values[1]), kotlin.math.abs(values[2]))
        val maxIndex = absValues.indices.maxByOrNull { absValues[it] } ?: 0
        return when (maxIndex) {
            0 -> if (values[0] > 0) MovementEvent.Direction.X_POS else MovementEvent.Direction.X_NEG
            1 -> if (values[1] > 0) MovementEvent.Direction.Y_POS else MovementEvent.Direction.Y_NEG
            else -> if (values[2] > 0) MovementEvent.Direction.Z_POS else MovementEvent.Direction.Z_NEG
        }
    }

    private fun hasBecomeStationary(timestamp: Long): Boolean {
        // True if enough time has passed since the last movement while previously moving
        val timeSinceLastMovement = timestamp - lastMovementTime
        return timeSinceLastMovement > timeBeforeDeclaringStationary && isMoving
    }
}
