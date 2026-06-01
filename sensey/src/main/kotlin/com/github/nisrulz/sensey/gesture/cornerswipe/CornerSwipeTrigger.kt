
package com.github.nisrulz.sensey.gesture.cornerswipe

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs

/**
 * Classifies corner swipe gestures by origin corner and swipe direction.
 *
 * Algorithm: Determines the starting corner by checking whether the press
 * is within [cornerRadiusPx] of any enabled corner. Then computes the
 * dominant direction from start to end position. The origin corner is
 * filtered against [enabledCorners] before emitting.
 * Expected sensor: Touch input (via [androidx.compose.foundation.gestures.detectDragGestures]).
 * State: None (stateless).
 */
internal class CornerSwipeTrigger(
    private val cornerRadiusPx: Float = 48f,
    private val enabledCorners: Set<CornerSwipeEvent.Corner> = CornerSwipeEvent.Corner.entries.toSet(),
) : GestureTrigger<CornerSwipeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): CornerSwipeEvent? {
        if (values.size < 6) return null
        val startX = values[0]
        val startY = values[1]
        val endX = values[2]
        val endY = values[3]
        val screenW = values[4]
        val screenH = values[5]

        val corner = classifyCorner(startX, startY, screenW, screenH) ?: return null
        if (corner !in enabledCorners) return null
        val direction = classifyDirection(startX, startY, endX, endY)
        return CornerSwipeEvent(corner, direction)
    }

    private fun classifyCorner(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
    ): CornerSwipeEvent.Corner? =
        when {
            x < cornerRadiusPx && y < cornerRadiusPx -> CornerSwipeEvent.Corner.TOP_LEFT
            x > w - cornerRadiusPx && y < cornerRadiusPx -> CornerSwipeEvent.Corner.TOP_RIGHT
            x < cornerRadiusPx && y > h - cornerRadiusPx -> CornerSwipeEvent.Corner.BOTTOM_LEFT
            x > w - cornerRadiusPx && y > h - cornerRadiusPx -> CornerSwipeEvent.Corner.BOTTOM_RIGHT
            else -> null
        }

    private fun classifyDirection(
        sx: Float,
        sy: Float,
        ex: Float,
        ey: Float,
    ): CornerSwipeEvent.Direction {
        val dx = ex - sx
        val dy = ey - sy
        return if (abs(dx) > abs(dy)) {
            if (dx > 0) CornerSwipeEvent.Direction.RIGHT else CornerSwipeEvent.Direction.LEFT
        } else {
            if (dy > 0) CornerSwipeEvent.Direction.DOWN else CornerSwipeEvent.Direction.UP
        }
    }
}
