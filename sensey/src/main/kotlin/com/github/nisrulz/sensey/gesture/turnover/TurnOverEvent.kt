
package com.github.nisrulz.sensey.gesture.turnover

sealed interface TurnOverEvent {
    data object Flipped : TurnOverEvent
}
