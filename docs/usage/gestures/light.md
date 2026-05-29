---
title: "Light"
weight: 4
---

# Light

Detects ambient light transitions (dark ↔ light) using the light sensor. The algorithm compares ambient lux values against configurable dark and light thresholds with hysteresis. On first reading it establishes a baseline state. Subsequent readings trigger a transition event only when the value crosses the opposite threshold. Same-state readings are ignored to avoid repeated events.

## Events

| Event | Description |
|-------|-------------|
| `LightEvent.Dark` | Ambient light below `darkThreshold` |
| `LightEvent.Light` | Ambient light above `lightThreshold` |

Hysteresis (different thresholds for dark→light vs light→dark) prevents oscillation at boundaries.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `darkThreshold` | Lux value below which the environment is considered dark | `8f` |
| `lightThreshold` | Lux value above which the environment is considered light (not exposed as a plugin parameter; defaults internally) | `12f` |

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
