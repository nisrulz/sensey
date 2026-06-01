
package com.github.nisrulz.sensey.gesture.touch

internal class PinchScaleTrigger : TouchGesture.Scale {
    private var eventOccurred = 0
    private var scaleInCount = 0
    private var scaleOutCount = 0

    override fun evaluate(scaleFactor: Float): TouchEvent? =
        when {
            scaleFactor > SCALE_IN_THRESHOLD -> {
                scaleInCount++
                if (eventOccurred != SCALE_IN && scaleInCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_IN
                    scaleOutCount = 0
                    TouchEvent.PinchScale(scaleFactor, isScalingOut = false)
                } else {
                    null
                }
            }
            scaleFactor < SCALE_OUT_THRESHOLD -> {
                scaleOutCount++
                if (eventOccurred != SCALE_OUT && scaleOutCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_OUT
                    scaleInCount = 0
                    TouchEvent.PinchScale(scaleFactor, isScalingOut = true)
                } else {
                    null
                }
            }
            else -> null
        }

    fun reset() {
        eventOccurred = 0
        scaleInCount = 0
        scaleOutCount = 0
    }

    companion object {
        private const val SCALE_IN_THRESHOLD = 1.01f
        private const val SCALE_OUT_THRESHOLD = 0.99f
        private const val CONFIRMATION_THRESHOLD = 2
        private const val SCALE_IN = 1
        private const val SCALE_OUT = 2
    }
}
