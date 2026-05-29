---
title: "Wave"
weight: 9
---

# Wave

Detects a hand wave over the proximity sensor. Register with `wavePlugin`.

## Events

| Event | Description |
|-------|-------------|
| `WaveEvent.Waved` | Hand wave over proximity sensor detected |

A debounce of 1 second prevents rapid successive waves.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `timeWindowMillis` | Max time (ms) for the wave motion | `500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    wavePlugin(
        timeWindowMillis = 500L, // ms window for the wave motion
    ) {
        println("Wave detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Wave to wake | Turn on screen with hand wave |
| Wave to skip | Skip song with proximity wave gesture |
| Wave to dismiss | Dismiss notification with a wave |
| Wave to answer | Answer call with hand wave |
| Wave to reject | Reject call with hand wave |
| Wave to silence | Silence ringing by waving over sensor |
