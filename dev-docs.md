# Dev Documentation

## Building

```sh
./gradlew assembleDebug
```

## Code Quality

### ktlint

This project uses [ktlint](https://github.com/pinterest/ktlint) via the
[ktlint-gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle) to enforce
consistent Kotlin code style.

```sh
# Check for violations
./gradlew ktlintCheck

# Auto-format all Kotlin files
./gradlew ktlintFormat
```

Configuration is in `.editorconfig` at the project root.

## Documentation

API reference documentation is generated with
[Dokka 2.2.0](https://kotlinlang.org/docs/dokka-get-started.html) and deployed
to GitHub Pages.

### Generate locally

```sh
./gradlew :sensey:dokkaGeneratePublicationHtml
```

Open `sensey/build/dokka/html/index.html` in a browser.

### Versioned docs (local)

```sh
./gradlew publishDocs
```

Output goes to `build/dokka/{version}/` with a `latest/` symlink.

### CI Deployment

The [docs workflow](.github/workflows/docs.yml) runs on pushes to `master`/
`develop` and version tags (`v*`). It generates docs with the Dokka versioning
plugin, organizes output into versioned directories, and deploys to GitHub
Pages at [nisrulz.github.io/sensey](https://nisrulz.github.io/sensey/latest/index.html).

## Publishing

- To release library to MavenLocal (~/.m2/):

  ```sh
  ./gradlew releaseToMavenLocal
  ```

- To release library
  to [MavenCentral](https://search.maven.org/artifact/com.github.nisrulz/sensey):

  ```sh
  ./gradlew releaseToMavenCentral
  ```

## Build Environment

- AGP 9.2.1
- Gradle 9.5.1
- Kotlin 2.3.21
- Java 21
- compileSdk 36 / minSdk 23 / targetSdk 35
- Configuration cache enabled

## Creating a Custom Plugin

Sensey's plugin system lets you define custom gesture detection by implementing
the `GesturePlugin` interface. Plugins are registered via `Sensey.register()`
inside a `senseyRegister {}` or `SenseyGestureEffect {}` block.

### Basic Plugin (no sensor)

```kotlin
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin

class MyAppPlugin(
    private val dispatcher: (String) -> Unit,
) : GesturePlugin {
    override val key = "MyAppPlugin"

    override fun onRegister(sensey: Sensey) {
        dispatcher("Plugin registered")
    }

    override fun onUnregister(sensey: Sensey) {
        // cleanup
    }
}
```

Register:

```kotlin
senseyRegister(lifecycle) {
    shakePlugin { ... }
    Sensey.register(MyAppPlugin { msg -> println(msg) })
}
```

### Sensor-Based Plugin

For custom sensor processing, implement `GesturePlugin` and manage the sensor
listener directly using Android's `SensorManager`:

```kotlin
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin

class PressurePlugin(
    private val context: android.content.Context,
    private val onPressure: (Float) -> Unit,
) : GesturePlugin {
    override val key = "PressurePlugin"
    private var listener: SensorEventListener? = null

    override fun onRegister(sensey: Sensey) {
        val sm = context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
        val sensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: return
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                onPressure(event.values[0])
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onUnregister(sensey: Sensey) {
        val sm = context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
        listener?.let { sm.unregisterListener(it) }
        listener = null
    }
}
```

### Compose Touch Plugin

For custom touch gesture handling in Compose, provide a `ComposeGestureProvider`
that gets attached via `Modifier.senseyGestures()`:

```kotlin
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.foundation.gestures.detectTapGestures
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class DoubleTapPlugin(
    private val onDoubleTap: () -> Unit,
) : GesturePlugin {
    override val key = "DoubleTapPlugin"

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(
            ComposeGestureProvider { installGestures() }
        )
    }

    override fun onUnregister(sensey: Sensey) {}

    private suspend fun PointerInputScope.installGestures() {
        detectTapGestures(onDoubleTap = { onDoubleTap() })
    }
}
```
