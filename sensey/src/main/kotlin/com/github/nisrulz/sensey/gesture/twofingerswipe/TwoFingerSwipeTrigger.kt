
package com.github.nisrulz.sensey.gesture.twofingerswipe

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Classifies two-finger touch gestures as directional swipes.
 *
 * Algorithm: Receives [panX, panY] from
 * [androidx.compose.foundation.gestures.detectTransformGestures].
 * Direction is determined by the dominant pan axis once the pan exceeds
 * [minDragDistance]. Zoom and rotation are intentionally ignored — a
 * natural two-finger swipe nearly always includes some jitter.
 * Expected sensor: Touch input (two-finger gesture).
 * State: None (stateless).
 */
internal class TwoFingerSwipeTrigger(
    private val minDragDistance: Float = 40f,
) : GestureTrigger<TwoFingerSwipeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TwoFingerSwipeEvent? {
        if (values.size < 8) return null
        val panX = values[4]
        val panY = values[5]

        val distance = sqrt(panX * panX + panY * panY)
        if (distance < minDragDistance) return null

        val direction =
            if (abs(panX) > abs(panY)) {
                if (panX > 0) TwoFingerSwipeEvent.Direction.RIGHT else TwoFingerSwipeEvent.Direction.LEFT
            } else {
                if (panY > 0) TwoFingerSwipeEvent.Direction.DOWN else TwoFingerSwipeEvent.Direction.UP
            }
        return TwoFingerSwipeEvent(direction)
    }
}
