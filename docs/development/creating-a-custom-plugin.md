---
title: "Creating a Custom Plugin"
weight: 4
---

# Creating a Custom Plugin

Sensey's plugin system lets you define custom gesture detection by implementing
the `GesturePlugin` interface. Plugins are registered via `Sensey.register()`
inside a `senseyRegister {}` or `SenseyGestureEffect {}` block.

## Sensor-Based Plugin

For custom sensor processing, implement `GesturePlugin` and manage the sensor
listener directly using Android's `SensorManager`:

```kotlin
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin

class PressurePlugin(
    private val context: android.content.Context,
    private val onPressure: (Float) -> Unit,
) : GesturePlugin {
    override val key = "PressurePlugin"
    private var listener: SensorEventListener? = null

    override fun onRegister(sensey: Sensey) {
        val sm = context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
        val sensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: return
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                onPressure(event.values[0])
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onUnregister(sensey: Sensey) {
        val sm = context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
        listener?.let { sm.unregisterListener(it) }
        listener = null
    }
}
```

## Compose Touch Plugin

For custom touch gesture handling in Compose, provide a `ComposeGestureProvider`
that gets attached via `Modifier.senseyGestures()`:

```kotlin
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.foundation.gestures.detectTapGestures
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class DoubleTapPlugin(
    private val onDoubleTap: () -> Unit,
) : GesturePlugin {
    override val key = "DoubleTapPlugin"

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(
            ComposeGestureProvider { installGestures() }
        )
    }

    override fun onUnregister(sensey: Sensey) {}

    private suspend fun PointerInputScope.installGestures() {
        detectTapGestures(onDoubleTap = { onDoubleTap() })
    }
}
```
