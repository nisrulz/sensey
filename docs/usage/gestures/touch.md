---
title: "Touch (Generic)"
weight: 19
---

# Touch

This is the **generic** touch gesture plugin, `touchPlugin`. All other touch plugins (`cornerSwipePlugin`, `edgeSwipePlugin`, `diagonalSwipePlugin`, `longPressDragPlugin`, `twoFingerSwipePlugin`, `pinchScalePlugin`) are convenience wrappers that configure this plugin internally with a pre-configured `TouchConfig`.

Detects tap, double-tap, long-press, swipe, scroll, two-finger tap, edge swipe, corner swipe, diagonal swipe, two-finger swipe, long-press drag, and pinch/scale gestures in Compose.

## How to perform

Tap, double-tap, long-press, swipe, scroll, pinch, or multi-touch on the screen to trigger different events.

## Algorithm

The touch plugin uses multiple Compose gesture detectors internally:
- `detectTapGestures` for taps, double-taps, long-presses, and N-taps
- `detectDragGestures` for swipe/scroll with velocity-based classification
- `detectTransformGestures` for pinch/scale with debounced confirmation
- `detectDragGesturesAfterLongPress` for long-press-then-drag
- Manual pointer event tracking for two-finger swipes

Swipe direction is determined by partitioning the atan2 angle into eight quadrants. Edge/corner detection checks if the touch start position falls within a configurable zone.

## Events

| Event | Description |
|-------|-------------|
| `TouchEvent.Tap.Single` | Single tap |
| `TouchEvent.Tap.Double` | Double tap |
| `TouchEvent.Tap.NTap(count)` | N consecutive taps within the time window (default `count=3`) |
| `TouchEvent.LongPress` | Long press |
| `TouchEvent.TwoFingerTap` | Two finger tap |
| `TouchEvent.Swipe(direction, origin, fingerCount)` | Swipe with direction, origin (Any/Edge/Corner), and finger count |
| `TouchEvent.Scroll(direction)` | Scroll (low-velocity drag) |
| `TouchEvent.LongPressDrag(direction, distance)` | Long press followed by a directional drag |
| `TouchEvent.PinchScale(scaleFactor, isScalingOut)` | Pinch in or out (zoom) |

Directions: `Direction` enum with `UP`, `DOWN`, `LEFT`, `RIGHT`, `UP_RIGHT`, `UP_LEFT`, `DOWN_RIGHT`, `DOWN_LEFT`.

Swipe origins: `SwipeOrigin` sealed interface with `Any`, `Edge(type)`, `Corner(type)`.

## Configuration

Each gesture feature is independently controlled by its own config sub-object in `TouchConfig`:

| Feature | Config | Default | Key fields |
|---------|--------|---------|------------|
| Taps | `taps` | enabled | `nTapCount`, `nTapWindowMs` |
| Basic swipe/scroll | `swipe` | enabled | `minDistance`, `velocityThreshold`, `diagonalOnly` |
| Edge swipe | `edgeSwipe` | disabled | `edgeThresholdDp`, `enabledEdges` |
| Corner swipe | `cornerSwipe` | disabled | `cornerRadiusDp`, `enabledCorners` |
| Two-finger swipe | `twoFingerSwipe` | disabled | `minDistance` |
| Long-press drag | `longPressDrag` | disabled | `minDistance` |
| Pinch/scale | `pinchScale` | disabled | — |

Enable any combination independently — there are no implicit gating rules.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touch.TouchConfig
import com.github.nisrulz.sensey.gesture.touch.SwipeConfig
import com.github.nisrulz.sensey.gesture.touch.EdgeSwipeConfig

// Default — taps + swipe/scroll only
SenseyGestureEffect(lifecycle) {
    touchPlugin(context) { event ->
        when (event) {
            is TouchEvent.Tap.Single     -> println("Single tap")
            is TouchEvent.Tap.Double     -> println("Double tap")
            is TouchEvent.Tap.NTap       -> println("${event.count}-tap")
            is TouchEvent.LongPress      -> println("Long press")
            is TouchEvent.Swipe          -> println("Swipe ${event.direction}")
            is TouchEvent.Scroll         -> println("Scroll ${event.direction}")
            else -> {}
        }
    }
}

// Custom — edge swipes only, no taps
SenseyGestureEffect(lifecycle) {
    touchPlugin(
        context = context,
        config = TouchConfig(
            edgeSwipe = EdgeSwipeConfig(enabled = true),
        ),
    ) { event ->
        if (event is TouchEvent.Swipe) {
            println("Edge swipe: ${event.origin} → ${event.direction}")
        }
    }
}

// Differentiate by both edge origin and direction:
SenseyGestureEffect(lifecycle) {
    touchPlugin(
        context = context,
        config = TouchConfig(
            edgeSwipe = EdgeSwipeConfig(enabled = true),
        ),
    ) { event ->
        val swipe = event as? TouchEvent.Swipe ?: return@touchPlugin
        val origin = swipe.origin as? TouchEvent.SwipeOrigin.Edge ?: return@touchPlugin
        when (origin.type) {
            TouchEvent.EdgeType.TOP -> when (swipe.direction) {
                TouchEvent.Direction.LEFT  -> // top edge, left swipe
                TouchEvent.Direction.RIGHT -> // top edge, right swipe
                else -> {}
            }
            TouchEvent.EdgeType.BOTTOM -> when (swipe.direction) {
                TouchEvent.Direction.LEFT  -> // bottom edge, left swipe
                TouchEvent.Direction.RIGHT -> // bottom edge, right swipe
                else -> {}
            }
            else -> {}
        }
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content that receives touch gestures
}
```

## Convenience wrappers

Each of the following functions is a thin wrapper around `touchPlugin`:

| Function | Equivalent config |
|----------|-------------------|
| `edgeSwipePlugin(context) { }` | `TouchConfig(edgeSwipe = EdgeSwipeConfig(enabled = true))` |
| `cornerSwipePlugin(context) { }` | `TouchConfig(cornerSwipe = CornerSwipeConfig(enabled = true))` |
| `diagonalSwipePlugin(context) { }` | `TouchConfig(swipe = SwipeConfig(diagonalOnly = true))` |
| `longPressDragPlugin(context) { }` | `TouchConfig(longPressDrag = LongPressDragConfig(enabled = true))` |
| `twoFingerSwipePlugin(context) { }` | `TouchConfig(twoFingerSwipe = TwoFingerSwipeConfig(enabled = true))` |
| `pinchScalePlugin(context) { }` | `TouchConfig(pinchScale = PinchScaleConfig(enabled = true))` |

All wrappers dispatch `TouchEvent` — use the same `when` block as `touchPlugin`.

## Use cases

| Gesture | Scenario | Description |
|---------|----------|-------------|
| Single tap | Select | Tap to select an item |
| Single tap | Submit | Tap button to submit a form |
| Single tap | Open | Tap to open a link or file |
| Double tap | Like | Double-tap to like a post |
| Double tap | Zoom | Double-tap to zoom content |
| Long press | Context menu | Long-press to show context menu |
| Long press | Preview | Long-press for content preview |
| Swipe | Navigate | Swipe to navigate between pages |
| Swipe | Dismiss | Swipe to dismiss notification |
| Swipe | Delete | Swipe to delete an item |
| Scroll | Browse | Scroll through content |
| N-tap | Power user | Triple-tap for quick actions |
| Edge swipe | Back | Edge swipe for back navigation |
| Corner swipe | Quick action | Swipe from corner for shortcuts |
| Pinch | Zoom | Pinch to zoom in/out |
| Long press drag | Reorder | Long press then drag to reorder items |
| Two-finger tap | Zoom fit | Two-finger tap to fit content |
