
package com.github.nisrulz.sensey.gesture.pinchscale

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class PinchScaleTrigger : GestureTrigger<PinchScaleEvent> {
    private var eventOccurred = 0
    private var scaleInCount = 0
    private var scaleOutCount = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): PinchScaleEvent? {
        val scaleFactor = values.getOrNull(0) ?: return null

        return when {
            isScalingIn(scaleFactor) -> {
                scaleInCount++
                if (eventOccurred != SCALE_IN && scaleInCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_IN
                    scaleOutCount = 0
                    PinchScaleEvent(scaleFactor, isScalingOut = false)
                } else {
                    null
                }
            }
            isScalingOut(scaleFactor) -> {
                scaleOutCount++
                if (eventOccurred != SCALE_OUT && scaleOutCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_OUT
                    scaleInCount = 0
                    PinchScaleEvent(scaleFactor, isScalingOut = true)
                } else {
                    null
                }
            }
            else -> null
        }
    }

    fun reset() {
        eventOccurred = 0
        scaleInCount = 0
        scaleOutCount = 0
    }

    private fun isScalingIn(scaleFactor: Float): Boolean = scaleFactor > SCALE_IN_THRESHOLD

    private fun isScalingOut(scaleFactor: Float): Boolean = scaleFactor < SCALE_OUT_THRESHOLD

    companion object {
        private const val SCALE_IN_THRESHOLD = 1.01f
        private const val SCALE_OUT_THRESHOLD = 0.99f
        private const val CONFIRMATION_THRESHOLD = 2
        private const val SCALE_IN = 1
        private const val SCALE_OUT = 2
    }
}
