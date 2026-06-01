
package com.github.nisrulz.sensey.gesture.touch

import com.github.nisrulz.sensey.gesture.touch.internal.classifyDirection
import com.github.nisrulz.sensey.gesture.touch.internal.isAboveVelocityThreshold
import com.github.nisrulz.sensey.gesture.touch.internal.isDiagonalDirection
import kotlin.math.sqrt

internal class SwipeScrollTrigger(
    private val minDistance: Float = 120f,
    private val velocityThreshold: Float = 200f,
    private val diagonalOnly: Boolean = false,
) : TouchGesture.SwipeScroll {
    override fun evaluate(
        deltaX: Float,
        deltaY: Float,
        velocityX: Float,
        velocityY: Float,
    ): TouchEvent? {
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (distance < minDistance) return null

        val isSwipe = isAboveVelocityThreshold(velocityX, velocityY, velocityThreshold)
        val direction = classifyDirection(deltaX, deltaY)

        if (diagonalOnly && !isDiagonalDirection(direction)) return null

        val origin = TouchEvent.SwipeOrigin.Any
        return if (isSwipe) {
            TouchEvent.Swipe(direction, origin, fingerCount = 1)
        } else {
            TouchEvent.Scroll(direction)
        }
    }
}
