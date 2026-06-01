---
title: "RaiseToEar"
weight: 22
---

# RaiseToEar

Detects when the device is raised to the ear (call position). Register with `raiseToEarPlugin`.

## How to perform

Hold the phone up to your ear as if taking a phone call.

## Algorithm

Fuses proximity and gravity sensor data. The gesture fires when the device is near the ear (proximity distance below the threshold) AND NOT flat on a surface. Flatness is determined by `|gz|/gravMag ≤ minGzRatio` — when the phone is held upright at the ear, gravity is along the Y-axis (|gz| ≈ 0), not along Z as when flat on a table.

This uses a custom `RaiseToEarDetector` that registers for both `TYPE_PROXIMITY` and `TYPE_GRAVITY`.

## Events

| Event | Description |
|-------|-------------|
| `RaiseToEarEvent.AtEar` | Device is near the ear and not flat on a surface |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `maxProximityCm` | Maximum proximity distance (cm) to consider the device near the ear | `5f` |
| `minGzRatio` | Maximum `|gz|/gravMag` ratio below which the device is considered not flat (held upright) | `0.3f` |
| `debounceMs` | Minimum time (ms) between consecutive `AtEar` events | `500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    raiseToEarPlugin(
        maxProximityCm = 5f,
        minGzRatio = 0.3f,
        debounceMs = 500L,
    ) { event ->
        println("Device raised to ear!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Call screen-off | Turn off screen during a call when phone is at ear |
| Audio routing | Switch audio to earpiece speaker |
| Proximity unlock | Prepare face unlock when raising to ear |
