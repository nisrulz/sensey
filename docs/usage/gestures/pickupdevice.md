---
title: "PickupDevice"
weight: 11
---

# PickupDevice

Detects when the device is picked up or put down. Register with `pickupDevicePlugin`.

## Algorithm

The algorithm maintains a circular buffer of recent acceleration magnitudes. The range (max − min) across the buffer indicates movement: a high range above a moving threshold signals a pickup. When the mean acceleration returns to the gravity range (~9–10.5 m/s²) and the buffer range is stable for a number of consecutive readings, the device is declared put down.

## Events

| Event | Description |
|-------|-------------|
| `PickupDeviceEvent.PickedUp` | Device was picked up |
| `PickupDeviceEvent.PutDown` | Device was put down and is resting on a surface |

## Parameters

This plugin has no configurable parameters.

## Usage

```kotlin
senseyRegister(lifecycle) {
    pickupDevicePlugin { event ->
        when (event) {
            PickupDeviceEvent.PickedUp -> println("Picked up") // device was lifted from a surface
            PickupDeviceEvent.PutDown  -> println("Put down")  // device was set down on a surface
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
