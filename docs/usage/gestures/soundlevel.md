---
title: "SoundLevel"
weight: 16
---

# SoundLevel

Detects the ambient sound level. Register with `soundLevelPlugin`.

## Events

| Event | Properties |
|-------|------------|
| `SoundLevelEvent` | `level` — sound level (0–100 scale) |

Requires `RECORD_AUDIO` permission.

## Usage

```kotlin
senseyRegister(lifecycle) {
    soundLevelPlugin(context) { event ->
        println("Sound level: ${event.level} dB")
    }
}
```

## Use cases

| Scenario | Description |
|----------|-------------|
| Clap detection | Trigger action on loud clap |
| Noise alert | Alert when ambient noise exceeds threshold |
| Smart home | Control devices with sound level triggers |
| Whisper mode | Detect quiet environment for silent mode |
| Party mode | Auto-enable louder ringer in noisy environments |
| Noise pollution | Track ambient noise levels over time |
