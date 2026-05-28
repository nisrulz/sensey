
package com.github.nisrulz.sensey.gesture.light

sealed interface LightEvent {
    data object Dark : LightEvent

    data object Light : LightEvent
}
