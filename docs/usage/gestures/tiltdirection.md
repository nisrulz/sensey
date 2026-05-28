---
title: "TiltDirection"
weight: 13
---

# TiltDirection

Detects the dominant tilt axis and direction. Register with `tiltDirectionPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `TiltDirectionEvent.AxisXTilt(direction)` | Tilt around X axis |
| `TiltDirectionEvent.AxisYTilt(direction)` | Tilt around Y axis |
| `TiltDirectionEvent.AxisZTilt(direction)` | Tilt around Z axis |

Directions: `TiltDirectionEvent.Direction.CLOCKWISE` or `TiltDirectionEvent.Direction.ANTICLOCKWISE`.

The dominant axis (highest magnitude) is reported.

## Usage

```kotlin
senseyRegister(lifecycle) {
    tiltDirectionPlugin { event ->
        when (event) {
            is TiltDirectionEvent.AxisXTilt -> println("X: ${event.direction}")
            is TiltDirectionEvent.AxisYTilt -> println("Y: ${event.direction}")
            is TiltDirectionEvent.AxisZTilt -> println("Z: ${event.direction}")
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
