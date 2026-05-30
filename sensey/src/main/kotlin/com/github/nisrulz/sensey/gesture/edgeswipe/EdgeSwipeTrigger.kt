
package com.github.nisrulz.sensey.gesture.edgeswipe

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

internal class EdgeSwipeTrigger(
    private val edgeThreshold: Float = 48f,
    private val enabledEdges: Set<Edge> = setOf(Edge.LEFT, Edge.RIGHT, Edge.TOP, Edge.BOTTOM),
) : GestureTrigger<EdgeSwipeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): EdgeSwipeEvent? {
        if (values.size < 6) return null
        val startX = values[0]
        val startY = values[1]
        val endX = values[2]
        val endY = values[3]
        val width = values[4]
        val height = values[5]

        val dragDistance = sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY))
        if (dragDistance < edgeThreshold) return null

        val edge = findEdge(startX, startY, width, height) ?: return null
        return EdgeSwipeEvent(edge)
    }

    private fun findEdge(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    ): Edge? {
        for (edge in enabledEdges) {
            val near =
                when (edge) {
                    Edge.LEFT -> x <= edgeThreshold
                    Edge.RIGHT -> x >= width - edgeThreshold
                    Edge.TOP -> y <= edgeThreshold
                    Edge.BOTTOM -> y >= height - edgeThreshold
                }
            if (near) return edge
        }
        return null
    }
}
