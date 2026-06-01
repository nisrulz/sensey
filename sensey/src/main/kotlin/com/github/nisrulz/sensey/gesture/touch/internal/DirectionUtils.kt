
package com.github.nisrulz.sensey.gesture.touch.internal

import com.github.nisrulz.sensey.gesture.touch.TouchEvent.Direction
import kotlin.math.abs
import kotlin.math.atan2

internal fun classifyDirection(
    deltaX: Float,
    deltaY: Float,
): Direction {
    val degrees = Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble()))
    return when {
        degrees in -22.5..22.5 -> Direction.RIGHT
        degrees in 22.5..67.5 -> Direction.DOWN_RIGHT
        degrees in 67.5..112.5 -> Direction.DOWN
        degrees in 112.5..157.5 -> Direction.DOWN_LEFT
        degrees > 157.5 || degrees < -157.5 -> Direction.LEFT
        degrees in -157.5..-112.5 -> Direction.UP_LEFT
        degrees in -112.5..-67.5 -> Direction.UP
        degrees in -67.5..-22.5 -> Direction.UP_RIGHT
        else -> Direction.DOWN
    }
}

internal fun classifyAxisDominantDirection(
    deltaX: Float,
    deltaY: Float,
): Direction =
    if (abs(deltaX) > abs(deltaY)) {
        if (deltaX > 0) Direction.RIGHT else Direction.LEFT
    } else {
        if (deltaY > 0) Direction.DOWN else Direction.UP
    }

internal fun isDiagonalDirection(direction: Direction): Boolean =
    direction == Direction.UP_RIGHT ||
        direction == Direction.UP_LEFT ||
        direction == Direction.DOWN_RIGHT ||
        direction == Direction.DOWN_LEFT

internal fun isAboveVelocityThreshold(
    vx: Float,
    vy: Float,
    threshold: Float,
): Boolean = abs(vx) > threshold || abs(vy) > threshold
