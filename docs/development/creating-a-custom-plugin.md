---
title: "Creating a Custom Plugin"
weight: 4
---

# Creating a Custom Plugin

A plugin wraps custom gesture logic into a reusable unit. Every plugin needs
three things:

1. **Event type** — what your plugin emits (e.g. `Falling` / `Landed`)
2. **Trigger** — the detection algorithm (pure Kotlin, no Android code)
3. **Plugin class** — wires the trigger into Sensey's lifecycle

Plugins are registered via `Sensey.register()` inside a `senseyRegister {}` or
`SenseyGestureEffect {}` block.

## Sensor-Based Plugin

**Step 1**: Write a `GestureTrigger<T>` — it receives raw sensor values and
returns an event when detected.

| Sensor type | values passed to your trigger |
|---|---|
| `TYPE_ACCELEROMETER` | `[x, y, z]` in m/s² |
| `TYPE_GYROSCOPE` | `[x, y, z]` in rad/s |
| `TYPE_GRAVITY` | `[x, y, z]` in m/s² |
| `TYPE_PROXIMITY` | `[distance]` in cm |
| `TYPE_LIGHT` | `[lux]` |
| `TYPE_PRESSURE` | `[hPa]` |
| `TYPE_STEP_COUNTER` | `[steps]` |

**Step 2**: Create a `GesturePlugin` that wraps the trigger in a
`TypedSensorDetector` and registers it. Sensey handles the Android sensor
plumbing — you never touch `SensorManager`.

### Freefall detection

Detects when the device drops (all accelerometer axes near zero) and when it
lands again.

```kotlin
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TypedSensorDetector
import android.hardware.Sensor
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

// 1. Events your plugin will emit
sealed interface FreefallEvent {
    data object Falling : FreefallEvent
    data object Landed : FreefallEvent
}

// 2. Pure detection logic — no Android imports
class FreefallTrigger(
    private val freefallThreshold: Float = 2.0f, // m/s² below this = freefall
) : GestureTrigger<FreefallEvent> {
    private var inFreefall = false // track current state

    // Called by Sensey with each sensor reading
    override fun evaluate(values: FloatArray, timestamp: Long): FreefallEvent? {
        // values = [accelX, accelY, accelZ]
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        return when {
            magnitude < freefallThreshold && !inFreefall -> {
                inFreefall = true
                FreefallEvent.Falling
            }
            magnitude >= freefallThreshold && inFreefall -> {
                inFreefall = false
                FreefallEvent.Landed
            }
            else -> null // no event this frame
        }
    }
}

// 3. Plugin — bridges trigger + sensor type into Sensey
class FreefallPlugin(
    private val dispatcher: (FreefallEvent) -> Unit, // your callback
) : GesturePlugin {
    override val key = "FreefallPlugin" // unique id
    private var detector: TypedSensorDetector<FreefallEvent>? = null

    // Called by Sensey when plugin is registered
    override fun onRegister(sensey: Sensey) {
        val d = TypedSensorDetector(
            FreefallTrigger(),    // your trigger
            dispatcher,           // your callback
            Sensor.TYPE_ACCELEROMETER, // which sensor to subscribe to
        )
        detector = d // keep reference for cleanup
        sensey.registerSensorDetector(d) // Sensey starts listening
    }

    // Called by Sensey when plugin is unregistered
    override fun onUnregister(sensey: Sensey) {
        detector?.let { sensey.unregisterSensorDetector(it) } // Sensey stops listening
        detector = null
    }
}
```

### Flat-on-table detection

Detects when the phone is placed face-up on a table and when it's picked up.

```kotlin
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TypedSensorDetector
import android.hardware.Sensor
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.contract.GestureTrigger

sealed interface FlatEvent {
    data object OnTable : FlatEvent
    data object PickedUp : FlatEvent
}

class FlatOnTableTrigger(
    private val gravityThreshold: Float = 8.5f, // Z > this = flat on table
) : GestureTrigger<FlatEvent> {
    private var wasFlat = false

    override fun evaluate(values: FloatArray, timestamp: Long): FlatEvent? {
        // values = [gravityX, gravityY, gravityZ]
        val z = values[2]
        val isFlat = z > gravityThreshold // Z points up when flat
        return when {
            isFlat && !wasFlat -> {
                wasFlat = true
                FlatEvent.OnTable
            }
            !isFlat && wasFlat -> {
                wasFlat = false
                FlatEvent.PickedUp
            }
            else -> null
        }
    }
}

class FlatOnTablePlugin(
    private val dispatcher: (FlatEvent) -> Unit,
) : GesturePlugin {
    override val key = "FlatOnTablePlugin"
    private var detector: TypedSensorDetector<FlatEvent>? = null

    override fun onRegister(sensey: Sensey) {
        val d = TypedSensorDetector(
            FlatOnTableTrigger(),
            dispatcher,
            Sensor.TYPE_GRAVITY, // subscribe to gravity sensor
        )
        detector = d
        sensey.registerSensorDetector(d)
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.let { sensey.unregisterSensorDetector(it) }
        detector = null
    }
}
```

### Multi-sensor — device motion classifier

Combines accelerometer + gyroscope to classify movement. Pass both sensor
types to `TypedSensorDetector` — values arrive as one flat array:
`[accelX, accelY, accelZ, gyroX, gyroY, gyroZ]`.

```kotlin
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TypedSensorDetector
import android.hardware.Sensor
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

sealed interface MotionClass {
    data object Still : MotionClass
    data object Walking : MotionClass
    data object Driving : MotionClass
}

class MotionClassifierTrigger(
    private val stillThreshold: Float = 0.5f,
    private val drivingThreshold: Float = 5.0f,
) : GestureTrigger<MotionClass> {
    private var currentClass: MotionClass = MotionClass.Still

    override fun evaluate(values: FloatArray, timestamp: Long): MotionClass? {
        // values = [accelX, accelY, accelZ, gyroX, gyroY, gyroZ]
        val accelMag = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        val gyroMag = sqrt(values[3] * values[3] + values[4] * values[4] + values[5] * values[5])
        val newClass = when {
            accelMag < stillThreshold && gyroMag < stillThreshold -> MotionClass.Still
            accelMag > drivingThreshold || gyroMag > 2.0f -> MotionClass.Driving
            else -> MotionClass.Walking
        }
        return if (newClass != currentClass) {
            currentClass = newClass
            newClass
        } else null
    }
}

class MotionClassifierPlugin(
    private val dispatcher: (MotionClass) -> Unit,
) : GesturePlugin {
    override val key = "MotionClassifierPlugin"
    private var detector: TypedSensorDetector<MotionClass>? = null

    override fun onRegister(sensey: Sensey) {
        val d = TypedSensorDetector(
            MotionClassifierTrigger(),
            dispatcher,
            Sensor.TYPE_ACCELEROMETER, // values[0..2]
            Sensor.TYPE_GYROSCOPE,     // values[3..5]
        )
        detector = d
        sensey.registerSensorDetector(d)
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.let { sensey.unregisterSensorDetector(it) }
        detector = null
    }
}
```

## Compose Touch Plugin

For touch gestures, create a `ComposeGestureProvider` and register it on the
`Sensey` instance. The provider's `installGestures()` runs inside a Compose
`PointerInputScope` — use any Compose gesture detector API.

### Long press with position

Reports where on the screen the long press happened.

```kotlin
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class LongPressPlugin(
    private val onLongPress: (Offset) -> Unit, // receives X, Y position
) : GesturePlugin {
    override val key = "LongPressPlugin"

    override fun onRegister(sensey: Sensey) {
        // Register a touch provider — runs inside Compose's pointer system
        sensey.registerComposeGestureProvider(
            ComposeGestureProvider { installGestures() }
        )
    }

    override fun onUnregister(sensey: Sensey) {}

    // Called by Compose when pointer input is available
    private suspend fun PointerInputScope.installGestures() {
        detectTapGestures(onLongPress = { onLongPress(it) })
    }
}
```

### Fling detection with direction

Detects quick drags (flings) and reports direction + velocity.

```kotlin
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerInputChange
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider
import kotlin.math.abs

// Data returned by the plugin
data class FlingInfo(
    val direction: String, // "up" | "down" | "left" | "right"
    val velocity: Offset,  // px/s on each axis
)

class FlingPlugin(
    private val velocityThreshold: Float = 200f, // px/s to count as a fling
    private val onFling: (FlingInfo) -> Unit,
) : GesturePlugin {
    override val key = "FlingPlugin"
    private var dragStart = Offset.Zero // where the finger first touched

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(
            ComposeGestureProvider { installGestures() }
        )
    }

    override fun onUnregister(sensey: Sensey) {}

    private suspend fun PointerInputScope.installGestures() {
        detectDragGestures(
            onDragStart = { dragStart = it }, // remember start position
            onDragEnd = { /* fling finished */ },
            onDragCancel = { dragStart = Offset.Zero },
        ) { change: PointerInputChange, dragAmount: Offset ->
            change.consume()
            val totalX = change.position.x - dragStart.x
            val totalY = change.position.y - dragStart.y
            val velocity = dragAmount / 0.016f // per-frame delta → px/s

            if (abs(dragAmount.x) > velocityThreshold ||
                abs(dragAmount.y) > velocityThreshold
            ) {
                val direction = when {
                    abs(totalX) > abs(totalY) ->
                        if (totalX > 0) "right" else "left"
                    else ->
                        if (totalY > 0) "down" else "up"
                }
                onFling(FlingInfo(direction, velocity))
            }
        }
    }
}
```

### Multi-touch — two-finger chord

Detects when two fingers press down and lift simultaneously. Uses Compose's
low-level `awaitEachGesture` instead of the higher-level `detect*` helpers.

```kotlin
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class TwoFingerChordPlugin(
    private val onChord: () -> Unit,
) : GesturePlugin {
    override val key = "TwoFingerChordPlugin"

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(
            ComposeGestureProvider { installGestures() }
        )
    }

    override fun onUnregister(sensey: Sensey) {}

    private suspend fun PointerInputScope.installGestures() {
        awaitEachGesture {
            val firstDown = awaitFirstDown() // wait for first finger
            var pointerCount = 1

            // Track all pointers until none remain
            do {
                val event = awaitPointerEvent(PointerEventPass.Main)
                val pressed = event.changes.filter { it.pressed }
                pointerCount = pressed.size
                pressed.forEach { it.consume() }
            } while (pointerCount > 0)

            // All fingers lifted — check if it was a two-finger gesture
            if (firstDown.pressed) {
                onChord()
            }
        }
    }
}
```
