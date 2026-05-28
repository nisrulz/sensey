---
title: "RotationAngle"
weight: 14
---

# RotationAngle

Reports the device rotation angles. Register with `rotationAnglePlugin`.

## Events

| Event | Properties |
|-------|------------|
| `RotationAngleEvent` | `angleInAxisX`, `angleInAxisY`, `angleInAxisZ` in degrees |

Fires only when at least one angle changes by more than `minAngleChange` from the previous reading.

## Usage

```kotlin
senseyRegister(lifecycle) {
    rotationAnglePlugin { event ->
        println("X: ${event.angleInAxisX}, Y: ${event.angleInAxisY}, Z: ${event.angleInAxisZ}")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Level tool | Use as a digital level or inclinometer |
| Camera stability | Detect camera tilt for stability warnings |
| Panorama guide | Guide user to hold device level for panoramas |
| AR placement | Detect surface angle for AR object placement |
| Angle sensor | Measure and display device tilt angles |
