
package com.github.nisrulz.sensey.gesture.edgeswipe

enum class Edge {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}

data class EdgeSwipeEvent(
    val edge: Edge,
)
