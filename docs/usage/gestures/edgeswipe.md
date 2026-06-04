---
title: "EdgeSwipe"
weight: 31
---

# EdgeSwipe

Detects swipes that originate from a screen edge. This is a **convenience wrapper** around `touchPlugin` — internally configures `SwipeConfig(edgeDetection = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Place your finger at a screen edge and swipe inward.

## Algorithm

Tracks drag gestures via Compose's `detectDragGestures`. When a drag starts within `edgeThresholdDp` of any enabled edge, the originating edge is identified. Dispatches `TouchEvent.Swipe` with `SwipeOrigin.Edge`.

## Events

| Event | Description |
|-------|-------------|
| `TouchEvent.Swipe(direction, origin = SwipeOrigin.Edge(type), fingerCount = 1)` | Edge swipe detected |

Edges (via `SwipeOrigin.Edge`): `LEFT`, `RIGHT`, `TOP`, `BOTTOM`
Directions: `UP`, `DOWN`, `LEFT`, `RIGHT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `edgeThresholdDp` | Distance from the edge (in dp) within which a drag must start | `48.dp` |
| `enabledEdges` | Which edges to monitor for swipes | `setOf(LEFT, RIGHT, TOP, BOTTOM)` |

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.Direction
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.SwipeOrigin

senseyRegister(lifecycle) {
    edgeSwipePlugin(context) { event ->
        val swipe = event as TouchEvent.Swipe
        val edge = (swipe.origin as SwipeOrigin.Edge).type
        println("Edge swipe from $edge going ${swipe.direction}")
    }
}

// Differentiate by both edge AND direction:
senseyRegister(lifecycle) {
    edgeSwipePlugin(context) { event ->
        val swipe = event as TouchEvent.Swipe
        val origin = swipe.origin as SwipeOrigin.Edge
        when (origin.type) {
            TouchEvent.EdgeType.TOP -> when (swipe.direction) {
                Direction.LEFT  -> // top edge, swiping left
                Direction.RIGHT -> // top edge, swiping right
            }
            TouchEvent.EdgeType.LEFT -> when (swipe.direction) {
                Direction.UP   -> // left edge, swiping up
                Direction.DOWN -> // left edge, swiping down
            }
            TouchEvent.EdgeType.RIGHT -> when (swipe.direction) {
                Direction.UP   -> // right edge, swiping up
                Direction.DOWN -> // right edge, swiping down
            }
            TouchEvent.EdgeType.BOTTOM -> when (swipe.direction) {
                Direction.LEFT  -> // bottom edge, swiping left
                Direction.RIGHT -> // bottom edge, swiping right
            }
        }
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `edgeSwipePlugin` is equivalent to calling `touchPlugin` with `TouchConfig(swipes = SwipeConfig(edgeDetection = true))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Back navigation | Swipe from left or right edge for back gesture |
| Sidebar | Swipe from left edge to open sidebar |
| Notifications | Swipe from top edge to open notifications |
| Quick settings | Swipe from bottom edge to open quick settings |
