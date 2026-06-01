![Banner](img/github_banner.png)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.nisrulz/sensey)](https://search.maven.org/artifact/com.github.nisrulz/sensey)
[![GitHub stars](https://img.shields.io/github/stars/nisrulz/sensey.svg?style=social&label=Star)](https://github.com/nisrulz/sensey)
[![GitHub forks](https://img.shields.io/github/forks/nisrulz/sensey.svg?style=social&label=Fork)](https://github.com/nisrulz/sensey/fork)

[![API 23+](https://img.shields.io/badge/API-23%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=23)

Android library that makes sensor event and gesture detection a breeze. Eliminates boilerplate for setting up sensor-based gesture detection.

## Quick Start

```kt
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senseyRegister(lifecycle) {
            shakePlugin { event ->
                when (event) {
                    ShakeEvent.Detected -> println("Shake detected!")
                    ShakeEvent.Stopped  -> println("Shake stopped")
                }
            }
            flipPlugin { event ->
                when (event) {
                    FlipEvent.FaceUp   -> println("Face up")
                    FlipEvent.FaceDown -> println("Face down")
                }
            }
        }
    }
}
```

## Coroutines / Flow

Events as `Flow<T>` with lifecycle-aware collection:

```kt
import com.github.nisrulz.sensey.flow.senseyFlow

context.senseyFlow(lifecycle) {
    shakePlugin { event ->
        when (event) {
            ShakeEvent.Detected -> println("Shake detected!")
            ShakeEvent.Stopped  -> println("Shake stopped")
        }
    }
}
```

Auto-collects on `STARTED`, pauses on `STOP`, cleans up on `DESTROY`. All gestures support it.

```kt
// Compose
SenseyFlowEffect(lifecycle) {
    shakePlugin { /* events */ }
}
```

See the **[coroutines/flow guide](docs/usage/coroutines-flow.md)** for details.

## Gestures

Shake, Flip, Light, Proximity, Movement, Orientation, Chop, TapOnBack, WristTwist, Wave, Scoop, PickupDevice, TiltDirection, RotationAngle, PinchScale, TouchType, EdgeSwipe, DiagonalSwipe, SoundLevel, Step, TurnOver, DeviceSpin, RaiseToEar, Clap, NodGesture, HeadShake.

See the **[usage guide](docs/usage/)** for events, parameters, and examples.

## Checkout

- 📖 [Full usage guide](docs/usage/)
- 💡 [Overview](docs/usage/overview.md)
- 🌊 [Coroutines / Flow](docs/usage/coroutines-flow.md)
- 🔧 [Context-specific usage](docs/usage/context-specific-usage.md) (Compose, Service, WorkManager)
- 📐 [Architecture](docs/development/architecture.md)
- 🛠 [Development guide](docs/development/)
- 📜 [Changelog](https://github.com/nisrulz/sensey/releases)

## Including in your project

```gradle
implementation 'com.github.nisrulz:sensey:{latest version}'
```

*Latest version on [Maven Central](https://search.maven.org/artifact/com.github.nisrulz/sensey).*

Sensey declares its dependencies as `compileOnly`. Depending on which features you use,
add the required deps:

| Feature | Required dependency |
|---|---|
| Lifecycle-aware registration | `androidx.lifecycle:lifecycle-common` |
| Flow-based API (SenseyFlow) | `kotlinx-coroutines-core` |
| Touch gesture plugins | `androidx.compose.ui`, `androidx.compose.foundation` |

Sensor-only plugins (shake, flip, light, proximity, etc.) work with just `sensey` alone.

## License

Licensed under the [Apache License, Version 2.0](LICENSE.txt).

Copyright 2016 Nishant Srivastava
