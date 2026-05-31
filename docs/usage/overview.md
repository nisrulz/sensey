---
title: "Overview"
weight: 3
---

# Overview

Plugins register independently. Use the DSL inside `senseyRegister {}` or `senseyFlow {}`.

## Single plugin

```kotlin
senseyRegister(lifecycle) {
    shakePlugin { event ->
        when (event) {
            ShakeEvent.Detected -> // handle
            ShakeEvent.Stopped  -> // handle
        }
    }
}
```

## Flow-based collection

```kotlin
context.senseyFlow(lifecycle) {
    shakePlugin { event ->
        when (event) {
            ShakeEvent.Detected -> // handle
            ShakeEvent.Stopped  -> // handle
        }
    }
}
```

See [Coroutines / Flow](coroutines-flow.md) for details.

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
sensey.unregister(plugin)   // one plugin (Sensey instance)
sensey.unregisterAll()      // all plugins
senseyStop()                // all + release sensor manager
```

With `senseyRegister(lifecycle)`, cleanup is automatic on `ON_DESTROY`.
With `senseyFlow`, collection pauses on `STOP` and cleans up on `DESTROY`.
