
package com.github.nisrulz.sensey.gesture.taponback

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.EmaSmoother
import com.github.nisrulz.sensey.internal.linearAccelMag
import kotlin.math.abs

/**
 * Detects taps on the back of the device.
 *
 * Algorithm: Computes the linear acceleration magnitude (|accel - gravity|)
 * which spikes during any tap regardless of device orientation. Tracks it
 * with an EMA smoother so gradual movements (tilts) produce low jerk while
 * sharp impulses (taps) produce high jerk.
 * A valid tap requires: sufficient linear acceleration, minimum jerk, and
 * debounce since the last tap. Emits immediately when two valid taps occur
 * within [tapIntervalMs]. A single tap is silently discarded. After emission
 * a [cooldownMs] period prevents re-triggering.
 * Expected sensor pairs: TYPE_ACCELEROMETER + TYPE_GRAVITY.
 * Input: evaluate() receives a 6-element array:
 *   [ax, ay, az, gx, gy, gz] where ax/ay/az are raw acceleration and
 *   gx/gy/gz is the gravity vector from TYPE_GRAVITY.
 * State: smoother (EMA follower), lastTapTime (debounce + interval),
 *   tapCount (sequence accumulator), cooldownUntil (post-emission guard).
 */
internal class TapOnBackTrigger(
    private val accelThreshold: Float = 2f,
    private val minJerk: Float = 2f,
    private val tapDebounceMs: Long = 250L,
    private val tapIntervalMs: Long = 500L,
    private val cooldownMs: Long = 1000L,
) : GestureTrigger<TapOnBackEvent> {
    private val smoother = EmaSmoother()
    private var lastTapTime = 0L // Timestamp of the last detected tap (for debounce and interval check)
    private var tapCount = 0 // Number of taps accumulated in the current sequence
    private var cooldownUntil = 0L // Timestamp before which all events are ignored

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TapOnBackEvent? {
        if (timestamp < cooldownUntil) return null

        val linearMag =
            linearAccelMag(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
            )
        val jerk = abs(linearMag - smoother.update(linearMag))

        if (isValidTap(linearMag = linearMag, jerk = jerk, timestamp = timestamp)) {
            tapCount++
            smoother.reset()
            if (tapCount >= 2) {
                tapCount = 0
                cooldownUntil = timestamp + cooldownMs
                return TapOnBackEvent
            }
            lastTapTime = timestamp
            return null
        }

        if (tapCount > 0 && timestamp - lastTapTime > tapIntervalMs) {
            tapCount = 0
        }

        return null
    }

    private fun isValidTap(
        linearMag: Float,
        jerk: Float,
        timestamp: Long,
    ): Boolean =
        linearMag > accelThreshold &&
            timestamp - lastTapTime > tapDebounceMs &&
            jerk > minJerk
}
