
package com.github.nisrulz.sensey.gesture.wave

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects a hand wave over the proximity sensor.
 *
 * Algorithm: Tracks near→far state transitions of the proximity sensor. A
 * wave is recognised when the device transitions from NEAR to FAR, the near
 * state was held for a minimum duration, the entire gesture occurs within a
 * configurable time window, and sufficient debounce time has passed since
 * the last detected wave.
 * Expected sensor: Proximity sensor (TYPE_PROXIMITY).
 * State: lastProximityEventTime (last near transition), lastProximityState
 * (current state), lastWaveTime (debounce), nearStateStartTime (min duration).
 */
internal class WaveTrigger(
    private val timeWindowMillis: Long = 2000L,
    private val debounceMillis: Long = 1000L,
    private val minNearDurationMs: Long = 300L,
) : GestureTrigger<WaveEvent> {
    private var lastProximityEventTime = 0L // Timestamp of the last proximity state change to near
    private var lastProximityState = FAR // Previous proximity state for change detection
    private var lastWaveTime = 0L // Timestamp of the last detected wave (for debounce)
    private var nearStateStartTime = 0L // When the current near state began (for min duration check)

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): WaveEvent? {
        val proximityState = if (values[0] == 0f) NEAR else FAR // Convert sensor value to near/far (0 = near)
        val stateChanged = proximityState != lastProximityState // Detect a state transition

        if (stateChanged && proximityState == NEAR) {
            // Transitioned to near: record the timing
            lastProximityEventTime = timestamp
            nearStateStartTime = timestamp
        }

        val result =
            if (isWaveDetected(timestamp, proximityState)) {
                lastWaveTime = timestamp
                WaveEvent.Waved // All wave conditions satisfied → emit
            } else {
                null
            }

        lastProximityState = proximityState // Update state for next evaluation
        return result
    }

    private fun isWaveDetected(
        timestamp: Long,
        proximityState: Int,
    ): Boolean {
        // Wave conditions: far transition, minimum near duration, debounce, and time window
        val nearDuration = timestamp - nearStateStartTime
        val isNearHeld = proximityState == FAR && nearDuration >= minNearDurationMs
        val isPastDebounce = lastWaveTime == 0L || timestamp - lastWaveTime > debounceMillis
        val isWithinTimeWindow = timestamp - lastProximityEventTime < timeWindowMillis
        val isFarTransition = lastProximityState == NEAR && proximityState == FAR
        return isPastDebounce && isWithinTimeWindow && isFarTransition && isNearHeld
    }

    private companion object {
        const val FAR = 0
        const val NEAR = 1
    }
}
