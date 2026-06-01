---
title: "HeadShake"
weight: 26
---

# HeadShake

Detects a head shaking (no) gesture using the gyroscope. Register with `headShakePlugin`.

## How to perform

Hold the phone in front of you (screen facing you). Quickly rotate your wrists left then right past center — like shaking your head to say "no". The full motion should be brisk, completing in under a second.

## Algorithm

Uses a `GyroIntegrator` to track cumulative Z-axis (yaw) rotation. A two-phase oscillation detector tracks "out and back" motion: the Z-angle must first exceed the threshold in one direction, then return past zero (complete oscillation), all within the time window. Direction-agnostic — handles both left-then-right and right-then-left shakes.

## Events

| Event | Description |
|-------|-------------|
| `HeadShakeEvent` | Head shake gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Cumulative yaw rotation in degrees in one direction that triggers the oscillation phase | `30f` |
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
