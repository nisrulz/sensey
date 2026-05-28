
package com.github.nisrulz.sensey.gesture.shake

sealed interface ShakeEvent {
    data object Detected : ShakeEvent

    data object Stopped : ShakeEvent
}
