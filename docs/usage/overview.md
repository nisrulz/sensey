---
title: "Overview"
weight: 3
---

# Overview

Plugins register independently. Use the DSL inside `senseyRegister {}` or `SenseyGestureEffect {}`.

## Single plugin

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

## Multiple plugins

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
Sensey.unregister(plugin)   // one plugin
Sensey.unregisterAll()      // all plugins
senseyStop()                // all + release sensor manager
```

With `senseyRegister(lifecycle)`, cleanup is automatic on `ON_DESTROY`.
