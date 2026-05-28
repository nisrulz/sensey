---
title: "PinchScale"
weight: 18
---

# PinchScale

Detects pinch-to-zoom gestures in Compose. Register with `pinchScalePlugin`.

## Events

| Event | Properties |
|-------|------------|
| `PinchScaleEvent` | `scaleFactor`, `isScalingOut` |

Requires `Modifier.senseyGestures()` on the composable that receives touch input.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

SenseyGestureEffect(lifecycle) {
    pinchScalePlugin(context) { event ->
        if (event.isScalingOut) println("Scaling out: ${event.scaleFactor}")
        else println("Scaling in: ${event.scaleFactor}")
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content
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
