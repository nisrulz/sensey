---
title: "Step"
weight: 17
---

# Step

Tracks step count, distance, and activity type. Register with `stepPlugin`.

## Algorithm

The algorithm supports two input modes: (1) hardware step-counter sensor (single cumulative value) — subtracts the initial baseline and dispatches the incremental step count, deduplicating against the last dispatched value; (2) accelerometer fallback (3-axis values) — detects steps by monitoring acceleration magnitude peaks above a threshold. Each event includes distance (via stride estimation) and activity type (still/walking/running) computed through `StepDetectorUtil`.

## Events

| Event | Properties |
|-------|------------|
| `StepEvent` | `steps`, `distanceInMeters`, `activityType` |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `gender` | Gender used for stride length estimation in distance calculation; `StepDetectorUtil.MALE` or `StepDetectorUtil.FEMALE` | `StepDetectorUtil.MALE` |
| `threshold` | Acceleration magnitude delta threshold for step detection in accelerometer fallback mode | `3f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    stepPlugin(
        gender = StepDetectorUtil.MALE, // adjusts stride estimation for distance (default: MALE)
        threshold = 3f,                 // accel delta threshold for step detection (default: 3f)
    ) { event ->
        println("Steps: ${event.steps}, Distance: ${event.distanceInMeters}m, Activity: ${event.activityType}")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Fitness tracker | Track daily steps and distance |
| Activity recognition | Detect walking vs running vs stationary |
| Calorie estimation | Estimate calories burned based on steps |
| Gamification | Reward users for reaching step goals |
| Walking navigation | Auto-advance navigation instructions on steps |
| Step challenges | Compete with friends on step counts |
| Health insights | Provide weekly step summaries and trends |
| Movement analysis | Analyze walking patterns and stride length |
