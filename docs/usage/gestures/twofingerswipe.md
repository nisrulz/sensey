---
title: "TwoFingerSwipe"
weight: 33
---

# TwoFingerSwipe

Detects directional two-finger swipe gestures. This is a **convenience wrapper** around `touchPlugin` — internally configures `SwipeConfig(enableTwoFinger = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Place two fingers on the screen and swipe in a direction.

## Algorithm

Tracks the centroid of two touch points via `awaitPointerEvent`. When the centroid moves beyond `minDragDistance`, the dominant axis direction is determined. Dispatches `TouchEvent.Swipe` with `fingerCount = 2`.

## Events

| Event | Description |
|-------|-------------|
| `TouchEvent.Swipe(direction, origin = Any, fingerCount = 2)` | Two-finger swipe detected |

Directions: `UP`, `DOWN`, `LEFT`, `RIGHT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum drag distance in pixels | `80f` |

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent

senseyRegister(lifecycle) {
    twoFingerSwipePlugin(context) { event ->
        val swipe = event as TouchEvent.Swipe
        println("Two-finger swipe ${swipe.direction}")
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `twoFingerSwipePlugin` is equivalent to calling `touchPlugin` with `TouchConfig(swipes = SwipeConfig(enableTwoFinger = true))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Zoom to fit | Two-finger swipe to fit content |
| Navigate tabs | Two-finger swipe to switch tabs |
| Custom gesture | Two-finger swipe for app-specific action |
