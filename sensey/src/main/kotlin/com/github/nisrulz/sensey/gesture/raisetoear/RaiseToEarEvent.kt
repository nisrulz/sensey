
package com.github.nisrulz.sensey.gesture.raisetoear

sealed interface RaiseToEarEvent {
    data object AtEar : RaiseToEarEvent
}
