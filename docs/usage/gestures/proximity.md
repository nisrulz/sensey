---
title: "Proximity"
weight: 5
---

# Proximity

Detects when an object is near or far from the proximity sensor. Register with `proximityPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `ProximityEvent.Near` | Object is near the device |
| `ProximityEvent.Far` | Object moved away |

## Usage

```kotlin
senseyRegister(lifecycle) {
    proximityPlugin { event ->
        when (event) {
            ProximityEvent.Near -> println("Near")
            ProximityEvent.Far  -> println("Far")
        }
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Pocket mode | Disable touch input when device is in pocket |
| Call handling | Turn screen off during call when held to ear |
| Wave gestures | Detect hand wave for contactless interaction |
| Cover to sleep | Turn off screen when covered |
