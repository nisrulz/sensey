---
title: "WristTwist"
weight: 8
---

# WristTwist

Detects a wrist-twisting motion. Register with `wristTwistPlugin`.

## Algorithm

The algorithm monitors linear acceleration (total magnitude minus gravity). When a single impulse exceeds the threshold the gesture window starts. The gesture is considered complete when no further impulses occur within the configured timeout — similar in structure to chop detection but tuned with different defaults for the wrist-twist motion profile.

## Events

| Event | Description |
|-------|-------------|
| `WristTwistEvent.Twisted` | Wrist twist gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Linear acceleration magnitude threshold to trigger the gesture window | `12f` |
| `timeForWristTwistGesture` | Time window in milliseconds during which twist impulses are accumulated | `1000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    wristTwistPlugin(
        threshold = 12f,                // linear accel magnitude to trigger (default: 12f)
        timeForWristTwistGesture = 1000L, // max duration of the twist motion window (default: 1000L)
    ) {
        println("Wrist twist detected!") // a wrist-twisting motion was recognised
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Twist to launch | Launch camera with a twist gesture |
| Twist to answer | Answer incoming call with wrist twist |
| Twist to switch | Switch between front and rear camera |
| Twist to reject | Reject call with twist gesture |
