
package com.github.nisrulz.sensey.internal

/**
 * Single-pole EMA (exponential moving average) smoother.
 *
 * Acts as a low-pass filter: gradual changes are followed by the smoother
 * while sharp impulses produce a large deviation (jerk) from the smooth value.
 * Call [update] with each raw sample; call [reset] to clear state.
 */
internal class EmaSmoother(
    private val alpha: Float = 0.9f,
) {
    private var smooth = 0f

    /** Feed a raw value and return the current smoothed value. */
    fun update(raw: Float): Float {
        smooth = alpha * smooth + (1f - alpha) * raw
        return smooth
    }

    /** The current smoothed value. */
    val current: Float get() = smooth

    /** Reset the smoother to zero. */
    fun reset() {
        smooth = 0f
    }
}
