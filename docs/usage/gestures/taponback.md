---
title: "TapOnBack"
weight: 15
---

# TapOnBack

Detects double-taps on the device back or side using gravity vector angle analysis. Single taps are ignored — only two rapid taps trigger. Register with `tapOnBackPlugin`.

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Minimum angle change to register a tap | `1.5f` |
| `tapDebounceMs` | Debounce timeout between taps | `250L` |
| `tapSequenceTimeoutMs` | Max time for a double-tap sequence | `500L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(
        angleThreshold = 1.5f,       // min gravity vector angle change
        tapDebounceMs = 250L,        // debounce between taps
        tapSequenceTimeoutMs = 500L, // max time for a double-tap sequence
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
