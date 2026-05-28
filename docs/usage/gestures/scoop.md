---
title: "Scoop"
weight: 10
---

# Scoop

Detects a scooping motion gesture. Register with `scoopPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `ScoopEvent.Scooped` | Scoop gesture detected |

## Usage

```kotlin
senseyRegister(lifecycle) {
    scoopPlugin {
        println("Scoop detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Scoop to pick up | Turn on screen when picking up device |
| Scoop to notify | Show notifications on scoop gesture |
| Scoop to preview | Quick glance preview on lift |
| Scoop to unlock | Trigger face unlock on pickup |
| Scoop to resume | Resume media playback on pickup |
| Scoop to glance | Show time and weather on lift |
