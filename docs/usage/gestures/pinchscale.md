---
title: "PinchScale"
weight: 18
---

# PinchScale

Detects pinch-to-zoom gestures (scale in/out) in Compose. Register with `pinchScalePlugin`.

## Algorithm

The algorithm monitors the scale factor from `detectTransformGestures`. When the factor exceeds 1.01 (pinch-in) or falls below 0.99 (pinch-out), consecutive readings are counted. The event is only emitted after a confirmation count (2 consecutive readings) is reached, providing debounce against jittery touch input. Tracks the last emitted direction so opposite-direction events can be reported.

## Events

| Event | Properties |
|-------|------------|
| `PinchScaleEvent` | `scaleFactor` — current pinch zoom factor; `isScalingOut` — `true` for pinch-out (zoom out), `false` for pinch-in (zoom in) |

## Parameters

This plugin has no configurable parameters.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

SenseyGestureEffect(lifecycle) {
    pinchScalePlugin(context) { event ->
        if (event.isScalingOut) println("Scaling out: ${event.scaleFactor}") // pinch-out (zoom out)
        else println("Scaling in: ${event.scaleFactor}") // pinch-in (zoom in)
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content that receives pinch gestures
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Image zoom | Pinch to zoom in/out on images |
| Map zoom | Zoom in/out on maps with pinch gesture |
| Text resize | Adjust font size with pinch gesture |
| Canvas zoom | Zoom in/out on drawing canvas |
| Photo gallery | Pinch to zoom photo thumbnails |
| Video timeline | Pinch to zoom video timeline for precision |
| Document viewer | Pinch to zoom in/out on documents |
