---
title: "CornerSwipe"
weight: 30
---

# CornerSwipe

Detects swipes that originate from any screen corner within a configurable radius. Register with `cornerSwipePlugin`.

## How to perform

Place your finger at a screen corner and swipe inward toward the center.

## Algorithm

Tracks drag gestures via Compose's `detectDragGestures`. When a drag starts within `cornerRadiusDp` of any enabled corner, the starting corner is identified and the swipe direction is classified by the dominant axis of movement from the start point.

## Events

| Event | Properties | Description |
|-------|------------|-------------|
| `CornerSwipeEvent` | `corner` — originating corner; `direction` — swipe direction | Corner swipe detected |

Corners: `TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`
Directions: `LEFT`, `RIGHT`, `UP`, `DOWN`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `cornerRadiusDp` | Distance from the corner (in dp) within which a drag must start | `48.dp` |
| `enabledCorners` | Which corners to monitor for swipes | `setOf(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT)` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    cornerSwipePlugin(
        context,
        cornerRadiusDp = 48.dp,
        enabledCorners = setOf(CornerSwipeEvent.Corner.TOP_LEFT, CornerSwipeEvent.Corner.TOP_RIGHT),
    ) { event ->
        println("Corner swipe from ${event.corner} going ${event.direction}")
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
| Quick settings | Swipe from top-left or top-right corner for quick settings |
| Back navigation | Swipe from bottom-left corner to go back |
| Assistant | Swipe from bottom-right corner to trigger assistant |
| Shortcut gestures | Different corners trigger different actions |
