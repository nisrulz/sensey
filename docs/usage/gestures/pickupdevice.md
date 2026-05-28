---
title: "PickupDevice"
weight: 11
---

# PickupDevice

Detects when the device is picked up or put down. Register with `pickupDevicePlugin`.

## Events

| Event | Description |
|-------|-------------|
| `PickupDeviceEvent.PickedUp` | Device was picked up |
| `PickupDeviceEvent.PutDown` | Device was put down |

## Usage

```kotlin
senseyRegister(lifecycle) {
    pickupDevicePlugin { event ->
        when (event) {
            PickupDeviceEvent.PickedUp -> println("Picked up")
            PickupDeviceEvent.PutDown  -> println("Put down")
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Pickup to show | Show notifications when picked up |
| Pickup to unlock | Trigger face unlock on pickup |
| Put down to sleep | Turn off screen when put down |
| Pickup to resume | Resume media playback on pickup |
| Pickup to glance | Show time and notifications on lift |
| Put down to pause | Pause media when device is set down |
| Put down to lock | Lock device automatically when put down |
