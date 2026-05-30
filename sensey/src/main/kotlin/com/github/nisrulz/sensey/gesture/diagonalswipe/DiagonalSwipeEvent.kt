
package com.github.nisrulz.sensey.gesture.diagonalswipe

enum class DiagonalDirection {
    UP_RIGHT,
    DOWN_RIGHT,
    DOWN_LEFT,
    UP_LEFT,
}

data class DiagonalSwipeEvent(
    val direction: DiagonalDirection,
)
