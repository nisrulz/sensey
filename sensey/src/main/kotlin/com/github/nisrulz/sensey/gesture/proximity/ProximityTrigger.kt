
package com.github.nisrulz.sensey.gesture.proximity

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class ProximityTrigger(
    private val debounceMillis: Long = 200L,
) : GestureTrigger<ProximityEvent> {
    private var lastDispatchedState: ProximityEvent? = null
    private var lastStateChangeTime = 0L
    private var hasPendingState = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ProximityEvent? {
        val distance = values[0]
        val maxRange = values.getOrNull(1) ?: return null
        val currentState = if (distance < maxRange) ProximityEvent.Near else ProximityEvent.Far

        if (currentState == lastDispatchedState) {
            hasPendingState = false
            return null
        }

        if (!hasPendingState) {
            hasPendingState = true
            lastStateChangeTime = timestamp
            return null
        }

        if (timestamp - lastStateChangeTime >= debounceMillis) {
            lastDispatchedState = currentState
            hasPendingState = false
            return currentState
        }

        return null
    }
}
