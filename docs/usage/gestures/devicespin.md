---
title: "DeviceSpin"
weight: 21
---

# DeviceSpin

Detects a rapid spin of the device on any axis using the gyroscope. Register with `deviceSpinPlugin`.

## How to perform

Place the phone on a flat surface and spin it like a top, or hold it in your hand and spin it rapidly around as if turning a steering wheel.

## Algorithm

Uses a `GyroIntegrator` (shared with [TurnOver](turnover.md)) to track cumulative rotation on all three axes within a configurable time window. When any axis exceeds the angle threshold, the gesture fires.

## Events

| Event | Description |
|-------|-------------|
| `DeviceSpinEvent.Spun` | Device rotated past the angle threshold on any axis |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `angleThreshold` | Cumulative rotation in degrees on any axis that triggers the event | `270f` |
| `timeWindowMs` | Time window in milliseconds within which the rotation must complete | `2000L` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    deviceSpinPlugin(
        angleThreshold = 270f,
        timeWindowMs = 2000L,
    ) { event ->
        println("Device spun!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Spin to shuffle | Randomize playlist or content |
| Spin to reload | Refresh content with a spin gesture |
| Spin to rotate | Cycle through display modes or camera modes |
