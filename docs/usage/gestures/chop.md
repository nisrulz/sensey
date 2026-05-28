---
title: "Chop"
weight: 7
---

# Chop

Detects a chopping motion gesture. Register with `chopPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `ChopEvent.Chopped` | Chop gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Acceleration threshold for chop detection | `30f` |
| `timeForChopGesture` | Max time (ms) for the chop gesture | `500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    chopPlugin(
        threshold = 30f,           // accel magnitude to trigger
        timeForChopGesture = 500L, // max duration of the chop motion
    ) {
        println("Chop detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Chop to toggle | Toggle flashlight on chop gesture |
| Chop to capture | Take a screenshot with a chop motion |
| Chop to play | Play or pause media with a chop |
| Chop to skip | Skip to next track with double chop |
| Chop to record | Start/stop voice recording |
