
package com.github.nisrulz.sensey.gesture.chop

sealed interface ChopEvent {
    data object Chopped : ChopEvent
}
