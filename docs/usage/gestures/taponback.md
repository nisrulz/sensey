---
title: "TapOnBack"
weight: 15
---

# TapOnBack

Detects quick double-taps on the device back. Single taps are ignored. Register with `tapOnBackPlugin`.

## How to perform

Hold the phone in one hand and double-tap quickly on the back with a finger.

## Algorithm

State machine with four phases:

1. **IDLE** — Wait for the first spike (sharp acceleration + jerk above thresholds)
2. **SETTLING_FIRST** — Signal must drop below 2.0 m/s² within `settleWindowMs` (100ms)
3. **GUARD** — A `reboundGuardMs` period (180ms) absorbs natural rebound oscillations that follow a tap, preventing them from being误detected as the second tap
4. **LISTENING** — After the guard expires, watch for a valid second spike within `tapIntervalMs` (400ms). When found → emit `TapOnBackEvent.Detected`

## Events

| Event | Description |
|-------|-------------|
| `TapOnBackEvent.Detected` | Quick double-tap on the back detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `accelThreshold` | Minimum linear acceleration magnitude (m/s²) | `1.5f` |
| `minJerk` | Minimum jerk between consecutive samples | `2.0f` |
| `preSettleMs` | Required quiet period before the first spike (filters continuous motion) | `200L` |
| `settleWindowMs` | Max time to wait for settling after a spike | `100L` |
| `reboundGuardMs` | Guard period after settling to absorb rebound oscillations | `180L` |
| `tapIntervalMs` | Max total time for the double-tap sequence | `400L` |
| `cooldownMs` | Post-detection cooldown | `1000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(
        accelThreshold = 1.5f,
        minJerk = 2.0f,
        settleWindowMs = 100L,
        reboundGuardMs = 180L,
        tapIntervalMs = 400L,
        cooldownMs = 1000L,
    ) {
        println("Tap on back detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Tap to screenshot | Take screenshot by double-tapping back |
| Tap to launch | Open camera or app with back tap |
| Tap to toggle | Toggle flashlight with back tap |
| Tap to go back | Navigate back with double tap |
| Tap to mute | Mute incoming call |
| Tap to assist | Trigger accessibility shortcut |
