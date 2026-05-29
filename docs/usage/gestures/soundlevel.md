---
title: "SoundLevel"
weight: 16
---

# SoundLevel

Detects the ambient sound level in decibels. Register with `soundLevelPlugin`.

## Algorithm

The algorithm captures raw audio via `AudioRecord` with `VOICE_RECOGNITION` source. It computes the root-mean-square (RMS) of the audio sample buffer normalised by the maximum 16-bit PCM amplitude (32768). The RMS is converted to decibels using the formula 20 × log₁₀(RMS) with a fixed positive offset to produce a human-friendly range (0–100 scale). No audio data is stored, transmitted, or persisted.

## Events

| Event | Properties |
|-------|------------|
| `SoundLevelEvent` | `level` — sound level on a human-friendly 0–100 scale |

## Parameters

This plugin has no configurable parameters.

## Usage

```kotlin
senseyRegister(lifecycle) {
    soundLevelPlugin(context) { event ->
        println("Sound level: ${event.level}") // ambient sound level on 0–100 scale
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
