---
title: "NodGesture"
weight: 25
---

# NodGesture

Detects a nodding (yes) gesture using the gyroscope. Register with `nodGesturePlugin`.

## How to perform

Hold the phone normally in front of you (screen facing you). Quickly tilt the top of the phone forward then back past level — like nodding your head to say "yes". The full motion should be brisk, completing in under a second.

## Algorithm

Uses a `GyroIntegrator` to track cumulative X-axis (pitch) rotation. A two-phase oscillation detector tracks "out and back" motion: the X-angle must first exceed the threshold in one direction, then return past zero (complete oscillation), all within the time window. Direction-agnostic — handles both tilting forward then back and back then forward.

## Events

| Event | Description |
|-------|-------------|
| `NodGestureEvent` | Nod gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Cumulative pitch rotation in degrees in one direction that triggers the oscillation phase | `30f` |
| `timeWindowMs` | Time window in milliseconds within which the full nod must complete | `800L` |
| `cooldownMs` | Minimum time in milliseconds between consecutive nod events | `1500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    nodGesturePlugin(
        angleThreshold = 30f,
        timeWindowMs = 800L,
    ) {
        println("Nod detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Nod to answer | Accept incoming call with a nod |
| Nod to confirm | Confirm dialog or action without touching the screen |
| Hands-free scroll | Scroll content by nodding while holding the device |
