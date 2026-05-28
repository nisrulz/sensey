---
title: "Movement"
weight: 6
---

# Movement

Detects when the device is moving or stationary. Register with `movementPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `MovementEvent.Moved` | Device is moving |
| `MovementEvent.Stationary` | Device has been still for the timeout period |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Acceleration threshold to consider as movement | `0.5f` |
| `timeBeforeDeclaringStationary` | Time (ms) of no movement before declaring stationary | `3000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    movementPlugin(
        threshold = 0.5f,                // accel magnitude to count as movement
        timeBeforeDeclaringStationary = 3000L, // ms of stillness → Stationary
    ) { event ->
        when (event) {
            MovementEvent.Moved      -> println("Moving")
            MovementEvent.Stationary -> println("Stationary")
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
