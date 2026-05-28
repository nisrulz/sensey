
package com.github.nisrulz.sensey.gesture.tiltdirection

sealed interface TiltDirectionEvent {
    enum class Direction { CLOCKWISE, ANTICLOCKWISE }

    data class AxisXTilt(
        val direction: Direction,
    ) : TiltDirectionEvent

    data class AxisYTilt(
        val direction: Direction,
    ) : TiltDirectionEvent

    data class AxisZTilt(
        val direction: Direction,
    ) : TiltDirectionEvent
}
