---
title: "TouchType"
weight: 19
---

# TouchType

Detects various touch gestures in Compose. Register with `touchTypePlugin`.

## Events

| Event | Description |
|-------|-------------|
| `TouchTypeEvent.NTap(count)` | N consecutive taps within the time window (default `count=3`, 400ms window) |
| `TouchTypeEvent.DoubleTap` | Double tap |
| `TouchTypeEvent.LongPress` | Long press |
| `TouchTypeEvent.SingleTap` | Single tap |
| `TouchTypeEvent.Swipe(direction)` | Swipe in any of 8 directions |
| `TouchTypeEvent.Scroll(direction)` | Scroll (tap-and-move) |
| `TouchTypeEvent.ThreeFingerSingleTap` | Three finger tap |
| `TouchTypeEvent.TwoFingerSingleTap` | Two finger tap |

Directions: `TouchTypeEvent.Direction` enum with `UP`, `DOWN`, `LEFT`, `RIGHT`,
`UP_RIGHT`, `UP_LEFT`, `DOWN_RIGHT`, `DOWN_LEFT`.

Requires `Modifier.senseyGestures()` on the composable that receives touch input.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

SenseyGestureEffect(lifecycle) {
    touchTypePlugin(context) { event ->
        when (event) {
            is TouchTypeEvent.NTap    -> println("${event.count}-tap detected")
            TouchTypeEvent.DoubleTap -> println("Double tap")
            TouchTypeEvent.LongPress -> println("Long press")
            TouchTypeEvent.SingleTap -> println("Single tap")
            is TouchTypeEvent.Swipe  -> println("Swipe direction: ${event.direction}")
            is TouchTypeEvent.Scroll -> println("Scroll direction: ${event.direction}")
            TouchTypeEvent.ThreeFingerSingleTap -> println("Three finger tap")
            TouchTypeEvent.TwoFingerSingleTap   -> println("Two finger tap")
        }
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content
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
