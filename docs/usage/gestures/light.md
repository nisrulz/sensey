---
title: "Light"
weight: 4
---

# Light

Detects ambient light levels. Register with `lightPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `LightEvent.Dark` | Ambient light below threshold |
| `LightEvent.Light` | Ambient light above threshold |

Hysteresis prevents oscillation at boundaries.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `darkThreshold` | Lux value below which is considered dark | `5f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    lightPlugin(darkThreshold = 5f) { event ->
        when (event) {
            LightEvent.Dark  -> println("Dark")
            LightEvent.Light -> println("Light")
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Auto brightness | Adjust screen brightness based on ambient light |
| Pocket detection | Detect if device is in a dark pocket |
| Reading mode | Switch to warm display in low light |
| Night mode | Enable dark theme when ambient light drops |
| Battery saver | Reduce power consumption in dark conditions |
