---
title: "Setup"
weight: 2
---

# Setup

## Add dependency

Latest version on [Maven Central](https://search.maven.org/artifact/com.github.nisrulz/sensey).

```kotlin
// build.gradle.kts
implementation("com.github.nisrulz:sensey:{latest-version}")
```

Or via version catalog:

```toml
# gradle/libs.versions.toml
[libraries]
sensey = { module = "com.github.nisrulz:sensey", version.ref = "sensey" }
```

## Required dependencies

Sensey declares all its dependencies as `compileOnly` — they are **not** bundled
transitively into your app. You most likely already have them set up in your app:

- **Lifecycle** — needed for lifecycle-aware auto-registration.
  Most Android apps using Jetpack already include it.
  ```kotlin
  implementation("androidx.lifecycle:lifecycle-common:2.8.7")
  ```
- **Coroutines** — needed for `SenseyFlow` / Flow-based API.
  Standard in nearly all modern Android projects.
  ```kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.0")
  ```
- **Compose** — needed for touch-based gesture plugins (pinch, swipe, touch type, edge swipe).
  Only required if you use those plugins.
  ```kotlin
  implementation(platform("androidx.compose:compose-bom:2026.05.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.foundation:foundation")
  ```

## Initialize

### Activity (auto lifecycle)

```kotlin
import com.github.nisrulz.sensey.senseyRegister

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    senseyRegister(lifecycle) {
        shakePlugin { /* handle events */ }
    }
    // Auto-stops on ON_DESTROY
}
```

### Compose — Eager initialization

Initialize before `setContent` so `senseyGestures()` picks up the Sensey instance:

```kotlin
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senseyRegister { /* register plugins */ }
        setContent {
            Box(modifier = Modifier.fillMaxSize().senseyGestures())
        }
    }
}
```

### Compose — Lifecycle-bound

Use `SenseyGestureEffect` inside the composable tree for lifecycle-aware setup:

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        shakePlugin { /* handle events */ }
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

### Any Context (manual lifecycle)

```kotlin
import com.github.nisrulz.sensey.senseyRegister

context.senseyRegister {
    shakePlugin { /* handle events */ }
}
```

## Stop

```kotlin
import com.github.nisrulz.sensey.senseyStop

senseyStop() // releases sensor manager
```

Lifecycle-aware `senseyRegister(lifecycle)` and `SenseyGestureEffect` auto-stop on destroy. Manual stop only needed with `context.senseyRegister {}`.
