---
title: "Setup"
weight: 2
---

# Setup

## Add the dependency

Latest version on [Maven Central](https://search.maven.org/artifact/com.github.nisrulz/sensey).

### Direct dependency

```kotlin
// build.gradle.kts (module)
implementation("com.github.nisrulz:sensey:{latest-version}")
```

### Via version catalog

```toml
# gradle/libs.versions.toml
[versions]
sensey = "{latest-version}"

[libraries]
sensey = { module = "com.github.nisrulz:sensey", version.ref = "sensey" }
```

```kotlin
// build.gradle.kts (module)
implementation(libs.sensey)
```

## Initialize

### Activity

```kotlin
import com.github.nisrulz.sensey.senseyRegister

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    senseyRegister(lifecycle) {
        // register sensor plugins here
    }
}
// Auto-stops on ON_DESTROY
```

### Compose

```kotlin
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
fun MyScreen(lifecycle: Lifecycle) {
    SenseyGestureEffect(lifecycle) {
        // register sensor and touch plugins here
    }
    Box(modifier = Modifier.fillMaxSize().senseyGestures())
}
```

### Service or any Context

```kotlin
import com.github.nisrulz.sensey.senseyRegister

context.senseyRegister {
    // register plugins here
}
```

## Stop

Stop all plugins and release the sensor manager:

```kotlin
import com.github.nisrulz.sensey.senseyStop

senseyStop()
```

Lifecycle-aware registration (`senseyRegister(lifecycle)`) and `SenseyGestureEffect` handle this automatically on destroy. Manual stop is only needed when using `context.senseyRegister {}` without a lifecycle.
