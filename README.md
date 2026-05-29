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

## Gestures

Shake, Flip, Light, Proximity, Movement, Orientation, Chop, TapOnBack, WristTwist, Wave, Scoop, PickupDevice, TiltDirection, RotationAngle, PinchScale, TouchType, SoundLevel, Step.

See the **[usage guide](docs/usage/)** for events, parameters, and examples.

## Checkout

- 📖 [Full usage guide](docs/usage/)
- 💡 [Overview](docs/usage/overview.md)
- 🔧 [Context-specific usage](docs/usage/context-specific-usage.md) (Compose, Service, WorkManager)
- 📐 [Architecture](docs/development/architecture.md)
- 🛠 [Development guide](docs/development/)
- 📜 [Changelog](https://github.com/nisrulz/sensey/releases)

## Including in your project

```gradle
implementation 'com.github.nisrulz:sensey:{latest version}'
```

*Latest version on [Maven Central](https://search.maven.org/artifact/com.github.nisrulz/sensey).*

## License

Licensed under the [Apache License, Version 2.0](LICENSE.txt).

Copyright 2016 Nishant Srivastava
