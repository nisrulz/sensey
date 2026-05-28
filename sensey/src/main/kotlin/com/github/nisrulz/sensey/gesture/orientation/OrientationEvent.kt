
package com.github.nisrulz.sensey.gesture.orientation

sealed interface OrientationEvent {
    data object TopSideUp : OrientationEvent

    data object BottomSideUp : OrientationEvent

    data object LeftSideUp : OrientationEvent

    data object RightSideUp : OrientationEvent
}
