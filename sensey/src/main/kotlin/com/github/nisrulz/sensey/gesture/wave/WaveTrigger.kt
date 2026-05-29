
package com.github.nisrulz.sensey.gesture.wave

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class WaveTrigger(
    private val timeWindowMillis: Long = 2000L,
    private val debounceMillis: Long = 1000L,
    private val minNearDurationMs: Long = 300L,
) : GestureTrigger<WaveEvent> {
    private var lastProximityEventTime = 0L
    private var lastProximityState = FAR
    private var lastWaveTime = 0L
    private var nearStateStartTime = 0L

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): WaveEvent? {
        val proximityState = if (values[0] == 0f) NEAR else FAR
        val stateChanged = proximityState != lastProximityState

        if (stateChanged && proximityState == NEAR) {
            lastProximityEventTime = timestamp
            nearStateStartTime = timestamp
        }

        val result =
            if (isWaveDetected(timestamp, proximityState)) {
                lastWaveTime = timestamp
                WaveEvent.Waved
            } else {
                null
            }

        lastProximityState = proximityState
        return result
    }

    private fun isWaveDetected(
        timestamp: Long,
        proximityState: Int,
    ): Boolean {
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
