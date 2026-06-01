---
title: "Movement"
weight: 6
---

# Movement

Detects when the device is moving or stationary. Register with `movementPlugin`.

## How to perform

Move the phone around in your hand, or place it still on a flat surface.

## Algorithm

The algorithm computes the Euclidean magnitude of the acceleration vector and compares the absolute delta between consecutive readings against a threshold. If the delta exceeds the threshold the device is considered moving (with the dominant spatial direction reported). If no movement occurs within a configurable timeout the device is declared stationary.

## Events

| Event | Description |
|-------|-------------|
| `MovementEvent.Moved(direction)` | Device is moving; `direction` indicates the dominant axis (`X_POS`, `X_NEG`, `Y_POS`, `Y_NEG`, `Z_POS`, `Z_NEG`) |
| `MovementEvent.Stationary` | Device has been still for the timeout period |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Acceleration magnitude delta threshold to consider as movement | `0.3f` |
| `timeBeforeDeclaringStationary` | Time in milliseconds of no movement before declaring stationary | `1500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    movementPlugin(
        threshold = 0.3f,                                     // accel delta magnitude to trigger (default: 0.3f)
        timeBeforeDeclaringStationary = 1500L, // ms of stillness before Stationary event (default: 1500L)
    ) { event ->
        when (event) {
            is MovementEvent.Moved -> println("Movement detected!" + " in direction: " + event.direction) // device is moving
            is MovementEvent.Stationary -> println("Device stationary!") // device has stopped moving
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Pocket mode | Detect device movement while in pocket |
| Driving detection | Detect if device is moving in a vehicle |
| Idle timeout | Pause content when device is stationary |
| Activity awareness | Trigger actions based on device motion state |
| Stationary lock | Auto-lock when device is still for a period |
