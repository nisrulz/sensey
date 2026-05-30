
package com.github.nisrulz.sensey.gesture.raisetoear

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.magnitude3

/**
 * Detects when the device is raised to the ear (call position).
 *
 * Algorithm: Evaluates a combined 4-element input:
 *   [proximityCm, gx, gy, gz]
 * where [proximityCm] is the proximity sensor distance and gx/gy/gz
 * is the gravity vector. The gesture fires when the device is near
 * the ear (proximity < [maxProximityCm]) AND NOT flat on a surface
 * (checked via |gz|/gravMag ≤ [minGzRatio]). When held upright at the
 * ear, gravity is along the Y-axis so gz ≈ 0; when flat on a table
 * gz ≈ 9.81.
 * Expected sensor pair: Proximity (TYPE_PROXIMITY) + Gravity (TYPE_GRAVITY).
 * State: lastTriggerTime (debounce).
 */
internal class RaiseToEarTrigger(
    private val maxProximityCm: Float = 5f,
    private val minGzRatio: Float = 0.3f,
    private val debounceMs: Long = 500L,
) : GestureTrigger<RaiseToEarEvent> {
    private var lastTriggerTime = 0L // Timestamp of the last AtEar event (for debounce)

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): RaiseToEarEvent? {
        if (values.size < 4) return null
        val proximityCm = values[0] // Distance from proximity sensor (cm)
        val gx = values[1]
        val gy = values[2]
        val gz = values[3]

        val isNear = proximityCm in 0f..maxProximityCm
        if (!isNear) return null

        val gravMag = magnitude3(floatArrayOf(gx, gy, gz))
        val absGzRatio = kotlin.math.abs(gz) / gravMag
        val isNotFlat = absGzRatio <= minGzRatio
        if (!isNotFlat) return null

        if (timestamp - lastTriggerTime < debounceMs) return null
        lastTriggerTime = timestamp
        return RaiseToEarEvent.AtEar
    }
}
