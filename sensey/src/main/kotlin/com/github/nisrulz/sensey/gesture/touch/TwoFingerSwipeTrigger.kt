
package com.github.nisrulz.sensey.gesture.touch

import com.github.nisrulz.sensey.gesture.touch.internal.classifyAxisDominantDirection
import com.github.nisrulz.sensey.gesture.touch.internal.isDiagonalDirection
import kotlin.math.sqrt

internal class TwoFingerSwipeTrigger(
    private val minDistance: Float = 80f,
    private val diagonalOnly: Boolean = false,
) : TouchGesture.TwoFinger {
    override fun evaluate(
        panX: Float,
        panY: Float,
    ): TouchEvent? {
        val distance = sqrt(panX * panX + panY * panY)
        if (distance < minDistance) return null
        val direction = classifyAxisDominantDirection(panX, panY)
        if (diagonalOnly && !isDiagonalDirection(direction)) return null
        return TouchEvent.Swipe(direction, TouchEvent.SwipeOrigin.Any, fingerCount = 2)
    }
}
