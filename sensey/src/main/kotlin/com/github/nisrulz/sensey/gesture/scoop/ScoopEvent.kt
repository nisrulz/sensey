
package com.github.nisrulz.sensey.gesture.scoop

sealed interface ScoopEvent {
    data object Scooped : ScoopEvent
}
