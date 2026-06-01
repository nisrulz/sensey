---
title: "TwoFingerSwipe"
weight: 28
---

# TwoFingerSwipe

Detects two-finger directional swipes in Compose. Register with `twoFingerSwipePlugin`.

## How to perform

Place two fingers on the screen and swipe in a direction without pinching or rotating.

## Algorithm

Uses Compose's `detectTransformGestures` to track the pan offset of the two-finger gesture. The algorithm filters out gestures with significant zoom (< 0.9 or > 1.1) or rotation (> 0.3 radians) to ensure only pure swipes are detected. Direction is determined by the dominant axis of the pan vector.

## Events

| Event | Properties | Description |
|-------|------------|-------------|
| `TwoFingerSwipeEvent` | `direction` — swipe direction | Two-finger swipe detected |

Directions: `LEFT`, `RIGHT`, `UP`, `DOWN`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum pan distance in pixels to qualify | `80f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    twoFingerSwipePlugin(context) { event ->
        println("Two-finger swipe: ${event.direction}")
    }
}
```

Requires `senseyGestures()` on a composable to capture touch input:

```kotlin
Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Navigate tabs | Two-finger swipe left/right between tabs |
| Scroll pages | Two-finger swipe up/down to scroll |
| Undo/Redo | Two-finger swipe left/right for undo/redo |
| Zoom alternative | Two-finger swipe for zoom in certain apps |
