---
title: "LongPressDrag"
weight: 27
---

# LongPressDrag

Detects a long press followed by a directional drag in Compose. Register with `longPressDragPlugin`.

## How to perform

Press and hold your finger briefly on the screen, then drag in any direction without lifting.

## Algorithm

Uses Compose's `detectDragGesturesAfterLongPress` to track the drag delta after a long press. Direction is determined by the dominant axis of movement (horizontal wins over vertical). The drag distance is also reported.

## Events

| Event | Properties | Description |
|-------|------------|-------------|
| `LongPressDragEvent` | `direction` — drag direction; `distance` — total drag distance in pixels | Long press + drag detected |

Directions: `LEFT`, `RIGHT`, `UP`, `DOWN`

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `minDragDistance` | Minimum drag distance in pixels to qualify | `20f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    longPressDragPlugin(context) { event ->
        println("Long press drag: ${event.direction}, distance: ${event.distance}")
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
| Drag to reorder | Long press then drag to reorder list items |
| Quick actions | Long press + direction for contextual actions |
| Game controls | Long press for charging, drag to aim |
| Drag and drop | Initiate drag-and-drop from a long press |
