---
title: "Scoop"
weight: 10
---

# Scoop

Detects a scooping (lifting/pickup) motion. Register with `scoopPlugin`.

## Algorithm

The algorithm maintains an EMA-smoothed acceleration baseline and computes the impulse (deviation from baseline). When the impulse exceeds the threshold, consecutive samples are counted. A scoop is emitted when the sustained-sample count (3 consecutive) and peak jerk within the window both exceed their respective internal minima, subject to a global debounce.

## Events

| Event | Description |
|-------|-------------|
| `ScoopEvent.Scooped` | Scoop gesture detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Impulse threshold — minimum deviation from the EMA baseline to count as a scoop motion | `10f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    scoopPlugin(
        threshold = 10f, // impulse deviation from baseline (default: 10f)
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
