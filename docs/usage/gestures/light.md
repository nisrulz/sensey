---
title: "Light"
weight: 4
---

# Light

Detects ambient light transitions (dark ↔ light). Register with `lightPlugin`.

## How to perform

Cover the top of the phone with your hand to simulate darkness, or expose it to light.

## Algorithm

The algorithm compares ambient lux against a configurable dark threshold with a built-in hysteresis gap. On first reading it establishes a baseline state. Transition to dark fires when lux drops below the dark threshold. Transition to light fires when lux rises above a fixed internal threshold (`12f`). Same-state readings are ignored to avoid repeated events.

## Events

| Event | Description |
|-------|-------------|
| `LightEvent.Dark` | Ambient light dropped below `darkThreshold` |
| `LightEvent.Light` | Ambient light rose above the internal light threshold |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `darkThreshold` | Lux value below which the environment is considered dark | `8f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    lightPlugin(
        darkThreshold = 8f, // lux below this = Dark event (default: 8f)
    ) { event ->
        when (event) {
            LightEvent.Dark  -> println("Dark")  // ambient light dropped below threshold
            LightEvent.Light -> println("Light") // ambient light rose above threshold
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
