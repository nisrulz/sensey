
package com.github.nisrulz.sensey.gesture.touchtype

sealed interface TouchTypeEvent {
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

    data class NTap(
        val count: Int,
    ) : TouchTypeEvent

    data object DoubleTap : TouchTypeEvent

    data object LongPress : TouchTypeEvent

    data object SingleTap : TouchTypeEvent

    data class Swipe(
        val direction: Direction,
    ) : TouchTypeEvent

    data class Scroll(
        val direction: Direction,
    ) : TouchTypeEvent

    data object ThreeFingerSingleTap : TouchTypeEvent

    data object TwoFingerSingleTap : TouchTypeEvent
}
