
package com.github.nisrulz.sensey.gesture.proximity

sealed interface ProximityEvent {
    data object Far : ProximityEvent

    data object Near : ProximityEvent
}
