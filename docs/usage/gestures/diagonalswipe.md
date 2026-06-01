---
title: "DiagonalSwipe"
weight: 32
---

# DiagonalSwipe

Detects diagonal swipe gestures (UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT). This is a **convenience wrapper** around `touchPlugin` — internally configures `SwipeConfig(diagonalOnly = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Swipe diagonally across the screen (e.g., bottom-left to top-right).

## Algorithm

Classifies drag gestures by computing the atan2 angle and checking it falls within diagonal quadrants. Dispatches `TouchEvent.Swipe` with diagonal directions only.

## Events

| Event | Description |
|-------|-------------|
| `TouchEvent.Swipe(direction, origin = Any, fingerCount = 1)` | Diagonal swipe detected |

Directions (diagonal only): `UP_RIGHT`, `UP_LEFT`, `DOWN_RIGHT`, `DOWN_LEFT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum drag distance in pixels | `80f` |

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent

senseyRegister(lifecycle) {
    diagonalSwipePlugin(context) { event ->
        val swipe = event as TouchEvent.Swipe
        println("Diagonal swipe ${swipe.direction}")
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `diagonalSwipePlugin` is equivalent to calling `touchPlugin` with `TouchConfig(swipes = SwipeConfig(diagonalOnly = true))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Quick capture | Diagonal swipe to capture screenshot |
| Shortcut | Diagonal swipe to trigger app shortcut |
| Navigation | Diagonal swipe for custom navigation |
