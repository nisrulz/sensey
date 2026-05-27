# Gesture Detection Details

This document explains how each gesture trigger works under the hood — the algorithms, math, and state machines used to detect gestures from raw sensor data.

---

## TouchType

**Sensor:** Touch events → computed `[deltaX, deltaY, velocityX, velocityY]`

### N-Tap Detection

Multi-tap detection is abstracted into a single `detectMultiTap()` function triggered on each `ACTION_UP`:

1. On each `ACTION_UP`, record the timestamp.
2. If `now - lastTapTime ≤ multiTapTimeWindow` (default `400ms`), increment the counter; otherwise reset to `1`.
3. When the counter reaches `multiTapCount` (default `3`), emit `TouchTypeEvent.NTap(multiTapCount)` and reset.

This is independent of Android's `GestureDetector` — it works alongside `onSingleTapConfirmed` / `onDoubleTap` without interference.

### Swipe / Scroll

See [TouchTypeTrigger source](src/main/kotlin/com/github/nisrulz/sensey/gesture/touchtype/TouchTypeTrigger.kt) for direction angle mapping and velocity classification.

### Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `multiTapCount` | `3` | Number of consecutive taps to trigger `NTap` |
| `multiTapTimeWindow` | `400L` | Max interval (ms) between taps to count as consecutive |
