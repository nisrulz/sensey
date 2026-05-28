
package com.github.nisrulz.sensey.gesture.flip

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class FlipTrigger(
    private val faceUpLowerBound: Float = 8f,
    private val faceUpUpperBound: Float = 10.5f,
    private val faceDownLowerBound: Float = -10.5f,
    private val faceDownUpperBound: Float = -8f,
) : GestureTrigger<FlipEvent> {
    private var eventOccurred = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): FlipEvent? {
        val z = values[2]
        return when {
            isFaceUp(z) && eventOccurred != 1 -> {
                eventOccurred = 1
                FlipEvent.FaceUp
            }
            isFaceDown(z) && eventOccurred != 2 -> {
                eventOccurred = 2
                FlipEvent.FaceDown
            }
            else -> null
        }
    }

    private fun isFaceUp(z: Float): Boolean = z in faceUpLowerBound..faceUpUpperBound

    private fun isFaceDown(z: Float): Boolean = z in faceDownLowerBound..faceDownUpperBound
}
