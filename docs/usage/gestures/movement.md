---
title: "Movement"
weight: 6
---

# Movement

Detects when the device is moving or stationary. Register with `movementPlugin`.

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
| `timeBeforeDeclaringStationary` | Time in milliseconds of no movement before declaring stationary | `5000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    movementPlugin(
        threshold = 0.3f,                   // accel magnitude delta to count as movement (default: 0.3f)
        timeBeforeDeclaringStationary = 5000L, // ms of stillness before Stationary event (default: 5000L)
    ) { event ->
        when (event) {
            is MovementEvent.Moved      -> println("Moving in direction: ${event.direction}") // device is in motion
            MovementEvent.Stationary -> println("Stationary") // device has been still for the timeout
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
