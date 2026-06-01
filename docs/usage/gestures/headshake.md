---
title: "HeadShake"
weight: 26
---

# HeadShake

Detects a head shaking (no) gesture using the gyroscope. Register with `headShakePlugin`.

## How to perform

Hold the phone in front of you (screen facing you) — upright or tilted with the top higher. Quickly rotate your wrists left then right past center — like shaking your head to say "no". The full motion should be brisk, completing in under a second. Works at any pitch angle thanks to combined Y+Z axis detection.

## Algorithm

Uses a `GyroIntegrator` to track cumulative rotation on the horizontal plane (combined Y + Z axes). A two-phase oscillation detector tracks "out and back" motion: the horizontal angle must first exceed the threshold in one direction, then return past zero (complete oscillation), all within the time window. Direction-agnostic — handles both left-then-right and right-then-left shakes.

The Y+Z combination ensures the gesture works regardless of device pitch — when the phone is held tilted (top higher), the world-vertical rotation of a head shake projects onto both Y and Z axes, and summing them recovers the full signal.

## Events

| Event | Description |
|-------|-------------|
| `HeadShakeEvent` | Head shake gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Cumulative horizontal rotation in degrees (Y+Z combined) in one direction that triggers the oscillation phase | `30f` |
| `timeWindowMs` | Time window in milliseconds within which the full shake must complete | `800L` |
| `cooldownMs` | Minimum time in milliseconds between consecutive shake events | `1500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    headShakePlugin(
        angleThreshold = 30f,
        timeWindowMs = 800L,
    ) {
        println("Head shake detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Shake to dismiss | Reject call or dismiss notification by shaking head |
| Shake to undo | Undo last action with a shake gesture |
| Shake to redo | Redo with a second shake |
