---
title: "Flip"
weight: 3
---

# Flip

Detects when the device flips face-up or face-down. Register with `flipPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `FlipEvent.FaceUp` | Device is face-up (screen up) |
| `FlipEvent.FaceDown` | Device is face-down (screen down) |

## Usage

```kotlin
senseyRegister(lifecycle) {
    flipPlugin { event ->
        when (event) {
            FlipEvent.FaceUp   -> println("Face up")
            FlipEvent.FaceDown -> println("Face down")
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
