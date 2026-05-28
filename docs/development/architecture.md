---
title: "Architecture"
weight: 5
---

# Architecture

Sensey is built on a three-layer architecture that separates detection logic, plugin registration, and lifecycle management.

## Layer 1: GestureTrigger&lt;T&gt;

The **detection algorithm**. A pure Kotlin contract with zero Android dependencies. Each gesture defines a trigger that accepts sensor data and emits typed events.

```
Sensor data → GestureTrigger<T> → Event
```

Triggers receive raw sensor values and determine when a gesture has occurred. Because they have no Android dependencies, triggers can be unit tested on the JVM without a device or emulator.

## Layer 2: GesturePlugin

The **bridge** between a trigger and the Android sensor system. Each plugin wraps a trigger and implements `GesturePlugin`, which provides `onRegister` and `onUnregister` lifecycle callbacks.

- Sensor-based plugins use `TypedSensorDetector` to register with `SensorManager` and feed data into the trigger
- Compose touch plugins provide a `ComposeGestureProvider` that attaches via `Modifier.senseyGestures()`
- Plugins are registered with `Sensey.register(plugin)`

This layer handles all Android framework interactions — sensor listeners, context, permissions — keeping the trigger layer pure.

## Layer 3: Sensey (facade)

The **plugin registry and lifecycle manager**. The `Sensey` class:

- Maintains a registry of all active plugins
- Provides `senseyRegister(lifecycle) {}` DSL for lifecycle-bound registration
- Provides `SenseyGestureEffect {}` for Compose integration
- Auto-cleans all plugins on lifecycle destroy (via `ON_DESTROY`)
- Supports manual lifecycle via `senseyStop()`

## Data flow

```
SensorManager
     ↓ (raw sensor data)
TypedSensorDetector
     ↓ (filtered values)
GestureTrigger<T>
     ↓ (gesture events)
GesturePlugin → user callback
     ↓
Sensey (registry + lifecycle)
```

## Project structure

```
com.github.nisrulz.sensey/
├── Sensey.kt                  # Facade / registry
├── SenseyExtensions.kt        # Extension functions (senseyRegister)
├── SenseyPluginRegistry.kt    # Plugin lifecycle management
├── SensorDetector.kt          # Base sensor detector
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

Each gesture directory follows the same pattern:

- `{Gesture}Event.kt` — Event sealed class
- `{Gesture}Trigger.kt` — Pure detection algorithm
