---
title: "PinchScale"
weight: 35
---

# PinchScale

Detects pinch-in and pinch-out (scale) gestures. This is a **convenience wrapper** around `touchPlugin` — internally configures `PinchScaleConfig(enabled = true)`. See [touch plugin](touch.md) for the full event hierarchy.

## How to perform

Place two fingers on the screen and pinch together or spread apart.

## Algorithm

Uses Compose's `detectTransformGestures` to monitor the scale factor. Events are debounced: consecutive readings must exceed the threshold (1.01 for pinch-in, 0.99 for pinch-out) before the event fires, reducing jitter. Dispatches `TouchEvent.PinchScale`.

## Events

| Event | Properties | Description |
|-------|------------|-------------|
| `TouchEvent.PinchScale` | `scaleFactor` — raw scale factor; `isScalingOut` — `true` for pinch-out, `false` for pinch-in | Pinch/scale detected |

## Parameters

This wrapper has no configurable parameters.

## Usage

```kotlin
import com.github.nisrulz.sensey.gesture.touch.TouchEvent

senseyRegister(lifecycle) {
    pinchScalePlugin(context) { event ->
        val pinch = event as TouchEvent.PinchScale
        if (pinch.isScalingOut) {
            println("Zooming out: ${pinch.scaleFactor}")
        } else {
            println("Zooming in: ${pinch.scaleFactor}")
        }
    }
}

Box(modifier = Modifier.fillMaxSize().senseyGestures())
```

Note: `pinchScalePlugin` is equivalent to calling `touchPlugin` with `TouchConfig(pinchScale = PinchScaleConfig(enabled = true))`.

## Use cases

| Scenario | Description |
|----------|-------------|
| Zoom | Pinch to zoom in/out on images or maps |
| Adjust | Pinch to adjust volume, brightness, etc. |
| Dismiss | Pinch-out to dismiss or close content |
