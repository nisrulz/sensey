---
title: "Scoop"
weight: 10
---

# Scoop

Detects a scooping (lifting/pickup) motion using the accelerometer. The algorithm maintains an EMA-smoothed acceleration baseline and computes the impulse (deviation from baseline) and jerk (change between consecutive samples). When the impulse exceeds the threshold, consecutive samples are counted. A scoop is emitted when the sustained-sample count and peak jerk within the window both exceed their respective minima, subject to a global debounce.

## Events

| Event | Description |
|-------|-------------|
| `ScoopEvent.Scooped` | Scoop gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Impulse threshold — minimum deviation from the EMA baseline to count as a scoop motion | `10f` |
| `minPeakJerk` | Minimum peak jerk (change in acceleration between consecutive samples) required within the impulse window | `3.0f` |
| `minSustainedSamples` | Minimum number of consecutive samples above the impulse threshold required to confirm the gesture | `3` |
| `debounceMs` | Global debounce time in milliseconds between successive scoop events | `1000L` |
| `baselineSamples` | Number of initial sensor readings used to establish the EMA baseline before detection begins | `10` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    scoopPlugin(
        threshold = 10f,          // impulse deviation from baseline (default: 10f)
        minPeakJerk = 3.0f,       // minimum peak jerk within the window (default: 3.0f)
        minSustainedSamples = 3,  // consecutive samples above threshold needed (default: 3)
        debounceMs = 1000L,       // debounce between scoop events in ms (default: 1000L)
        baselineSamples = 10,     // initial readings to establish baseline (default: 10)
    ) {
        println("Scoop detected!") // a scooping/lifting motion was recognised
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Scoop to pick up | Turn on screen when picking up device |
| Scoop to notify | Show notifications on scoop gesture |
| Scoop to preview | Quick glance preview on lift |
| Scoop to unlock | Trigger face unlock on pickup |
| Scoop to resume | Resume media playback on pickup |
| Scoop to glance | Show time and weather on lift |
