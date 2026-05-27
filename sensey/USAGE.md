# Usage

## Setup

Add the dependency:

```kotlin
// build.gradle.kts (module)
implementation("com.github.nisrulz:sensey:{latest-version}")
```

Initialize and register:

```kotlin
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

// In an Activity (lifecycle-aware, auto-stops on destroy):
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    senseyRegister(lifecycle) {
        // register sensor plugins here
    }
}

// In Compose (auto-stops on dispose):
@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        // register sensor and touch plugins here
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}

// In a Service or any Context:
context.senseyRegister {
    // register plugins here
}

// Manual stop:
// senseyStop()
```

---

## Overview

Sensey uses a **plugin-based architecture**. Each gesture is a `GesturePlugin` that can be registered and unregistered independently.

**Register individual plugins:**

```kotlin
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.gesture.shakePlugin

Sensey.register(shakePlugin { event ->
    when (event) {
        ShakeEvent.Detected -> // handle
        ShakeEvent.Stopped  -> // handle
    }
})
```

**Register multiple plugins at once using the builder DSL:**

```kotlin
senseyRegister(lifecycle) {
    shakePlugin { event ->
        when (event) {
            ShakeEvent.Detected -> println("Shaking!")
            ShakeEvent.Stopped  -> println("Stopped")
        }
    }
    flipPlugin { event ->
        when (event) {
            FlipEvent.FaceUp   -> println("Face up")
            FlipEvent.FaceDown -> println("Face down")
        }
    }
}
```

**Unregister:**

```kotlin
// individual
Sensey.unregister(plugin)

// all
Sensey.unregisterAll()

// stop entirely (clears all + releases sensor manager)
senseyStop()
```

With lifecycle-aware `senseyRegister(lifecycle)`, all plugins auto-unregister on `ON_DESTROY`.

**Standalone (without Sensey facade):**

```kotlin
val trigger = ShakeTrigger(threshold = 3f)
val detector = ShakeDetector(trigger) { event -> /* ... */ }
```

---

## Common examples

### Shake to undo

```kotlin
senseyRegister(lifecycle) {
    shakePlugin(threshold = 8f, timeBeforeDeclaringShakeStopped = 2000L) { event ->
        when (event) {
            ShakeEvent.Detected -> undoLastAction()
            ShakeEvent.Stopped  -> // optional: clear shake UI indicator
        }
    }
}
```

### Flip to mute incoming call

```kotlin
senseyRegister(lifecycle) {
    flipPlugin { event ->
        when (event) {
            FlipEvent.FaceDown -> silenceRinger()
            FlipEvent.FaceUp   -> // no-op
        }
    }
}
```

### Wave to wake screen

```kotlin
senseyRegister(lifecycle) {
    wavePlugin(timeWindowMillis = 800f) {
        turnScreenOn()
    }
}
```

### Pickup to show notifications

```kotlin
senseyRegister(lifecycle) {
    pickupDevicePlugin { event ->
        when (event) {
            PickupDeviceEvent.PickedUp -> peekNotifications()
            PickupDeviceEvent.PutDown  -> sleep()
        }
    }
}
```

### Chop to toggle flashlight

```kotlin
senseyRegister(lifecycle) {
    chopPlugin(threshold = 30f, timeForChopGesture = 500L) {
        toggleFlashlight()
    }
}
```

### Tap on back for screenshot

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(
        angleThreshold = 1.5f,
        tapDebounceMs = 250L,
        tapSequenceTimeoutMs = 500L,
    ) {
        takeScreenshot()
    }
}
```

### Step counter in fitness tracker

```kotlin
senseyRegister(lifecycle) {
    stepPlugin(gender = StepDetectorUtil.MALE) { event ->
        val type = when (event.activityType) {
            StepDetectorUtil.ACTIVITY_RUNNING -> "Running"
            StepDetectorUtil.ACTIVITY_WALKING -> "Walking"
            else -> "Still"
        }
        updateFitnessData(event.steps, event.distanceInMeters, type)
    }
}
```

### Sound-level clap detection

```kotlin
private var lastClapTime = 0L

senseyRegister(lifecycle) {
    soundLevelPlugin(context) { event ->
        val now = System.currentTimeMillis()
        if (event.level > 80 && (now - lastClapTime) > 1000L) {
            lastClapTime = now
            onClapDetected()
        }
    }
}
```

### Orientation-aware media player

```kotlin
senseyRegister(lifecycle) {
    orientationPlugin { event ->
        when (event) {
            OrientationEvent.TopSideUp    -> enterPortrait()
            OrientationEvent.BottomSideUp -> enterPortrait()
            OrientationEvent.LeftSideUp   -> enterLandscape()
            OrientationEvent.RightSideUp  -> enterLandscape()
        }
    }
}
```

### Pocket mode (movement + proximity)

```kotlin
senseyRegister(lifecycle) {
    proximityPlugin { event ->
        when (event) {
            ProximityEvent.Near -> enablePocketMode()
            ProximityEvent.Far  -> disablePocketMode()
        }
    }
    movementPlugin { event ->
        when (event) {
            MovementEvent.Moved -> checkIfInPocket()
            MovementEvent.Stationary -> // device at rest
        }
    }
}
```

### Double tap to like (Compose)

```kotlin
@Composable
fun PhotoPost(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        touchTypePlugin(context) { event ->
            if (event is TouchTypeEvent.DoubleTap) {
                likePost()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

### Swipe to navigate (Compose)

```kotlin
@Composable
fun GestureNavigator(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        touchTypePlugin(context) { event ->
            when (event) {
                is TouchTypeEvent.Swipe -> when (event.direction) {
                    TouchTypeEvent.Direction.LEFT  -> navigateBack()
                    TouchTypeEvent.Direction.RIGHT -> navigateForward()
                    else -> {}
                }
                else -> {}
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

### Pinch to zoom (Compose)

```kotlin
@Composable
fun ZoomableImage(lifecycle: Lifecycle) {
    var scale by remember { mutableFloatStateOf(1f) }
    SenseyGestureEffect(lifecycle) {
        pinchScalePlugin(context) { event ->
            scale = (scale * event.scaleFactor).coerceIn(0.5f, 3f)
        }
    }
    Image(
        painter = ...,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .senseyGestures()
            .graphicsLayer(scaleX = scale, scaleY = scale),
    )
}
```

### Long press for context menu (Compose)

```kotlin
@Composable
fun ContextMenuTrigger(lifecycle: Lifecycle) {
    var showMenu by remember { mutableStateOf(false) }
    SenseyGestureEffect(lifecycle) {
        touchTypePlugin(context) { event ->
            if (event is TouchTypeEvent.LongPress) {
                showMenu = true
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
        // content
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(text = { Text("Action") }, onClick = { })
        }
    }
}
```

---

## Context-specific usage

### Activity

```kotlin
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senseyRegister(lifecycle) {
            shakePlugin { ... }
            flipPlugin { ... }
        }
    }
    // Auto-stops on lifecycle ON_DESTROY
    // Manual: senseyStop()
}
```

### Service

```kotlin
class SensorService : Service() {
    override fun onCreate() {
        super.onCreate()
        senseyRegister {
            shakePlugin { ... }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        senseyStop()
    }
}
```

### WorkManager Worker

> **Note:** Android 8+ background execution limits prevent sensor delivery to background workers.
> Use a `ForegroundService` instead. The `applicationContext` extension works for the rare
> case where sensor data is available:

```kotlin
class SensorWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        applicationContext.senseyRegister {
            shakePlugin { ... }
        }
        // ...
    }
}
```

### Jetpack Compose

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        shakePlugin { event ->
            when (event) {
                ShakeEvent.Detected -> println("Shake detected!")
                ShakeEvent.Stopped  -> println("Shake stopped")
            }
        }
        touchTypePlugin(context) { event ->
            when (event) {
                is TouchTypeEvent.SingleTap -> println("Single tap")
                is TouchTypeEvent.Swipe     -> println("Swipe ${event.direction}")
                is TouchTypeEvent.NTap      -> println("${event.count}-tap")
                else -> {}
            }
        }
    }

    // Apply to the composable that should receive touch input:
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

---

## All gestures

### Shake

| Event | Description |
|-------|-------------|
| `ShakeEvent.Detected` | Device is being shaken |
| `ShakeEvent.Stopped` | Shaking has stopped |

```kotlin
senseyRegister(lifecycle) {
    shakePlugin(threshold = 10f, timeBeforeDeclaringShakeStopped = 2000L) { event ->
        when (event) {
            ShakeEvent.Detected -> println("Shake detected!")
            ShakeEvent.Stopped  -> println("Shake stopped")
        }
    }
}
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
senseyRegister(lifecycle) {
    flipPlugin { event ->
        when (event) {
            FlipEvent.FaceUp   -> println("Face up")
            FlipEvent.FaceDown -> println("Face down")
        }
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
senseyRegister(lifecycle) {
    lightPlugin(darkThreshold = 5f) { event ->
        when (event) {
            LightEvent.Dark  -> println("Dark")
            LightEvent.Light -> println("Light")
        }
    }
}
```

---

### Proximity

| Event | Description |
|-------|-------------|
| `ProximityEvent.Near` | Object is near the device |
| `ProximityEvent.Far` | Object moved away |

```kotlin
senseyRegister(lifecycle) {
    proximityPlugin { event ->
        when (event) {
            ProximityEvent.Near -> println("Near")
            ProximityEvent.Far  -> println("Far")
        }
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
senseyRegister(lifecycle) {
    movementPlugin(threshold = 0.5f, timeBeforeDeclaringStationary = 3000L) { event ->
        when (event) {
            MovementEvent.Moved      -> println("Moving")
            MovementEvent.Stationary -> println("Stationary")
        }
    }
}
```

---

### Chop

| Event | Description |
|-------|-------------|
| `ChopEvent.Chopped` | Chop gesture detected |

```kotlin
senseyRegister(lifecycle) {
    chopPlugin(threshold = 30f, timeForChopGesture = 500L) {
        println("Chop detected!")
    }
}
```

---

### WristTwist

| Event | Description |
|-------|-------------|
| `WristTwistEvent.Twisted` | Wrist twist gesture detected |

```kotlin
senseyRegister(lifecycle) {
    wristTwistPlugin {
        println("Wrist twist detected!")
    }
}
```

---

### Wave

| Event | Description |
|-------|-------------|
| `WaveEvent.Waved` | Hand wave over proximity sensor detected |

A debounce of 1 second prevents rapid successive waves.

```kotlin
senseyRegister(lifecycle) {
    wavePlugin(timeWindowMillis = 500f) {
        println("Wave detected!")
    }
}
```

---

### Scoop

| Event | Description |
|-------|-------------|
| `ScoopEvent.Scooped` | Scoop gesture detected |

```kotlin
senseyRegister(lifecycle) {
    scoopPlugin {
        println("Scoop detected!")
    }
}
```

---

### PickupDevice

| Event | Description |
|-------|-------------|
| `PickupDeviceEvent.PickedUp` | Device was picked up |
| `PickupDeviceEvent.PutDown` | Device was put down |

```kotlin
senseyRegister(lifecycle) {
    pickupDevicePlugin { event ->
        when (event) {
            PickupDeviceEvent.PickedUp -> println("Picked up")
            PickupDeviceEvent.PutDown  -> println("Put down")
        }
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
senseyRegister(lifecycle) {
    orientationPlugin(smoothness = 3) { event ->
        when (event) {
            OrientationEvent.TopSideUp    -> println("Top up")
            OrientationEvent.BottomSideUp -> println("Bottom up")
            OrientationEvent.LeftSideUp   -> println("Left up")
            OrientationEvent.RightSideUp  -> println("Right up")
        }
    }
}
```

---

### TiltDirection

| Event | Description |
|-------|-------------|
| `TiltDirectionEvent.AxisXTilt(direction)` | Tilt around X axis |
| `TiltDirectionEvent.AxisYTilt(direction)` | Tilt around Y axis |
| `TiltDirectionEvent.AxisZTilt(direction)` | Tilt around Z axis |

Directions: `TiltDirectionEvent.Direction.CLOCKWISE` or `TiltDirectionEvent.Direction.ANTICLOCKWISE`.

The dominant axis (highest magnitude) is reported.

```kotlin
senseyRegister(lifecycle) {
    tiltDirectionPlugin { event ->
        when (event) {
            is TiltDirectionEvent.AxisXTilt -> println("X: ${event.direction}")
            is TiltDirectionEvent.AxisYTilt -> println("Y: ${event.direction}")
            is TiltDirectionEvent.AxisZTilt -> println("Z: ${event.direction}")
        }
    }
}
```

---

### RotationAngle

| Event | Properties |
|-------|------------|
| `RotationAngleEvent` | `angleInAxisX`, `angleInAxisY`, `angleInAxisZ` in degrees |

Fires only when at least one angle changes by more than `minAngleChange` from the previous reading.

```kotlin
senseyRegister(lifecycle) {
    rotationAnglePlugin { event ->
        println("X: ${event.angleInAxisX}, Y: ${event.angleInAxisY}, Z: ${event.angleInAxisZ}")
    }
}
```

---

### TapOnBack

Detects double-taps on the device back/side using gravity vector angle analysis.
Single taps are ignored as false positives — only two rapid taps trigger.

```kotlin
senseyRegister(lifecycle) {
    tapOnBackPlugin(angleThreshold = 1.5f, tapDebounceMs = 250L, tapSequenceTimeoutMs = 500L) {
        println("Tap on back detected!")
    }
}
```

---

### SoundLevel

| Event | Properties |
|-------|------------|
| `SoundLevelEvent` | `level` — sound level (0–100 scale) |

Requires `RECORD_AUDIO` permission.

```kotlin
senseyRegister(lifecycle) {
    soundLevelPlugin(context) { event ->
        println("Sound level: ${event.level} dB")
    }
}
```

---

### Step

| Event | Properties |
|-------|------------|
| `StepEvent` | `steps`, `distanceInMeters`, `activityType` |

Activity type: `StepDetectorUtil.ACTIVITY_STILL` (0), `ACTIVITY_WALKING` (1), `ACTIVITY_RUNNING` (2).

Uses `StepDetectorPostKitKat` (step counter sensor, API 19+).

```kotlin
senseyRegister(lifecycle) {
    stepPlugin(gender = StepDetectorUtil.MALE) { event ->
        println("Steps: ${event.steps}, Distance: ${event.distanceInMeters}m")
    }
}
```

---

### PinchScale

| Event | Properties |
|-------|------------|
| `PinchScaleEvent` | `scaleFactor`, `isScalingOut` |

Requires `Modifier.senseyGestures()` on the composable that receives touch input.

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

SenseyGestureEffect(lifecycle) {
    pinchScalePlugin(context) { event ->
        if (event.isScalingOut) println("Scaling out: ${event.scaleFactor}")
        else println("Scaling in: ${event.scaleFactor}")
    }
}

    // Apply to the composable that should receive touch input:
    Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
        // content
    }
}
```

---

### TouchType

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

// Apply to the composable that should receive touch input:
Box(modifier = Modifier.fillMaxSize().senseyGestures()) {
    // content
}
```
