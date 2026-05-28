
package com.github.nisrulz.sensey.gesture.flip

sealed interface FlipEvent {
    data object FaceDown : FlipEvent

    data object FaceUp : FlipEvent
}
