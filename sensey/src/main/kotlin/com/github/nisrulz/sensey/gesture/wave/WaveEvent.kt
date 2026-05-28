
package com.github.nisrulz.sensey.gesture.wave

sealed interface WaveEvent {
    data object Waved : WaveEvent
}
