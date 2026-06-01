---
title: "EdgeSwipe"
weight: 24
---

# EdgeSwipe

Detects swipes that originate near the edge of a composable. Register with `edgeSwipePlugin`.

## How to perform

Place your finger at the edge of the screen and swipe inward.

## Algorithm

Tracks drag gestures via Compose's `detectDragGestures`. When a drag starts within `edgeThresholdDp` of any enabled composable edge and travels a sufficient distance, an `EdgeSwipeEvent` is emitted with the originating edge.

## Events

| Event | Description |
|-------|-------------|
| `EdgeSwipeEvent(edge)` | Swipe originated from the given edge |

Edges: `Edge.LEFT`, `Edge.RIGHT`, `Edge.TOP`, `Edge.BOTTOM`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `edgeThresholdDp` | Distance from the composable edge (in dp) within which a drag must start to qualify as an edge swipe | `48.dp` |
| `enabledEdges` | Which composable edges to monitor for edge swipes | `setOf(LEFT, RIGHT, TOP, BOTTOM)` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    edgeSwipePlugin(
        edgeThresholdDp = 48.dp,
        enabledEdges = setOf(Edge.LEFT, Edge.RIGHT),
    ) { event ->
        println("Edge swipe from: ${event.edge}") // LEFT, RIGHT, TOP, or BOTTOM
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
| Swipe to go back | Swipe from left edge to navigate back |
| Swipe to reveal | Swipe from right edge to open drawer |
| Swipe to refresh | Swipe from top edge to pull-to-refresh |
| Swipe to expand | Swipe from bottom edge to open sheet |
| Navigation drawer | Open drawer with left-edge swipe |
| Side menu | Swipe from left or right edge to show menu |
