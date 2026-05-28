---
title: "WristTwist"
weight: 8
---

# WristTwist

Detects a wrist twisting motion. Register with `wristTwistPlugin`.

## Events

| Event | Description |
|-------|-------------|
| `WristTwistEvent.Twisted` | Wrist twist gesture detected |

## Usage

```kotlin
senseyRegister(lifecycle) {
    wristTwistPlugin {
        println("Wrist twist detected!")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Twist to launch | Launch camera with a twist gesture |
| Twist to answer | Answer incoming call with wrist twist |
| Twist to switch | Switch between front and rear camera |
| Twist to reject | Reject call with twist gesture |
