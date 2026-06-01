
package com.github.nisrulz.sensey.gesture.touch

import com.github.nisrulz.sensey.gesture.touch.internal.classifyAxisDominantDirection
import com.github.nisrulz.sensey.gesture.touch.internal.classifyEdge
import kotlin.math.sqrt

internal class EdgeSwipeTrigger(
    private val edgeThresholdPx: Float,
    private val enabledEdges: Set<TouchEvent.EdgeType>,
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
        val edge = classifyEdge(startX, startY, screenW, screenH, edgeThresholdPx) ?: return null
        if (edge !in enabledEdges) return null
        val dx = endX - startX
        val dy = endY - startY
        val distance = sqrt(dx * dx + dy * dy)
        if (distance < minDistance) return null
        val direction = classifyAxisDominantDirection(dx, dy)
        return TouchEvent.Swipe(direction, TouchEvent.SwipeOrigin.Edge(edge), fingerCount = 1)
    }
}
