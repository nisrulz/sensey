
package com.github.nisrulz.sensey.gesture.movement

sealed interface MovementEvent {
    enum class Direction { X_POS, X_NEG, Y_POS, Y_NEG, Z_POS, Z_NEG }

    data class Moved(
        val direction: Direction,
    ) : MovementEvent

    data object Stationary : MovementEvent
}
