---
title: "Shake"
weight: 2
---

# Shake

Detects when the device is being shaken. Register with `shakePlugin`.

## Algorithm

The algorithm computes the Euclidean magnitude of raw acceleration and tracks it with a single-pole (EMA) smoothed delta. When the smoothed delta exceeds the threshold a shake is reported. If no new shake impulse occurs within the configured timeout, a stopped event is emitted.

## Events

| Event | Description |
|-------|-------------|
| `ShakeEvent.Detected` | Device is being shaken |
| `ShakeEvent.Stopped` | Shaking has stopped |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | EMA-smoothed acceleration delta magnitude that triggers shake detection; higher values require more vigorous shaking | `3f` |
| `timeBeforeDeclaringShakeStopped` | Time in milliseconds of stillness before `ShakeEvent.Stopped` is emitted | `1000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    shakePlugin(
        threshold = 3f,                         // accel delta magnitude to trigger (default: 3f)
        timeBeforeDeclaringShakeStopped = 1000L,  // ms of stillness before Stopped event (default: 1000L)
    ) { event ->
        when (event) {
            ShakeEvent.Detected -> println("Shake detected!") // device is being shaken
            ShakeEvent.Stopped  -> println("Shake stopped")   // shaking has ceased
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Shake to undo | Undo last action when device is shaken |
| Shake to shuffle | Randomize playlist or content |
| Shake to clear | Clear input fields or reset state |
| Shake to refresh | Refresh content or reload data |
| Shake to switch | Switch between tabs or views |
