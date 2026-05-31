---
title: "Coroutines / Flow"
weight: 4
---

# Coroutines / Flow

Flow-based gesture event collection using `senseyFlow` — a coroutine-friendly DSL that auto-collects events within a `Lifecycle`.

## senseyFlow

```kotlin
import com.github.nisrulz.sensey.flow.senseyFlow

context.senseyFlow(lifecycle) {
    shakePlugin { event ->
        when (event) {
            ShakeEvent.Detected -> /* react */
            ShakeEvent.Stopped  -> /* react */
        }
    }
}
```

### How it differs from senseyRegister

| Feature | senseyRegister | senseyFlow |
|---------|---------------|------------|
| Underlying mechanism | Callback (dispatcher lambda) | `callbackFlow` bridge |
| Lifecycle collection | Registers/unregisters on lifecycle | Collects on `STARTED`, cancels on `STOPPED` |
| Reactive pipelines | Manual | Composable with other flows via `Flow` operators |
| Back-pressure | None | Built into `callbackFlow` |

### Multiple plugins

```kotlin
context.senseyFlow(lifecycle) {
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

### Lifecycle behavior

- Events are **only collected** when lifecycle is at least `STARTED`.
- Collection **pauses** on `STOP` and **resumes** on `START`.
- Full cleanup on `DESTROY` — all plugins unregistered, sensor manager released.
- Events emitted while lifecycle is below `STARTED` are silently dropped.

## Compose — SenseyFlowEffect

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyFlowEffect

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyFlowEffect(lifecycle) {
        shakePlugin { event ->
            println(if (event is ShakeEvent.Detected) "Shake!" else "Stopped")
        }
    }
}
```

Automatically stops on `DisposableEffect` dispose.

## Stop

```kotlin
val scope = context.senseyFlow(lifecycle) { /* plugins */ }
// later:
scope.stop()
```

Lifecycle-aware setup auto-stops on `ON_DESTROY`. Call `stop()` explicitly to release early.
