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

### Compose

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        shakePlugin { /* handle events */ }
    }
    // Attach touch gestures to your composable
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
