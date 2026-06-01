---
title: "Context-specific usage"
weight: 5
---

# Context-specific usage

## Activity (auto lifecycle)

```kotlin
import com.github.nisrulz.sensey.senseyRegister

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senseyRegister(lifecycle) {
            shakePlugin { /* handle */ }
        }
        // Auto-cleanup on ON_DESTROY
    }
}
```

## Service (manual lifecycle)

```kotlin
class SensorService : Service() {
    override fun onCreate() {
        super.onCreate()
        senseyRegister {
            shakePlugin { /* handle */ }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        senseyStop() // must call manually
    }
}
```

## WorkManager Worker

> Android 8+ limits background work. Use a ForegroundService instead.
> `applicationContext.senseyRegister {}` works only when sensor data is available.

```kotlin
class SensorWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        applicationContext.senseyRegister {
            shakePlugin { /* handle */ }
        }
        return Result.success()
    }
}
```

## Foreground Service

Keeps the process alive for reliable sensor delivery.

```kotlin
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

class SensorForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        senseyRegister {
            shakePlugin { /* handle */ }
            lightPlugin { /* handle */ }
        }
    }

    override fun onDestroy() {
        senseyStop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sensor Detection")
            .setContentText("Monitoring sensors")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "sensor_service"
    }
}
```

## Jetpack Compose

### Gesture effect (touch-based)

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        shakePlugin { event ->
            println(if (event is ShakeEvent.Detected) "Shake!" else "Stopped")
        }
        touchPlugin(context) { event ->
            when (event) {
                is TouchEvent.Tap.Single -> println("Tap")
                is TouchEvent.Swipe      -> println("Swipe ${event.direction}")
                is TouchEvent.Tap.NTap   -> println("${event.count}-tap")
                else -> {}
            }
        }
    }
    // Attach touch input to a composable
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

### Flow-based (sensor-based)

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyFlowEffect

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyFlowEffect(lifecycle) {
        shakePlugin { event ->
            println(if (event is ShakeEvent.Detected) "Shake!" else "Stopped")
        }
        flipPlugin { event ->
            println(if (event is FlipEvent.FaceUp) "Face up" else "Face down")
        }
    }
}
```

`SenseyFlowEffect` wraps `senseyFlow` in a `DisposableEffect` — collection starts on `START`, stops on `STOP`, cleans up on dispose.

