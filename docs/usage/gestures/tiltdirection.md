---
title: "TiltDirection"
weight: 13
---

# TiltDirection

Detects the dominant tilt axis and its direction. Register with `tiltDirectionPlugin`.

## How to perform

Tilt the phone in any direction — left, right, forward, or back.

## Algorithm

The algorithm compares the absolute values of the three gyroscope components (X, Y, Z). The axis with the largest magnitude above the threshold is considered the dominant tilt axis. A positive value on that axis maps to ANTICLOCKWISE tilt; a negative value maps to CLOCKWISE.

## Events

| Event | Description |
|-------|-------------|
| `TiltDirectionEvent.AxisXTilt(direction)` | Tilt around X axis is dominant |
| `TiltDirectionEvent.AxisYTilt(direction)` | Tilt around Y axis is dominant |
| `TiltDirectionEvent.AxisZTilt(direction)` | Tilt around Z axis is dominant |

Directions: `TiltDirectionEvent.Direction.CLOCKWISE` (negative value) or `TiltDirectionEvent.Direction.ANTICLOCKWISE` (positive value). The dominant axis (highest magnitude) is reported.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Minimum gyroscope magnitude required on any axis to consider it as dominant tilt; values below this are ignored as noise | `0.5f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    tiltDirectionPlugin(
        threshold = 0.5f, // minimum gyroscope magnitude to consider as tilt (default: 0.5f)
    ) { event ->
        when (event) {
            is TiltDirectionEvent.AxisXTilt -> println("X tilt: ${event.direction}") // dominant tilt on X axis
            is TiltDirectionEvent.AxisYTilt -> println("Y tilt: ${event.direction}") // dominant tilt on Y axis
            is TiltDirectionEvent.AxisZTilt -> println("Z tilt: ${event.direction}") // dominant tilt on Z axis
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Tilt to scroll | Scroll content by tilting device |
| Tilt to balance | Control balance in games by tilting |
| Tilt to pan | Pan around a map or image by tilting |
| Tilt to steer | Steer in racing games by tilting |
| Tilt to adjust | Adjust volume or brightness by tilting |
