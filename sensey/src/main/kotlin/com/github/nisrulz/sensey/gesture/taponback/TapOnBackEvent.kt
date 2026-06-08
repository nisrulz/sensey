
package com.github.nisrulz.sensey.gesture.taponback

sealed interface TapOnBackEvent {
    data object Detected : TapOnBackEvent
}
