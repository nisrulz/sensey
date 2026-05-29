---
title: "Flip"
weight: 3
---

# Flip

Detects when the device flips face-up or face-down. Register with `flipPlugin`.

## Algorithm

The algorithm compares Z-axis acceleration against configurable bounds: a Z value between approximately 8 and 10.5 m/s² indicates face-up, while a Z value between approximately −10.5 and −8 m/s² indicates face-down. Events are emitted only once per orientation state change to avoid repeated dispatches.

## Events

| Event | Description |
|-------|-------------|
| `FlipEvent.FaceUp` | Device is face-up (screen pointing upward) |
| `FlipEvent.FaceDown` | Device is face-down (screen pointing downward) |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `faceUpLowerBound` | Minimum Z-axis acceleration (m/s²) for face-up detection | `8f` |
| `faceUpUpperBound` | Maximum Z-axis acceleration (m/s²) for face-up detection | `10.5f` |
| `faceDownLowerBound` | Minimum Z-axis acceleration (m/s²) for face-down detection (negative) | `-10.5f` |
| `faceDownUpperBound` | Maximum Z-axis acceleration (m/s²) for face-down detection (negative) | `-8f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    flipPlugin(
        faceUpLowerBound = 8f,     // lower Z bound for face-up (default: 8f)
        faceUpUpperBound = 10.5f,  // upper Z bound for face-up (default: 10.5f)
        faceDownLowerBound = -10.5f, // lower Z bound for face-down (default: -10.5f)
        faceDownUpperBound = -8f,  // upper Z bound for face-down (default: -8f)
    ) { event ->
        when (event) {
            FlipEvent.FaceUp   -> println("Face up")   // screen is pointing upward
            FlipEvent.FaceDown -> println("Face down") // screen is pointing downward
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Flip to mute | Silence incoming call by flipping face-down |
| Flip to pause | Pause media playback when face-down |
| Flip to snooze | Snooze alarm by flipping over |
| Flip to reject | Reject incoming call by flipping |
| Flip to silence | Mute notification sounds when flipped |
