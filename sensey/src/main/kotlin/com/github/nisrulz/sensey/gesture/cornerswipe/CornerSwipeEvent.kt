
package com.github.nisrulz.sensey.gesture.cornerswipe

/**
 * Event emitted when a swipe originating from a screen corner is detected.
 *
 * @property corner the corner where the swipe originated
 * @property direction the swipe direction
 */
data class CornerSwipeEvent(
    val corner: Corner,
    val direction: Direction,
) {
    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }

    enum class Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }
}
