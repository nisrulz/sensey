---
title: "Context-specific usage"
weight: 5
---

# Context-specific usage

## Activity

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

## Service

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

## WorkManager Worker

> **Note:** Android 8+ background execution limits prevent sensor delivery to background workers.
> Use a [ForegroundService](#foreground-service) instead. The `applicationContext` extension works for the rare
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

## Foreground Service

For reliable sensor delivery on Android 8+, use a `ForegroundService` with a visible notification. This keeps the process alive and allows sensor events to be delivered consistently.

```kotlin
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

class SensorForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        senseyRegister {
            shakePlugin { event ->
                // handle shake
            }
            lightPlugin { event ->
                // handle light changes
            }
        }
    }

    override fun onDestroy() {
        senseyStop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sensor Detection")
            .setContentText("Monitoring sensors in the background")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "sensor_service"
    }
}
```

## Jetpack Compose

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

