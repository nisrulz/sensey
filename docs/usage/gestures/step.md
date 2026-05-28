---
title: "Step"
weight: 17
---

# Step

Tracks step count, distance, and activity type. Register with `stepPlugin`.

## Events

| Event | Properties |
|-------|------------|
| `StepEvent` | `steps`, `distanceInMeters`, `activityType` |

Activity type: `StepDetectorUtil.ACTIVITY_STILL` (0), `ACTIVITY_WALKING` (1), `ACTIVITY_RUNNING` (2).

Uses `StepDetectorPostKitKat` (step counter sensor, API 19+).

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `gender` | `StepDetectorUtil.MALE` or `StepDetectorUtil.FEMALE` | — |

## Usage

```kotlin
senseyRegister(lifecycle) {
    stepPlugin(gender = StepDetectorUtil.MALE) { event ->
        println("Steps: ${event.steps}, Distance: ${event.distanceInMeters}m")
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
