---
title: "Proximity"
weight: 5
---

# Proximity

Detects when an object is near to or far from the proximity sensor. Register with `proximityPlugin`.

## Algorithm

The algorithm compares the raw distance value against the sensor's max range to classify near vs far. It filters out repeated events that match the last dispatched state, guarding against continuous-sensor duplicate firings while remaining compatible with on-change sensors that fire only once per transition.

## Events

| Event | Description |
|-------|-------------|
| `ProximityEvent.Near` | Object is near the device |
| `ProximityEvent.Far` | Object moved away from the device |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `debounceMillis` | Debounce time in milliseconds between proximity events (currently reserved for future use) | `200L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    proximityPlugin(
        debounceMillis = 200L, // debounce between proximity events (default: 200L)
    ) { event ->
        when (event) {
            ProximityEvent.Near -> println("Near") // object close to the sensor
            ProximityEvent.Far  -> println("Far")  // object moved away from the sensor
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
