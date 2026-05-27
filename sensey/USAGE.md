# Usage

## Setup

Add the dependency:

```kotlin
// build.gradle.kts (module)
implementation("com.github.nisrulz:sensey:{latest-version}")
```

Initialize in your `Application` or `Activity`:

```kotlin
Sensey.init(this)
```

Release in `onDestroy` or `onPause`:

```kotlin
Sensey.stop()
```

---

## Overview

Each gesture follows a consistent pattern:

1. **Event sealed interface** — defines the gesture outcomes (e.g., `ShakeEvent.Detected`, `ShakeEvent.Stopped`)
2. **Trigger** — pure Kotlin class implementing `GestureTrigger<T>`, contains the detection algorithm (no Android dependencies)
3. **Detector** — thin class that bridges Android `SensorEvent` → trigger → dispatcher

You can either use the **Sensey facade** (recommended) or create **standalone trigger/detector** instances.

---

**Using the Sensey facade:**

```kotlin
Sensey.startShakeDetection { event ->
    when (event) {
        ShakeEvent.Detected -> // handle
        ShakeEvent.Stopped   -> // handle
    }
}
```

> Store the lambda reference if you need to stop detection later:
> ```kotlin
> private val shakeDispatcher: (ShakeEvent) -> Unit = { event -> ... }
> Sensey.startShakeDetection(shakeDispatcher)
> Sensey.stopShakeDetection(shakeDispatcher)
> ```

---

### Shake

| Event | Description |
|-------|-------------|
| `ShakeEvent.Detected` | Device is being shaken |
| `ShakeEvent.Stopped` | Shaking has stopped |

```kotlin
Sensey.startShakeDetection { event ->
    when (event) {
        ShakeEvent.Detected -> println("Shake detected!")
        ShakeEvent.Stopped  -> println("Shake stopped")
    }
}
```

With custom parameters:

```kotlin
Sensey.startShakeDetection(
    threshold = 10f,
    timeBeforeDeclaringShakeStopped = 2000L,
) { event -> /* ... */ }
```

**Standalone:**

```kotlin
val trigger = ShakeTrigger(threshold = 3f)
val detector = ShakeDetector(trigger) { event -> /* ... */ }
```

---

### Flip

| Event | Description |
|-------|-------------|
| `FlipEvent.FaceUp` | Device is face-up (screen up) |
| `FlipEvent.FaceDown` | Device is face-down (screen down) |

```kotlin
Sensey.startFlipDetection { event ->
    when (event) {
        FlipEvent.FaceUp   -> println("Face up")
        FlipEvent.FaceDown -> println("Face down")
    }
}
```

---

### Light

| Event | Description |
|-------|-------------|
| `LightEvent.Dark` | Ambient light below threshold |
| `LightEvent.Light` | Ambient light above threshold |

Hysteresis prevents oscillation at boundaries.

```kotlin
Sensey.startLightDetection { event ->
    when (event) {
        LightEvent.Dark  -> println("Dark")
        LightEvent.Light -> println("Light")
    }
}
```

With custom thresholds:

```kotlin
Sensey.startLightDetection(darkThreshold = 5f) { event -> /* ... */ }
```

---

### Proximity

| Event | Description |
|-------|-------------|
| `ProximityEvent.Near` | Object is near the device |
| `ProximityEvent.Far` | Object moved away |

```kotlin
Sensey.startProximityDetection { event ->
    when (event) {
        ProximityEvent.Near -> println("Near")
        ProximityEvent.Far  -> println("Far")
    }
}
```

---

### Movement

| Event | Description |
|-------|-------------|
| `MovementEvent.Moved` | Device is moving |
| `MovementEvent.Stationary` | Device has been still for the timeout period |

```kotlin
Sensey.startMovementDetection { event ->
    when (event) {
        MovementEvent.Moved      -> println("Moving")
        MovementEvent.Stationary -> println("Stationary")
    }
}
```

With custom parameters:

```kotlin
Sensey.startMovementDetection(
    threshold = 0.5f,
    timeBeforeDeclaringStationary = 3000L,
) { event -> /* ... */ }
```

---

### Chop

| Event | Description |
|-------|-------------|
| `ChopEvent.Chopped` | Chop gesture detected |

```kotlin
Sensey.startChopDetection { event ->
    println("Chop detected!")
}
```

With custom parameters:

```kotlin
Sensey.startChopDetection(threshold = 30f, timeForChopGesture = 500L) { event -> /* ... */ }
```

---

### WristTwist

| Event | Description |
|-------|-------------|
| `WristTwistEvent.Twisted` | Wrist twist gesture detected |

```kotlin
Sensey.startWristTwistDetection { event ->
    println("Wrist twist detected!")
}
```

---

### Wave

| Event | Description |
|-------|-------------|
| `WaveEvent.Waved` | Hand wave over proximity sensor detected |

```kotlin
Sensey.startWaveDetection { event ->
    println("Wave detected!")
}
```

With custom time window:

```kotlin
Sensey.startWaveDetection(timeWindowMillis = 500f) { event -> /* ... */ }
```

A debounce of 1 second prevents rapid successive waves.

---

### Scoop

| Event | Description |
|-------|-------------|
| `ScoopEvent.Scooped` | Scoop gesture detected |

```kotlin
Sensey.startScoopDetection { event ->
    println("Scoop detected!")
}
```

---

### PickupDevice

| Event | Description |
|-------|-------------|
| `PickupDeviceEvent.PickedUp` | Device was picked up |
| `PickupDeviceEvent.PutDown` | Device was put down |

```kotlin
Sensey.startPickupDeviceDetection { event ->
    when (event) {
        PickupDeviceEvent.PickedUp -> println("Picked up")
        PickupDeviceEvent.PutDown  -> println("Put down")
    }
}
```

---

### Orientation

| Event | Description |
|-------|-------------|
| `OrientationEvent.TopSideUp` | Top edge pointing up |
| `OrientationEvent.BottomSideUp` | Bottom edge pointing up |
| `OrientationEvent.LeftSideUp` | Left edge pointing up |
| `OrientationEvent.RightSideUp` | Right edge pointing up |

```kotlin
Sensey.startOrientationDetection { event ->
    when (event) {
        OrientationEvent.TopSideUp    -> println("Top up")
        OrientationEvent.BottomSideUp -> println("Bottom up")
        OrientationEvent.LeftSideUp   -> println("Left up")
        OrientationEvent.RightSideUp  -> println("Right up")
    }
}
```

With smoothness:

```kotlin
Sensey.startOrientationDetection(smoothness = 3) { event -> /* ... */ }
```

---

### TiltDirection

| Event | Description |
|-------|-------------|
| `TiltDirectionEvent.AxisXTilt(direction)` | Tilt around X axis |
| `TiltDirectionEvent.AxisYTilt(direction)` | Tilt around Y axis |
| `TiltDirectionEvent.AxisZTilt(direction)` | Tilt around Z axis |

Directions: `TiltDirectionTrigger.DIRECTION_CLOCKWISE` (0) or `TiltDirectionTrigger.DIRECTION_ANTICLOCKWISE` (1).

The dominant axis (highest magnitude) is reported.

```kotlin
Sensey.startTiltDirectionDetection { event ->
    when (event) {
        is TiltDirectionEvent.AxisXTilt -> println("X: ${event.direction}")
        is TiltDirectionEvent.AxisYTilt -> println("Y: ${event.direction}")
        is TiltDirectionEvent.AxisZTilt -> println("Z: ${event.direction}")
    }
}
```

---

### RotationAngle

| Event | Properties |
|-------|------------|
| `RotationAngleEvent` | `angleInAxisX`, `angleInAxisY`, `angleInAxisZ` in degrees |

Fires only when at least one angle changes by more than 1 degree from the previous reading.

```kotlin
Sensey.startRotationAngleDetection { event ->
    println("X: ${event.angleInAxisX}, Y: ${event.angleInAxisY}, Z: ${event.angleInAxisZ}")
}
```

---

### SoundLevel

| Event | Properties |
|-------|------------|
| `SoundLevelEvent` | `level` — sound level (0–100 scale) |

Requires `RECORD_AUDIO` permission.

```kotlin
Sensey.startSoundLevelDetection(context) { event ->
    println("Sound level: ${event.level} dB")
}
```

---

### Step

| Event | Properties |
|-------|------------|
| `StepEvent` | `steps`, `distanceInMeters`, `activityType` |

Activity type: `StepDetectorUtil.ACTIVITY_STILL` (0), `ACTIVITY_WALKING` (1), `ACTIVITY_RUNNING` (2).

Auto-selects `StepDetectorPostKitKat` (if step counter sensor available) or `StepDetectorPreKitKat` (accelerometer-based).

```kotlin
Sensey.startStepDetection(context, StepDetectorUtil.MALE) { event ->
    println("Steps: ${event.steps}, Distance: ${event.distanceInMeters}m")
}
```

---

### PinchScale

| Event | Properties |
|-------|------------|
| `PinchScaleEvent` | `scaleFactor`, `isScalingOut` |

Requires touch events dispatched to Sensey via `setupDispatchTouchEvent`.

```kotlin
// Override dispatchTouchEvent in Activity
override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    Sensey.setupDispatchTouchEvent(event)
    return super.dispatchTouchEvent(event)
}

// Start detection
Sensey.startPinchScaleDetection(context) { event ->
    if (event.isScalingOut) println("Scaling out: ${event.scaleFactor}")
    else println("Scaling in: ${event.scaleFactor}")
}
```

---

### TouchType

| Event | Description |
|-------|-------------|
| `TouchTypeEvent.DoubleTap` | Double tap |
| `TouchTypeEvent.LongPress` | Long press |
| `TouchTypeEvent.SingleTap` | Single tap |
| `TouchTypeEvent.Swipe(direction)` | Swipe in any of 8 directions |
| `TouchTypeEvent.Scroll(direction)` | Scroll (tap-and-move) |
| `TouchTypeEvent.ThreeFingerSingleTap` | Three finger tap |
| `TouchTypeEvent.TwoFingerSingleTap` | Two finger tap |

Swipe directions: `SWIPE_DIR_UP`, `SWIPE_DIR_DOWN`, `SWIPE_DIR_LEFT`, `SWIPE_DIR_RIGHT`,
`SWIPE_DIR_UP_LEFT`, `SWIPE_DIR_UP_RIGHT`, `SWIPE_DIR_DOWN_LEFT`, `SWIPE_DIR_DOWN_RIGHT`.

Scroll directions: `SCROLL_DIR_UP`, `SCROLL_DIR_DOWN`, `SCROLL_DIR_LEFT`, `SCROLL_DIR_RIGHT`.

```kotlin
// Override dispatchTouchEvent in Activity
override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    Sensey.setupDispatchTouchEvent(event)
    return super.dispatchTouchEvent(event)
}

// Start detection
Sensey.startTouchTypeDetection(context) { event ->
    when (event) {
        TouchTypeEvent.DoubleTap -> println("Double tap")
        TouchTypeEvent.LongPress -> println("Long press")
        TouchTypeEvent.SingleTap -> println("Single tap")
        is TouchTypeEvent.Swipe  -> println("Swipe direction: ${event.direction}")
        is TouchTypeEvent.Scroll -> println("Scroll direction: ${event.direction}")
        TouchTypeEvent.ThreeFingerSingleTap -> println("Three finger tap")
        TouchTypeEvent.TwoFingerSingleTap   -> println("Two finger tap")
    }
}
```
