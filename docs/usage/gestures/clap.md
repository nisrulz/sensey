---
title: "Clap"
weight: 23
---

# Clap

Detects a clap sound via the microphone. Register with `clapPlugin`.

## Algorithm

Monitors RMS energy (dBFS) from raw PCM samples captured via `AudioRecord` with `VOICE_RECOGNITION` source. A clap is characterized by a sharp rise in energy: the current buffer must exceed the loudness threshold AND be at least the configured rise-dB louder than the previous buffer. This prevents false triggers from sustained sounds like music or speech.

## Events

| Event | Description |
|-------|-------------|
| `ClapEvent.Clapped` | A clap sound was detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `thresholdDb` | Minimum dBFS (decibels relative to full scale) to consider a buffer loud | `-10f` |
| `riseDb` | Minimum difference in dB between consecutive buffers to qualify as a sharp rise | `10f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    clapPlugin(context) { event ->
        println("Clap detected!")
    }
}
```

> Requires `RECORD_AUDIO` permission at runtime. The plugin checks the permission on registration and silently disables itself if not granted.

## Use cases

| Scenario | Description |
|----------|-------------|
| Clap to pause | Pause/resume media playback with a clap |
| Clap to capture | Trigger camera shutter hands-free |
| Clap to toggle | Toggle flashlight or other features |
