---
title: "Architecture"
weight: 5
---

# Architecture

Three layers: detection logic → plugin bridge → lifecycle manager.

## Layer 1: GestureTrigger&lt;T&gt;

Pure detection algorithm. No Android imports. Takes sensor values, returns events.

```
Sensor data → GestureTrigger<T> → Event
```

Testable on JVM — no device or emulator needed.

## Layer 2: GesturePlugin

Bridge between trigger and Android. Implements `onRegister` / `onUnregister`.

- **Sensor plugins**: write a `GestureTrigger<T>`, wrap in `TypedSensorDetector`, call `sensey.registerSensorDetector()`. Sensey handles `SensorManager`.
- **Compose plugins**: create a `ComposeGestureProvider`, call `sensey.registerComposeGestureProvider()`.

Plugins register via `Sensey.register(plugin)`.

## Layer 3: Sensey (facade)

Plugin registry + lifecycle manager.

- `senseyRegister(lifecycle) {}` — lifecycle-bound DSL
- `SenseyGestureEffect {}` — Compose integration
- Auto-cleanup on `ON_DESTROY`
- Manual: `senseyStop()`

## Data flow

```
GesturePlugin
     ↓ (registers with system)
SensorManager → GestureTrigger<T>
     ↓ (gesture events)
User callback
```

## Project structure

```
com.github.nisrulz.sensey/
├── Sensey.kt                  # Facade + registerSensorDetector()
├── SenseyExtensions.kt        # senseyRegister() / senseyStop()
├── SenseyPluginRegistry.kt    # Builder DSL
├── SensorDetector.kt          # TypedSensorDetector bridge
├── contract/
│   ├── GesturePlugin.kt       # Plugin interface
│   └── GestureTrigger.kt      # Trigger interface
└── gesture/
    ├── GesturePlugins.kt      # DSL builder functions
    ├── shake/                 # Shake detection
    ├── flip/                  # Flip detection
    ├── compose/               # Compose touch integration
    └── ...                    # Other gestures
```

Each gesture directory:

- `{Gesture}Event.kt` — sealed interface for events
- `{Gesture}Trigger.kt` — pure detection logic
