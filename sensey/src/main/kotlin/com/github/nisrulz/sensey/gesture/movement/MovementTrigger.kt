
package com.github.nisrulz.sensey.gesture.movement

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GRAVITY_EARTH
import kotlin.math.abs
import kotlin.math.sqrt

internal class MovementTrigger(
    private val threshold: Float = 0.3f,
    private val timeBeforeDeclaringStationary: Long = 5000L,
) : GestureTrigger<MovementEvent> {
    private var currentAccel = GRAVITY_EARTH
    private var isMoving = false
    private var lastMovementTime = 0L
    private var hasBaseline = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): MovementEvent? {
        val previousAccel = currentAccel
        currentAccel = computeMagnitude(values)
        if (!hasBaseline) {
            hasBaseline = true
            return null
        }
        val delta = abs(currentAccel - previousAccel)

        return if (delta > threshold) {
            lastMovementTime = timestamp
            isMoving = true
            MovementEvent.Moved(dominantDirection(values))
        } else if (hasBecomeStationary(timestamp)) {
            isMoving = false
            MovementEvent.Stationary
        } else {
            null
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun dominantDirection(values: FloatArray): MovementEvent.Direction {
        val absValues = floatArrayOf(kotlin.math.abs(values[0]), kotlin.math.abs(values[1]), kotlin.math.abs(values[2]))
        val maxIndex = absValues.indices.maxByOrNull { absValues[it] } ?: 0
        return when (maxIndex) {
            0 -> if (values[0] > 0) MovementEvent.Direction.X_POS else MovementEvent.Direction.X_NEG
            1 -> if (values[1] > 0) MovementEvent.Direction.Y_POS else MovementEvent.Direction.Y_NEG
            else -> if (values[2] > 0) MovementEvent.Direction.Z_POS else MovementEvent.Direction.Z_NEG
        }
    }

    private fun hasBecomeStationary(timestamp: Long): Boolean {
        val timeSinceLastMovement = timestamp - lastMovementTime
        return timeSinceLastMovement > timeBeforeDeclaringStationary && isMoving
    }


}
