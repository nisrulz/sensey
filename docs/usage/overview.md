---
title: "Overview"
weight: 3
---

# Overview

Sensey uses a **plugin-based architecture**. Each gesture is a `GesturePlugin` that can be registered and unregistered independently.

## Register individual plugins

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

## Register multiple plugins at once

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

## Unregister

```kotlin
// individual
Sensey.unregister(plugin)

// all
Sensey.unregisterAll()

// stop entirely (clears all + releases sensor manager)
senseyStop()
```

With lifecycle-aware `senseyRegister(lifecycle)`, all plugins auto-unregister on `ON_DESTROY`.
