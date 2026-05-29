
package com.github.nisrulz.sensey.gesture.proximity

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class ProximityTrigger(
    @Suppress("UNUSED_PARAMETER") private val debounceMillis: Long = 200L,
) : GestureTrigger<ProximityEvent> {
    private var lastDispatchedState: ProximityEvent? = null

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ProximityEvent? {
        val distance = values[0]
        val maxRange = values.getOrNull(1) ?: return null
        val currentState = if (distance < maxRange) ProximityEvent.Near else ProximityEvent.Far

        // Same as last dispatched → no change (filters repeated events from continuous sensors)
        if (currentState == lastDispatchedState) return null

        // State transition detected → dispatch immediately.
        // Previous debounce-based algorithm required 2 events in the new state to dispatch,
        // which permanently stalled with on-change proximity sensors (they fire only once
        // per transition). The lastDispatchedState compare above is sufficient to prevent
        // same-state re-dispatches from continuous sensors.
        lastDispatchedState = currentState
        return currentState
    }
}
