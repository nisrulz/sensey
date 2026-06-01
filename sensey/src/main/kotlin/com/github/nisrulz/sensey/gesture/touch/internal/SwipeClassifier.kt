
package com.github.nisrulz.sensey.gesture.touch.internal

import com.github.nisrulz.sensey.gesture.touch.TouchEvent.CornerType
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.EdgeType
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.SwipeOrigin

internal fun classifySwipeOrigin(
    startX: Float,
    startY: Float,
    screenW: Float,
    screenH: Float,
    edgeThreshold: Float,
    cornerThreshold: Float,
    enabledOrigins: Set<Class<*>>,
    enabledEdges: Set<EdgeType> = EdgeType.entries.toSet(),
    enabledCorners: Set<CornerType> = CornerType.entries.toSet(),
): SwipeOrigin? {
    if (enabledOrigins.contains(SwipeOrigin.Corner::class.java)) {
        val corner = classifyCorner(startX, startY, screenW, screenH, cornerThreshold)
        if (corner != null && corner in enabledCorners) {
            return SwipeOrigin.Corner(corner)
        }
    }
    if (enabledOrigins.contains(SwipeOrigin.Edge::class.java)) {
        val edge = classifyEdge(startX, startY, screenW, screenH, edgeThreshold)
        if (edge != null && edge in enabledEdges) {
            return SwipeOrigin.Edge(edge)
        }
    }
    return if (enabledOrigins.contains(SwipeOrigin.Any::class.java)) SwipeOrigin.Any else null
}

internal fun classifyCorner(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    cornerRadiusPx: Float,
): CornerType? =
    when {
        x <= cornerRadiusPx && y <= cornerRadiusPx -> CornerType.TOP_LEFT
        x >= w - cornerRadiusPx && y <= cornerRadiusPx -> CornerType.TOP_RIGHT
        x <= cornerRadiusPx && y >= h - cornerRadiusPx -> CornerType.BOTTOM_LEFT
        x >= w - cornerRadiusPx && y >= h - cornerRadiusPx -> CornerType.BOTTOM_RIGHT
        else -> null
    }

internal fun classifyEdge(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    edgeThreshold: Float,
): EdgeType? {
    if (x <= edgeThreshold) return EdgeType.LEFT
    if (x >= width - edgeThreshold) return EdgeType.RIGHT
    if (y <= edgeThreshold) return EdgeType.TOP
    if (y >= height - edgeThreshold) return EdgeType.BOTTOM
    return null
}
