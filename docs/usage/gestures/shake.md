---
title: "Shake"
weight: 2
---

# Shake

Detects when the device is being shaken. Register with `shakePlugin`.

## Events

| Event | Description |
|-------|-------------|
| `ShakeEvent.Detected` | Device is being shaken |
| `ShakeEvent.Stopped` | Shaking has stopped |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `threshold` | Acceleration threshold for shake detection | `10f` |
| `timeBeforeDeclaringShakeStopped` | Time (ms) of stillness before declaring stopped | `2000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    shakePlugin(
        threshold = 10f,                      // accel magnitude to trigger
        timeBeforeDeclaringShakeStopped = 2000L, // ms of stillness → Stopped
    ) { event ->
        when (event) {
            ShakeEvent.Detected -> println("Shake detected!")
            ShakeEvent.Stopped  -> println("Shake stopped")
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
