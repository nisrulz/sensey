
package com.github.nisrulz.sensey.gesture.touch

import com.github.nisrulz.sensey.gesture.touch.internal.classifyAxisDominantDirection
import kotlin.math.sqrt

internal class LongPressDragTrigger(
    private val minDistance: Float = 20f,
) : TouchGesture.Drag {
    override fun evaluate(
        deltaX: Float,
        deltaY: Float,
    ): TouchEvent? {
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (distance < minDistance) return null
        val direction = classifyAxisDominantDirection(deltaX, deltaY)
        return TouchEvent.LongPressDrag(direction, distance)
    }
}
