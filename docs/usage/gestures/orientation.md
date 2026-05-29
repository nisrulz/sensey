---
title: "Orientation"
weight: 12
---

# Orientation

Detects the device orientation (which edge is pointing up). Register with `orientationPlugin`.

## Algorithm

The algorithm computes pitch and roll via `SensorManager.getRotationMatrix` and `getOrientation`. It smoothes these angles with a configurable moving-average window and classifies the orientation using the averaged pitch and roll with hysteresis from the previous orientation. An event is emitted only when the orientation actually changes from the last reported state.

## Events

| Event | Description |
|-------|-------------|
| `OrientationEvent.TopSideUp` | Top edge of the device pointing up (portrait) |
| `OrientationEvent.BottomSideUp` | Bottom edge pointing up (reverse portrait) |
| `OrientationEvent.LeftSideUp` | Left edge pointing up (reverse landscape) |
| `OrientationEvent.RightSideUp` | Right edge pointing up (landscape) |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `smoothness` | Moving-average window size for pitch/roll smoothing; higher values reduce jitter but slow response to orientation changes | `1` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    orientationPlugin(
        smoothness = 1, // moving-average window for smoothing (default: 1 = no smoothing)
    ) { event ->
        when (event) {
            OrientationEvent.TopSideUp    -> println("Top up")    // portrait orientation
            OrientationEvent.BottomSideUp -> println("Bottom up") // reverse portrait
            OrientationEvent.LeftSideUp   -> println("Left up")   // reverse landscape
            OrientationEvent.RightSideUp  -> println("Right up")  // landscape orientation
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Auto-rotate | Switch between portrait and landscape |
| Game controls | Use orientation as tilt input for games |
| Media player | Lock portrait for video, landscape for browsing |
| Navigation | Auto-rotate maps based on device orientation |
| Flat detection | Detect if device is lying flat on a surface |
| Orientation lock | Lock UI orientation based on physical position |
