---
title: "Wave"
weight: 9
---

# Wave

Detects a hand wave over the proximity sensor. Register with `wavePlugin`.

## Algorithm

The algorithm tracks near→far state transitions of the proximity sensor. A wave is recognised when the device transitions from NEAR to FAR, the near state was held for a fixed minimum duration, the entire gesture occurs within a configurable time window, and sufficient debounce time has passed since the last detected wave.

## Events

| Event | Description |
|-------|-------------|
| `WaveEvent.Waved` | Hand wave over proximity sensor detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `timeWindowMillis` | Maximum time in milliseconds for the complete near→far wave motion | `1000L` |
| `debounceMillis` | Minimum time in milliseconds between successive wave events to prevent repeated triggers | `1000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    wavePlugin(
        timeWindowMillis = 1000L, // max time for the wave motion (default: 1000L)
        debounceMillis = 1000L,   // debounce between successive wave events (default: 1000L)
    ) {
        println("Wave detected!") // a hand wave over the proximity sensor was recognised
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
