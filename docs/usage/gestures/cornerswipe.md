---
title: "CornerSwipe"
weight: 30
---

# CornerSwipe

Detects swipes that originate from any screen corner within a configurable radius. This is a **convenience wrapper** around `touchPlugin` — internally configures `SwipeConfig(cornerDetection = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Place your finger at a screen corner and swipe inward toward the center.

## Algorithm

Tracks drag gestures via Compose's `detectDragGestures`. When a drag starts within `cornerRadiusDp` of any enabled corner, the starting corner is identified and the swipe direction is classified by the dominant axis of movement from the start point. Dispatches `TouchEvent.Swipe` with `SwipeOrigin.Corner`.

## Events

| Event | Description |
|-------|-------------|
| `TouchEvent.Swipe(direction, origin = SwipeOrigin.Corner(type), fingerCount = 1)` | Corner swipe detected |

Corners (via `SwipeOrigin.Corner`): `TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`
Directions: `UP`, `DOWN`, `LEFT`, `RIGHT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `cornerRadiusDp` | Distance from the corner (in dp) within which a drag must start | `48.dp` |
| `enabledCorners` | Which corners to monitor for swipes | `setOf(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT)` |

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.SwipeOrigin

senseyRegister(lifecycle) {
    cornerSwipePlugin(
        context,
        cornerRadiusDp = 48.dp,
        enabledCorners = setOf(TouchEvent.CornerType.TOP_LEFT, TouchEvent.CornerType.TOP_RIGHT),
    ) { event ->
        val swipe = event as TouchEvent.Swipe
        val corner = (swipe.origin as SwipeOrigin.Corner).type
        println("Corner swipe from $corner going ${swipe.direction}")
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `cornerSwipePlugin` is equivalent to calling `touchPlugin` with `TouchConfig(swipes = SwipeConfig(cornerDetection = true, cornerRadiusDp = ..., enabledCorners = ...))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Quick settings | Swipe from top-left or top-right corner for quick settings |
| Back navigation | Swipe from bottom-left corner to go back |
| Assistant | Swipe from bottom-right corner to trigger assistant |
| Shortcut gestures | Different corners trigger different actions |
