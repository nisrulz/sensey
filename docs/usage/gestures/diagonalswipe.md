---
title: "DiagonalSwipe"
weight: 25
---

# DiagonalSwipe

Detects diagonal swipe gestures inside a composable. Register with `diagonalSwipePlugin`.

## How to perform

Swipe diagonally across the screen — for example, from bottom-left toward top-right.

## Algorithm

Computes the angle of a drag gesture via `atan2`. If the angle falls within `angleToleranceDeg` of a true diagonal (45°, 135°, -135°, -45°) and the total drag distance exceeds `minDragDistance`, a `DiagonalSwipeEvent` is emitted with the matched direction.

Purely horizontal or vertical swipes do not trigger — the angle must be near-diagonal.

## Events

| Event | Description |
|-------|-------------|
| `DiagonalSwipeEvent(direction)` | Diagonal swipe detected |

Directions: `UP_RIGHT`, `DOWN_RIGHT`, `DOWN_LEFT`, `UP_LEFT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum total drag distance in pixels to qualify as a swipe | `80f` |
| `angleToleranceDeg` | Degrees away from true diagonal (45°) to still count as diagonal | `22.5f` |

With the default tolerance, any swipe between 22.5° and 67.5° from horizontal is classified as a diagonal.

## Usage

```kotlin
senseyRegister(lifecycle) {
    diagonalSwipePlugin(
        minDragDistance = 80f,
        angleToleranceDeg = 22.5f,
    ) { event ->
        println("Diagonal swipe: ${event.direction}") // UP_RIGHT, DOWN_RIGHT, etc.
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
| Diagonal swipe to navigate | Swipe diagonally to switch between tabs |
| Game controls | Diagonal swipe for directional input in games |
| Photo gallery | Diagonal swipe to select multiple photos |
| Shortcut gesture | Diagonal swipe as a power-user shortcut |
