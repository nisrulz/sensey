
package com.github.nisrulz.sensey.gesture.diagonalswipe

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

internal class DiagonalSwipeTrigger(
    private val minDragDistance: Float = 80f,
    private val angleToleranceDeg: Float = 22.5f,
) : GestureTrigger<DiagonalSwipeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): DiagonalSwipeEvent? {
        if (values.size < 4) return null
        val startX = values[0]
        val startY = values[1]
        val endX = values[2]
        val endY = values[3]

        val dx = endX - startX
        val dy = endY - startY
        val distance = sqrt(dx * dx + dy * dy)
        if (distance < minDragDistance) return null

        if (abs(dx) < 1f || abs(dy) < 1f) return null

        val direction = classifyDiagonal(dx.toDouble(), dy.toDouble()) ?: return null
        return DiagonalSwipeEvent(direction)
    }

    private fun classifyDiagonal(
        dx: Double,
        dy: Double,
    ): DiagonalDirection? {
        val degrees = Math.toDegrees(atan2(dy, dx))
        return when {
            degrees in (45.0 - angleToleranceDeg)..(45.0 + angleToleranceDeg) -> DiagonalDirection.DOWN_RIGHT
            degrees in (135.0 - angleToleranceDeg)..(135.0 + angleToleranceDeg) -> DiagonalDirection.DOWN_LEFT
            degrees in (-135.0 - angleToleranceDeg)..(-135.0 + angleToleranceDeg) -> DiagonalDirection.UP_LEFT
            degrees in (-45.0 - angleToleranceDeg)..(-45.0 + angleToleranceDeg) -> DiagonalDirection.UP_RIGHT
            else -> null
        }
    }
}
