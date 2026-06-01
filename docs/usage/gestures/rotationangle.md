---
title: "RotationAngle"
weight: 14
---

# RotationAngle

Reports the device rotation angles (Euler angles). Register with `rotationAnglePlugin`.

## How to perform

Rotate the phone around any axis and observe the angle readings change in real time.

## Algorithm

The algorithm wraps raw Euler-angle readings (axis X, Y, Z) into a `RotationAngleEvent`. It compares each axis value against the previously emitted event; if any axis has changed by more than the minimum angle threshold a new event is emitted. The first reading is always emitted.

## Events

| Event | Properties |
|-------|------------|
| `RotationAngleEvent` | `angleInAxisX`, `angleInAxisY`, `angleInAxisZ` — rotation angles in degrees |

Fires only when at least one angle changes by more than `minAngleChange` from the previous reading.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minAngleChange` | Minimum absolute change in degrees on any axis required to trigger a new event; filters out jitter | `1f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    rotationAnglePlugin(
        minAngleChange = 1f, // minimum degree change to trigger an event (default: 1f)
    ) { event ->
        println("X: ${event.angleInAxisX}°, Y: ${event.angleInAxisY}°, Z: ${event.angleInAxisZ}°")
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
