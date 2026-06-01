---
title: "TapOnBack"
weight: 15
---

# TapOnBack

Detects double-taps on the device back or side. Register with `tapOnBackPlugin`.

## How to perform

Hold the phone in one hand and double-tap firmly on the back or side of the device with a finger.

## Algorithm

Computes the linear acceleration magnitude (`|accel - gravity|`) which spikes during any tap regardless of device orientation or whether it is held or on a table. Tracks this magnitude with an EMA smoother so gradual movements (tilts) produce low jerk while sharp impulses (taps) produce high jerk. Emits immediately when two valid taps occur within `tapIntervalMs`. A single tap is always ignored.

## Events

| Event | Description |
|-------|-------------|
| `TapOnBackEvent` | Double-tap on the back/side of the device detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `accelThreshold` | Minimum linear acceleration magnitude (m/s²) to register a tap | `2f` |
| `minJerk` | Minimum jerk between raw magnitude and EMA-smoothed magnitude (higher = less sensitive to gradual movements) | `2f` |
| `tapDebounceMs` | Debounce timeout in milliseconds between individual taps within a sequence | `250L` |
| `tapIntervalMs` | Maximum allowed gap in milliseconds between the two taps of a double-tap sequence | `500L` |
| `cooldownMs` | Post-detection cooldown in milliseconds during which all input is ignored | `1000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(
        accelThreshold = 2f,   // min linear acceleration to register tap (default: 2f)
        minJerk = 5f,          // min jerk from EMA smoother (default: 5f)
        tapDebounceMs = 250L,  // debounce between individual taps (default: 250L)
        tapIntervalMs = 500L,  // max gap between two taps (default: 500L)
        cooldownMs = 1000L,    // post-detection cooldown (default: 1000L)
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
| Tap to open recent | Open recent apps with back tap |
| Tap to mute | Mute incoming call with back tap |
| Tap to assist | Trigger accessibility shortcut |
| Tap to search | Open search with back tap gesture |
