
package com.github.nisrulz.sensey.gesture.longpressdrag

/**
 * Event emitted when a long press followed by a directional drag is detected.
 *
 * @property direction the dominant drag direction
 * @property distance total drag distance in pixels
 */
data class LongPressDragEvent(
    val direction: Direction,
    val distance: Float,
) {
    enum class Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }
}
