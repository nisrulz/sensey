---
title: "Orientation"
weight: 12
---

# Orientation

Detects the device orientation based on which edge is pointing up. Register with `orientationPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `OrientationEvent.TopSideUp` | Top edge pointing up |
| `OrientationEvent.BottomSideUp` | Bottom edge pointing up |
| `OrientationEvent.LeftSideUp` | Left edge pointing up |
| `OrientationEvent.RightSideUp` | Right edge pointing up |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `smoothness` | Smoothing factor for orientation detection | `3` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    orientationPlugin(
        smoothness = 3, // higher = less jitter, slower response
    ) { event ->
        when (event) {
            OrientationEvent.TopSideUp    -> println("Top up")
            OrientationEvent.BottomSideUp -> println("Bottom up")
            OrientationEvent.LeftSideUp   -> println("Left up")
            OrientationEvent.RightSideUp  -> println("Right up")
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
