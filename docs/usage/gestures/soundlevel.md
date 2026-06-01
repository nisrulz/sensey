---
title: "SoundLevel"
weight: 16
---

# SoundLevel

Measures the ambient sound level in decibels. Register with `soundLevelPlugin`.

## How to perform

Make noise near the phone's microphone to see the sound level change, or stay quiet to observe the ambient noise floor.

## Algorithm

A stateless, per‑buffer computation on raw 16‑bit PCM audio captured via `AudioRecord` with `VOICE_RECOGNITION` source:

1. **RMS power** — the root‑mean‑square of the buffer is computed and normalised by the maximum 16‑bit signed amplitude (32768), yielding a value in [0, 1].

2. **dB conversion** — the normalised RMS is converted with `20 × log₁₀(rms)`. A configurable **offset** (default +100 dB) shifts the raw dBFS range (~−90 dBFS to 0 dBFS) into a human‑friendly 0–100 scale.

The result is emitted immediately via `SoundLevelEvent` and discarded. No audio data is stored, transmitted, or persisted.

## Events

| Event | Properties |
|-------|------------|
| `SoundLevelEvent` | `level` — sound level on a human‑friendly 0–100 scale |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `offset` | Positive shift applied to raw dBFS to produce a 0–100 scale | `100f` |

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
| Party mode | Auto‑enable louder ringer in noisy environments |
| Noise pollution | Track ambient noise levels over time |

## Interpreting the level

| Raw dBFS | With offset (+100) | Typical environment |
|----------|---------------------|---------------------|
| −80 dBFS | ≈ 20 | Quiet room, library |
| −50 dBFS | ≈ 50 | Normal conversation |
| −30 dBFS | ≈ 70 | Loud music, traffic |
| −10 dBFS | ≈ 90 | Close shouting, clap |
| 0 dBFS   | 100 | Full scale (very rare) |
