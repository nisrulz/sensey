
package com.github.nisrulz.sensey.gesture.light

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class LightTrigger(
    private val darkThreshold: Float = 8f,
    private val lightThreshold: Float = 12f,
) : GestureTrigger<LightEvent> {
    private var wasDark = true
    private var hasBaseline = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): LightEvent? {
        val lux = values[0]
        if (!hasBaseline) return initializeBaseline(lux)
        return when {
            lux < darkThreshold && !wasDark -> {
                wasDark = true
                LightEvent.Dark
            }
            lux > lightThreshold && wasDark -> {
                wasDark = false
                LightEvent.Light
            }
            else -> null
        }
    }

    private fun initializeBaseline(lux: Float): LightEvent {
        val isDark = lux < darkThreshold
        wasDark = isDark
        hasBaseline = true
        return if (isDark) LightEvent.Dark else LightEvent.Light
    }
}
