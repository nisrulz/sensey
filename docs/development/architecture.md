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

`observe()` returns `Flow<T>` — default is `emptyFlow()`. Override to expose a persistent event stream (e.g. orientation changes, sound level).

## Layer 2: GesturePlugin

Bridge between trigger and Android. Implements `onRegister` / `onUnregister`.

- **Sensor plugins**: write a `GestureTrigger<T>`, wrap in `TypedSensorDetector`, call `sensey.registerSensorDetector()`. Sensey handles `SensorManager`.
- **Compose plugins**: create a `ComposeGestureProvider`, call `sensey.registerComposeGestureProvider()`.

Plugins register via `Sensey.register(plugin)`.

## Layer 3: Sensey (facade)

Plugin registry + lifecycle manager.

- `senseyRegister(lifecycle) {}` — lifecycle-bound DSL
- `SenseyGestureEffect {}` — Compose touch integration
- Auto-cleanup on `ON_DESTROY`
- Manual: `senseyStop()`

## Layer 4: SenseyFlowScope (coroutines)

Flow-based lifecycle-aware plugin collection.

- `context.senseyFlow(lifecycle) {}` — creates a `SenseyFlowScope` that collects plugin flows while lifecycle is `STARTED`.
- `SenseyFlowEffect {}` — Compose wrapper using `DisposableEffect`.
- Internally uses `callbackFlow` to bridge plugin dispatchers into Kotlin `Flow<T>`.

## Data flow

### Callback path

```
GesturePlugin
     ↓ (registers with system)
SensorManager → GestureTrigger<T>
     ↓ (gesture events)
User callback
```

### Flow path

```
GesturePlugin
     ↓ (registers with system)
SensorManager → GestureTrigger<T>
     ↓ (callbackFlow bridge)
Flow<T> → SenseyFlowScope → User collector
```

## Project structure

```
com.github.nisrulz.sensey/
├── Sensey.kt                  # Facade + registerSensorDetector()
├── SenseyExtensions.kt        # senseyRegister() / senseyStop()
├── SenseyPluginRegistry.kt    # Builder DSL
├── SensorDetector.kt          # TypedSensorDetector bridge
├── internal/
│   ├── AudioCapture.kt        # Shared AudioRecord coroutine wrapper
│   ├── EmaSmoother.kt         # Single-pole EMA filter
│   ├── GyroIntegrator.kt      # Gyroscope integration
│   └── Util.kt                # Math helpers (magnitude, angle, etc.)
├── contract/
│   ├── GesturePlugin.kt       # Plugin interface
│   └── GestureTrigger.kt      # Trigger interface (+ observe() flow)
├── flow/
│   └── SenseyFlow.kt          # SenseyFlowScope + senseyFlow()
├── gesture/
│   ├── GesturePlugins.kt      # DSL builder functions
│   ├── compose/               # Compose touch integration (senseyGestures)
│   │   └── SenseyComposeFlow.kt  # SenseyFlowEffect composable
│   ├── audio/clap/            # Clap detection (AudioRecord)
│   ├── shake/                 # Shake detection
│   ├── flip/                  # Flip detection
│   ├── chop/                  # Chop gesture
│   ├── wristtwist/            # Wrist twist gesture
│   ├── wave/                  # Proximity wave gesture
│   ├── scoop/                 # Scoop/lift gesture
│   ├── pickupdevice/          # Pickup / put-down detection
│   ├── orientation/           # Screen orientation detection
│   ├── tiltdirection/         # Gyro tilt axis detection
│   ├── rotationangle/         # Euler angle reporting
│   ├── taponback/             # Back-tap double-tap detection
│   ├── movement/              # Device movement / stationary
│   ├── light/                 # Ambient light transitions
│   ├── proximity/             # Near / far proximity
│   ├── soundlevel/            # Mic-based dB level
│   ├── step/                  # Step counter / activity
│   ├── turnover/              # Gyro-based 180° flip
│   ├── devicespin/            # Rapid spin detection
│   ├── raisetoear/            # Proximity + gravity ear detection
│   ├── pinchscale/            # Compose pinch-to-zoom
│   └── touchtype/             # Compose tap/swipe/scroll
```

Each gesture directory:

- `{Gesture}Event.kt` — sealed interface for events
- `{Gesture}Trigger.kt` — pure detection logic
