
package com.github.nisrulz.sensey.gesture.audio.clap

sealed interface ClapEvent {
    data object Clapped : ClapEvent
}
