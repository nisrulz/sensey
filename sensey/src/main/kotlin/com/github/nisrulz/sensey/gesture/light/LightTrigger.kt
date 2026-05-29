
package com.github.nisrulz.sensey.gesture.light

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects dark/light transitions.
 *
 * Algorithm: Compares ambient lux values against configurable dark/light thresholds.
 * On first reading, establishes a baseline state. Subsequent readings trigger a
 * transition event only when the value crosses the opposite threshold (dark→light
 * or light→dark). Same-state readings are ignored to avoid repeated events.
 * Expected sensor: Light sensor (TYPE_LIGHT).
 * State: wasDark (current ambient state), hasBaseline (whether initial reading was captured).
 */
internal class LightTrigger(
    private val darkThreshold: Float = 8f,
    private val lightThreshold: Float = 12f,
) : GestureTrigger<LightEvent> {
    private var wasDark = true // Tracks the current ambient state (dark vs light)
    private var hasBaseline = false // Whether the initial sensor reading has been captured

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): LightEvent? {
        val lux = values[0] // Extract illuminance (lux) from the sensor reading
        if (!hasBaseline) return initializeBaseline(lux) // First reading: establish the initial state
        return when {
            lux < darkThreshold && !wasDark -> { // Transitioned from light to dark
                wasDark = true
                LightEvent.Dark // Emit dark event
            }
            lux > lightThreshold && wasDark -> { // Transitioned from dark to light
                wasDark = false
                LightEvent.Light // Emit light event
            }
            else -> null // No threshold crossing → no event
        }
    }

    private fun initializeBaseline(lux: Float): LightEvent {
        // Establish the initial light state from the first sensor reading
        val isDark = lux < darkThreshold
        wasDark = isDark
        hasBaseline = true
        return if (isDark) LightEvent.Dark else LightEvent.Light // Emit initial state
    }
}
