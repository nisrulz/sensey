---
title: "TouchType"
weight: 19
---

# TouchType

Detects various touch gestures (tap, double-tap, long-press, swipe, scroll, multi-finger tap) in Compose. Register with `touchTypePlugin`.

## Algorithm

The algorithm classifies drag gestures as swipes or scrolls based on velocity: high-velocity drags are swipes, low-velocity drags are scrolls. Direction is determined by the atan2 angle partitioned into eight quadrants. For tap gestures, consecutive taps within a fixed short window are accumulated and dispatched as `NTap(3)` when the count reaches three.

## Events

| Event | Description |
|-------|-------------|
| `TouchTypeEvent.NTap(count)` | N consecutive taps within the time window (default `count=3`, 400 ms window) |
| `TouchTypeEvent.DoubleTap` | Double tap |
| `TouchTypeEvent.LongPress` | Long press |
| `TouchTypeEvent.SingleTap` | Single tap |
| `TouchTypeEvent.Swipe(direction)` | Swipe in any of 8 directions (high velocity) |
| `TouchTypeEvent.Scroll(direction)` | Scroll (tap-and-move, low velocity) |
| `TouchTypeEvent.ThreeFingerSingleTap` | Three finger tap |
| `TouchTypeEvent.TwoFingerSingleTap` | Two finger tap |

Directions: `TouchTypeEvent.Direction` enum with `UP`, `DOWN`, `LEFT`, `RIGHT`, `UP_RIGHT`, `UP_LEFT`, `DOWN_RIGHT`, `DOWN_LEFT`.

Swipe directions are reported with precise 8-direction names (e.g. `DOWN_RIGHT`), while scroll directions use coarse 4-direction names (e.g. `DOWN`) to reflect the typical granularity of scroll flings.

## Parameters

This plugin has no configurable parameters.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

SenseyGestureEffect(lifecycle) {
    touchTypePlugin(context) { event ->
        when (event) {
            is TouchTypeEvent.NTap    -> println("${event.count}-tap detected") // 3 consecutive quick taps
            TouchTypeEvent.DoubleTap -> println("Double tap")  // two rapid taps
            TouchTypeEvent.LongPress -> println("Long press")  // sustained touch
            TouchTypeEvent.SingleTap -> println("Single tap")  // single tap
            is TouchTypeEvent.Swipe  -> println("Swipe direction: ${event.direction}") // high-velocity drag
            is TouchTypeEvent.Scroll -> println("Scroll direction: ${event.direction}") // low-velocity drag
            TouchTypeEvent.ThreeFingerSingleTap -> println("Three finger tap")  // three-finger tap
            TouchTypeEvent.TwoFingerSingleTap   -> println("Two finger tap")    // two-finger tap
        }
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content that receives touch gestures
}
```

## Use cases

| Gesture | Scenario | Description |
|---------|----------|-------------|
| Single tap | Select | Tap to select an item |
| Single tap | Submit | Tap button to submit a form |
| Single tap | Open | Tap to open a link or file |
| Double tap | Like | Double-tap to like a post |
| Double tap | Zoom | Double-tap to zoom content |
| Double tap | Quick reply | Quick reply to message |
| Long press | Context menu | Long-press to show context menu |
| Long press | Drag | Long-press to start drag-and-drop |
| Long press | Preview | Long-press for content preview |
| Swipe | Navigate | Swipe to navigate between pages |
| Swipe | Dismiss | Swipe to dismiss notification |
| Swipe | Delete | Swipe to delete an item |
| Swipe | Reveal | Swipe to reveal actions |
| Scroll | Browse | Scroll through content |
| N-tap | Power user | Triple-tap for quick actions |
| N-tap | Debug | Triple-tap to open debug menu |
| Two-finger tap | Zoom fit | Two-finger tap to fit content to screen |
| Two-finger tap | Context | Two-finger tap for secondary action |
| Three-finger tap | Screenshot | Three-finger tap to capture screenshot |
| Three-finger tap | Split screen | Three-finger tap for split screen |
