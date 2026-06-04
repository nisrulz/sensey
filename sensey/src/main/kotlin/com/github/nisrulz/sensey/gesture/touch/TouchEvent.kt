
package com.github.nisrulz.sensey.gesture.touch

/**
 * Unified event hierarchy for all touch-based gesture detection.
 *
 * Consolidates tap, swipe, scroll, long-press-drag, and pinch-scale
 * events into a single sealed interface. The [Swipe] event carries
 * origin information ([SwipeOrigin]) so edge swipes and corner swipes
 * are expressed as [Swipe] with a specific origin rather than separate
 * event types.
 *
 * This is the event type used by [TouchPlugin], [touchPlugin], and all
 * convenience wrapper functions ([edgeSwipePlugin], [cornerSwipePlugin], etc.).
 */
sealed interface TouchEvent {
    /**
     * Tap family of events. Subtypes represent sequences of 1 or more taps.
     * - [Single]: one quick tap
     * - [Double]: two quick taps (handled natively by Compose)
     * - [NTap]: N consecutive taps within the configured time window
     */
    sealed interface Tap : TouchEvent {
        data object Single : Tap

        data object Double : Tap

        data class NTap(
            val count: Int,
        ) : Tap
    }

    data object LongPress : TouchEvent

    data object TwoFingerTap : TouchEvent

    /**
     * Swipe or multi-touch directional gesture.
     *
     * @property direction 8-direction from atan2 of displacement
     * @property origin where the gesture started (Any, Edge, or Corner)
     * @property fingerCount number of fingers detected (1 or 2)
     *
     * To differentiate swipes by both origin edge and direction (e.g. top
     * edge swiped left vs. top edge swiped right):
     * ```
     * when (event.origin) {
     *     is SwipeOrigin.Edge -> when (event.direction) {
     *         Direction.LEFT  -> // top edge, swiping left
     *         Direction.RIGHT -> // top edge, swiping right
     *     }
     * }
     * ```
     */
    data class Swipe(
        val direction: Direction,
        val origin: SwipeOrigin,
        val fingerCount: Int,
    ) : TouchEvent

    /**
     * Low-velocity drag gesture (scroll).
     *
     * @property direction 8-direction from atan2 of displacement
     */
    data class Scroll(
        val direction: Direction,
    ) : TouchEvent

    /**
     * Long-press followed by a directional drag.
     *
     * @property direction dominant direction of the drag
     * @property distance total drag distance in pixels
     */
    data class LongPressDrag(
        val direction: Direction,
        val distance: Float,
    ) : TouchEvent

    /**
     * Pinch-in / pinch-out (scale) gesture.
     *
     * @property scaleFactor raw scale factor from the transform gesture
     * @property isScalingOut true when pinching out (scale factor < 0.99)
     */
    data class PinchScale(
        val scaleFactor: Float,
        val isScalingOut: Boolean,
    ) : TouchEvent

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UP_RIGHT,
        UP_LEFT,
        DOWN_RIGHT,
        DOWN_LEFT,
    }

    sealed interface SwipeOrigin {
        data object Any : SwipeOrigin

        data class Edge(
            val type: EdgeType,
        ) : SwipeOrigin

        data class Corner(
            val type: CornerType,
        ) : SwipeOrigin
    }

    enum class EdgeType {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    }

    enum class CornerType {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }
}
