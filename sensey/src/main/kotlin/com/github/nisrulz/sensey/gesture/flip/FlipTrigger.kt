
package com.github.nisrulz.sensey.gesture.flip

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects device face-up and face-down orientation.
 *
 * Algorithm: Compares the Z-axis acceleration against configurable bounds.
 * A Z value between ~8 and 10.5 indicates face-up; a Z value between
 * ~-10.5 and -8 indicates face-down. Events are emitted only once per
 * orientation state change to avoid repeated dispatches.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: eventOccurred (tracks the last emitted orientation to deduplicate).
 */
internal class FlipTrigger(
    private val faceUpLowerBound: Float = 8f,
    private val faceUpUpperBound: Float = 10.5f,
    private val faceDownLowerBound: Float = -10.5f,
    private val faceDownUpperBound: Float = -8f,
) : GestureTrigger<FlipEvent> {
    private var eventOccurred = 0 // Tracks the last emitted orientation (1=face up, 2=face down) to deduplicate

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): FlipEvent? {
        val z = values[2] // Extract Z-axis acceleration
        return when {
            isFaceUp(z) && eventOccurred != 1 -> {
                eventOccurred = 1
                FlipEvent.FaceUp // Device is face-up and hasn't been reported yet
            }
            isFaceDown(z) && eventOccurred != 2 -> {
                eventOccurred = 2
                FlipEvent.FaceDown // Device is face-down and hasn't been reported yet
            }
            else -> null // No change or already in this state
        }
    }

    private fun isFaceUp(z: Float): Boolean = z in faceUpLowerBound..faceUpUpperBound
    // True when Z-axis acceleration is in the ~8–10.5 range (facing up)

    private fun isFaceDown(z: Float): Boolean = z in faceDownLowerBound..faceDownUpperBound
    // True when Z-axis acceleration is in the ~-10.5 to -8 range (facing down)
}
