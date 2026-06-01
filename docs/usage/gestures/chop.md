---
title: "Chop"
weight: 7
---

# Chop

Detects a chopping motion gesture. Register with `chopPlugin`.

## How to perform

Hold the phone in one hand and make a quick chopping motion downward, like cutting with the edge of your hand.

## Algorithm

The algorithm monitors linear acceleration (total magnitude minus gravity, approximately 9.81 m/s²). When a single impulse exceeds the threshold the gesture window starts. The gesture is considered complete when no further impulses occur within the configured timeout.

## Events

| Event | Description |
|-------|-------------|
| `ChopEvent.Chopped` | Chop gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Linear acceleration magnitude threshold to trigger the gesture window | `35f` |
| `timeForChopGesture` | Time window in milliseconds during which chop impulses are accumulated | `700L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    chopPlugin(
        threshold = 25f,           // linear accel magnitude to trigger (default: 25f)
        timeForChopGesture = 700L, // max duration of the chop motion window (default: 700L)
    ) {
        println("Chop detected!") // a chopping motion was recognised
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
