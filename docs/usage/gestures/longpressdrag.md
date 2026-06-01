---
title: "LongPressDrag"
weight: 34
---

# LongPressDrag

Detects a long press followed by a directional drag gesture. This is a **convenience wrapper** around `touchPlugin` — internally configures `LongPressDragConfig(enabled = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Press and hold for a moment, then drag in a direction.

## Algorithm

Uses Compose's `detectDragGesturesAfterLongPress`. Once a long press is recognized, subsequent drag movement is tracked. When the drag exceeds `minDragDistance`, the direction is determined by the dominant axis and emitted as `TouchEvent.LongPressDrag`.

## Events

| Event | Properties | Description |
|-------|------------|-------------|
| `TouchEvent.LongPressDrag` | `direction` — drag direction; `distance` — total drag distance in pixels | Long-press drag detected |

Directions: `UP`, `DOWN`, `LEFT`, `RIGHT`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum drag distance in pixels | `20f` |

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent

senseyRegister(lifecycle) {
    longPressDragPlugin(context) { event ->
        val drag = event as TouchEvent.LongPressDrag
        println("Long-press drag ${drag.direction}, distance: ${drag.distance}")
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `longPressDragPlugin` is equivalent to calling `touchPlugin` with `TouchConfig(longPressDrag = LongPressDragConfig(enabled = true))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Reorder | Long-press and drag to reorder list items |
| Move | Long-press and drag to move an element |
| Quick action | Long-press and drag to trigger an action with direction |
