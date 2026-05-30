
package com.github.nisrulz.sensey.internal

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

/** Euclidean norm of a 3-element vector. */
internal fun magnitude3(values: FloatArray): Float =
    sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

/** Angle in degrees between two 3-element vectors. Returns 0 if either vector is zero. */
internal fun angleBetweenDeg(
    ax: Float,
    ay: Float,
    az: Float,
    bx: Float,
    by: Float,
    bz: Float,
): Float {
    val ma = sqrt(ax * ax + ay * ay + az * az)
    val mb = sqrt(bx * bx + by * by + bz * bz)
    if (ma == 0f || mb == 0f) return 0f
    val dot = ax * bx + ay * by + az * bz
    return Math.toDegrees(acos((dot / (ma * mb)).coerceIn(-1f, 1f)).toDouble()).toFloat()
}

/** Linear acceleration magnitude: |accelMag - gravMag|. */
internal fun linearAccelMag(
    ax: Float,
    ay: Float,
    az: Float,
    gx: Float,
    gy: Float,
    gz: Float,
): Float = abs(magnitude3(floatArrayOf(ax, ay, az)) - magnitude3(floatArrayOf(gx, gy, gz)))
