---
title: "TurnOver"
weight: 20
---

# TurnOver

Detects a full 180-degree flip of the device using the gyroscope. Register with `turnOverPlugin`.

## How to perform

Hold the phone in your hand and quickly flip it over 180 degrees as if turning it face-down on a table.

## Algorithm

Uses a `GyroIntegrator` to track cumulative rotation across all axes. When the net rotation magnitude (sqrt of sum of squares) exceeds the configured threshold, the gesture fires and the integrator resets. This handles flips that involve rotation on multiple axes (e.g., wrist flicks) unlike a single-axis check. More precise than the accelerometer-based [Flip](flip.md) since it directly measures angular motion rather than inferring orientation from gravity.

## Events

| Event | Description |
|-------|-------------|
| `TurnOverEvent.Flipped` | Device rotated past the angle threshold |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Net rotation magnitude in degrees across all axes that triggers the event | `150f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    turnOverPlugin(
        angleThreshold = 130f,
    ) { event ->
        println("Device flipped over!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Flip to mute | Silence incoming call by turning over (gyro-based, more precise) |
| Flip to reject | Reject call with a deliberate flip motion |
| Silent mode toggle | Toggle silent/vibrate modes on flip |
