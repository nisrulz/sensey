---
title: "TapOnBack"
weight: 15
---

# TapOnBack

Detects double-taps on the device back or side. Register with `tapOnBackPlugin`.

## Algorithm

The algorithm maintains an EMA-smoothed gravity baseline from accelerometer readings. It computes the angular deviation from this baseline and the angular jerk (change in angle between consecutive samples). A valid tap requires sufficient angle deviation, minimum jerk, and debounce since the last tap. Taps are accumulated within a sequence timeout; the event is emitted only when at least two taps occur within that window. Single taps are ignored.

## Events

| Event | Description |
|-------|-------------|
| `TapOnBackEvent` | Double-tap on the back/side of the device detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Minimum angle deviation in degrees from the gravity baseline to register a tap | `1.5f` |
| `minAngleJerk` | Minimum angular jerk (change in angle between consecutive samples) in degrees to qualify as a tap impulse | `1.5f` |
| `tapDebounceMs` | Debounce timeout in milliseconds between individual taps within a sequence | `250L` |
| `tapSequenceTimeoutMs` | Maximum time window in milliseconds for accumulating a double-tap sequence; if the window expires with fewer than 2 taps the sequence is discarded | `500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(
        angleThreshold = 1.5f,       // min angle deviation from gravity baseline (default: 1.5f)
        minAngleJerk = 1.5f,         // min angular jerk between samples (default: 1.5f)
        tapDebounceMs = 250L,        // debounce between individual taps (default: 250L)
        tapSequenceTimeoutMs = 500L, // max time for a double-tap sequence (default: 500L)
    ) {
        println("Tap on back detected!") // a double-tap on the back/side was recognised
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
