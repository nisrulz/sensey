
package com.github.nisrulz.sensey.gesture.pickupdevice

sealed interface PickupDeviceEvent {
    data object PickedUp : PickupDeviceEvent

    data object PutDown : PickupDeviceEvent
}
