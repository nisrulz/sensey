
package com.github.nisrulz.sensey.gesture.touch

import com.github.nisrulz.sensey.gesture.touch.internal.classifyAxisDominantDirection
import com.github.nisrulz.sensey.gesture.touch.internal.classifyCorner
import kotlin.math.sqrt

internal class CornerSwipeTrigger(
    private val cornerRadiusPx: Float,
    private val enabledCorners: Set<TouchEvent.CornerType>,
    private val screenW: Float,
    private val screenH: Float,
    private val minDistance: Float = 120f,
) : TouchGesture.ZoneSwipe {
    override fun evaluate(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
    ): TouchEvent? {
        val corner = classifyCorner(startX, startY, screenW, screenH, cornerRadiusPx) ?: return null
        if (corner !in enabledCorners) return null
        val dx = endX - startX
        val dy = endY - startY
        val distance = sqrt(dx * dx + dy * dy)
        if (distance < minDistance) return null
        val direction = classifyAxisDominantDirection(dx, dy)
        return TouchEvent.Swipe(direction, TouchEvent.SwipeOrigin.Corner(corner), fingerCount = 1)
    }
}
