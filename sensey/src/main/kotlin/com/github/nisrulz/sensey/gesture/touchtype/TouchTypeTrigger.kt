
package com.github.nisrulz.sensey.gesture.touchtype

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Classifies touch events as swipes or scrolls with directional information.
 *
 * Algorithm: Computes Euclidean distance and optional velocity from touch
 * delta values. If distance is below a minimum threshold the event is ignored.
 * Velocity above threshold marks the gesture as a swipe (vs scroll). Direction
 * is determined by partitioning the atan2 angle into eight quadrants. Each
 * quadrant maps to a different Direction for swipes (e.g. DOWN_RIGHT) vs
 * scrolls (e.g. DOWN) to reflect the coarse-grained nature of scroll flings.
 * Expected sensor: Touch input (via View.OnTouchListener or similar).
 * State: None (stateless computation).
 */
internal class TouchTypeTrigger(
    private val swipeMinDistance: Float = 120f,
    private val swipeThresholdVelocity: Float = 200f,
) : GestureTrigger<TouchTypeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TouchTypeEvent? {
        if (values.size < 2) return null // Need at least deltaX and deltaY

        val deltaX = values[0] // Horizontal displacement
        val deltaY = values[1] // Vertical displacement
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY) // Euclidean distance
        if (distance < swipeMinDistance) return null // Too short to be a swipe or scroll

        val velocityX = values.getOrNull(2) ?: 0f // Horizontal velocity (optional input)
        val velocityY = values.getOrNull(3) ?: 0f // Vertical velocity (optional input)
        val isSwipe = isAboveVelocityThreshold(velocityX, velocityY) // Classify as swipe if velocity exceeds threshold

        val direction = classifyDirection(deltaX, deltaY, isSwipe) // Determine direction from displacement angle
        return if (isSwipe) TouchTypeEvent.Swipe(direction) else TouchTypeEvent.Scroll(direction) // Emit swipe or scroll event
    }

    private fun isAboveVelocityThreshold(
        vx: Float,
        vy: Float,
    ): Boolean = abs(vx) > swipeThresholdVelocity || abs(vy) > swipeThresholdVelocity
    // True if either velocity component exceeds the threshold

    private fun classifyDirection(
        deltaX: Float,
        deltaY: Float,
        isSwipe: Boolean,
    ): TouchTypeEvent.Direction {
        // Map the displacement angle to a Direction via a quadrant lookup
        val degrees = Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble()))
        val quadrant = classifyQuadrant(degrees)
        return if (isSwipe) quadrant.swipeDirection else quadrant.scrollDirection
    }

    private fun classifyQuadrant(degrees: Double): Quadrant =
        // Partition 360° into eight directional quadrants of 45° each
        when {
            degrees in -22.5..22.5 -> Quadrant.RIGHT
            degrees in 22.5..67.5 -> Quadrant.DOWN_RIGHT
            degrees in 67.5..112.5 -> Quadrant.DOWN
            degrees in 112.5..157.5 -> Quadrant.DOWN_LEFT
            degrees > 157.5 || degrees < -157.5 -> Quadrant.LEFT
            degrees in -157.5..-112.5 -> Quadrant.UP_LEFT
            degrees in -112.5..-67.5 -> Quadrant.UP
            degrees in -67.5..-22.5 -> Quadrant.UP_RIGHT
            else -> Quadrant.DOWN
        }

    private enum class Quadrant(
        val scrollDirection: TouchTypeEvent.Direction,
        val swipeDirection: TouchTypeEvent.Direction,
    ) {
        // Each quadrant maps to a coarse scroll direction and a precise swipe direction
        RIGHT(TouchTypeEvent.Direction.RIGHT, TouchTypeEvent.Direction.RIGHT),
        DOWN_RIGHT(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN_RIGHT),
        DOWN(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN),
        DOWN_LEFT(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN_LEFT),
        LEFT(TouchTypeEvent.Direction.LEFT, TouchTypeEvent.Direction.LEFT),
        UP_LEFT(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP_LEFT),
        UP(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP),
        UP_RIGHT(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP_RIGHT),
    }
}
