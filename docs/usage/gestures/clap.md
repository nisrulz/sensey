---
title: "Clap"
weight: 23
---

# Clap

Detects hand claps via the microphone. Register with `clapPlugin`.

## How to perform

Clap your hands anywhere within earshot of the phone. The gesture requires a configured number of claps (default: two) within a rolling time window. Works from across a room — the adaptive noise floor and sensitive threshold allow detection without clapping directly at the microphone.

## Algorithm

The detection runs a pipeline on each 16-bit PCM buffer from `AudioRecord` (`VOICE_RECOGNITION` source):

1. **RMS power** — the buffer's root‑mean‑square energy is computed and converted to dBFS.

2. **ZCR‑weighted RMS** — the RMS is scaled by the buffer's zero‑crossing rate. Narrow‑band sounds (voiced speech, tonal music) with ZCR below `minZcr` are quadratically penalised, rejecting many false triggers while broadband transients (claps) pass at full energy.

3. **Adaptive noise floor** — a fast‑attack / slow‑release EMA tracks the ambient level. In noisy environments the effective threshold rises automatically, maintaining immunity without manual tuning.

4. **Multi‑clap counting** — distinct claps (separated by ≥200 ms) are counted within a rolling `clapTimeframeMs` window. The event fires only when `requiredClaps` claps accumulate in that window. The default of 2 prevents single loud noises (door slams, coughs) from triggering while reliably detecting intentional double‑claps.

5. **Debounce** — after a clap event fires, subsequent triggers are suppressed for `debounceMs`.

## Events

| Event | Description |
|-------|-------------|
| `ClapEvent.Clapped` | Required number of claps was detected |

## Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `thresholdDb` | Absolute minimum dBFS a buffer must reach | `-45f` |
| `requiredClaps` | Number of distinct claps needed to fire the event | `2` |
| `clapTimeframeMs` | Rolling time window for multi‑clap counting (ms) | `800L` |
| `debounceMs` | Cooldown after the event fires (ms) | `500L` |
| `minZcr` | Minimum zero‑crossing rate (0–1); sounds below this get penalised | `0.10f` |
| `noiseFloorAttackAlpha` | EMA coefficient when ambient rises (fast) | `0.1f` |
| `noiseFloorReleaseAlpha` | EMA coefficient when ambient falls (slow) | `0.005f` |
| `minMarginDb` | Minimum dB the buffer must exceed the adaptive noise floor | `1f` |

## Usage

```kotlin
senseyRegister(lifecycle) {
    // Default: double‑clap required for triggering
    clapPlugin(context) { event ->
        println("Double clap detected!")
    }
}

// Single clap:
clapPlugin(context, requiredClaps = 1) { event ->
    println("Clap detected!")
}

// Triple clap within 1.5 seconds:
clapPlugin(context, requiredClaps = 3, clapTimeframeMs = 1500) { event ->
    println("Triple clap detected!")
}
```

> Requires `RECORD_AUDIO` permission at runtime. The plugin checks the permission on registration and silently disables itself if not granted.

## Use cases

| Scenario | Description |
|----------|-------------|
| Clap to pause | Pause/resume media playback with a double‑clap |
| Clap to capture | Trigger camera shutter hands‑free |
| Clap to toggle | Toggle flashlight or other features |

## Tuning tips

- **Too sensitive (false triggers):** raise `thresholdDb`, `requiredClaps`, or `minZcr`.
- **Not sensitive enough (missed claps):** lower `thresholdDb` or `requiredClaps`.
- **Double‑triggers on a single clap:** increase `debounceMs`.
- **Two claps too close together:** increase `clapTimeframeMs`.
- **Two claps too far apart:** decrease `clapTimeframeMs`.
- **Noisy environment:** the adaptive noise floor handles this automatically; tune `noiseFloorAttackAlpha` (how fast it adapts up) or `noiseFloorReleaseAlpha` (how fast it recovers).
