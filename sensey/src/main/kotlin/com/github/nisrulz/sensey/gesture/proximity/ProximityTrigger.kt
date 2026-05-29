
package com.github.nisrulz.sensey.gesture.proximity

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects near/far proximity transitions.
 *
 * Algorithm: Compares the raw distance against the sensor's max-range to determine
 * near vs far state. Filters out repeated events that match the last dispatched
 * state, which guards against continuous-sensor duplicate firings and is safe for
 * on-change sensors that fire only once per transition.
 * Expected sensor: Proximity sensor (TYPE_PROXIMITY).
 * State: lastDispatchedState (last emitted event, used to suppress duplicates).
 */
internal class ProximityTrigger(
    @Suppress("UNUSED_PARAMETER") private val debounceMillis: Long = 200L,
) : GestureTrigger<ProximityEvent> {
    private var lastDispatchedState: ProximityEvent? = null // Tracks the last emitted event to avoid duplicate dispatches

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ProximityEvent? {
        val distance = values[0] // Extract raw distance from the proximity sensor
        val maxRange = values.getOrNull(1) ?: return null // Extract max-range; abort if unavailable
        val currentState = if (distance < maxRange) ProximityEvent.Near else ProximityEvent.Far // Classify near/far

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
