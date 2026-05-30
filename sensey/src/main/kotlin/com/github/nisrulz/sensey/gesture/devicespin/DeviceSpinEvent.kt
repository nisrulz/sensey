
package com.github.nisrulz.sensey.gesture.devicespin

sealed interface DeviceSpinEvent {
    data object Spun : DeviceSpinEvent
}
