
package com.github.nisrulz.sensey.gesture.longpressdrag

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Classifies long-press-then-drag gestures by direction and distance.
 *
 * Algorithm: Computes Euclidean distance from the drag delta. If below
 * [minDragDistance] the event is ignored. Direction is determined by the
 * dominant axis (horizontal wins over vertical). The delta values come
 * from [androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress].
 * State: None (stateless).
 */
internal class LongPressDragTrigger(
    private val minDragDistance: Float = 20f,
) : GestureTrigger<LongPressDragEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): LongPressDragEvent? {
        if (values.size < 2) return null
        val deltaX = values[0]
        val deltaY = values[1]
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (distance < minDragDistance) return null
        val direction =
            if (abs(deltaX) > abs(deltaY)) {
                if (deltaX > 0) LongPressDragEvent.Direction.RIGHT else LongPressDragEvent.Direction.LEFT
            } else {
                if (deltaY > 0) LongPressDragEvent.Direction.DOWN else LongPressDragEvent.Direction.UP
            }
        return LongPressDragEvent(direction, distance)
    }
}
