
package com.github.nisrulz.sensey.gesture.wristtwist

sealed interface WristTwistEvent {
    data object Twisted : WristTwistEvent
}
