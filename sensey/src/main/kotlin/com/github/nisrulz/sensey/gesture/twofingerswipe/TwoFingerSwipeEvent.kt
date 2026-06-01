
package com.github.nisrulz.sensey.gesture.twofingerswipe

/**
 * Event emitted when a two-finger directional swipe is detected.
 *
 * @property direction the swipe direction
 */
data class TwoFingerSwipeEvent(
    val direction: Direction,
) {
    enum class Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }
}
