
package com.github.nisrulz.sensey.internal

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTest {
    // ── magnitude3 ──────────────────────────────────────────────────────────

    @Test
    fun magnitude3_zero() {
        assertEquals(0f, magnitude3(floatArrayOf(0f, 0f, 0f)), 1e-6f)
    }

    @Test
    fun magnitude3_positive() {
        assertEquals(5f, magnitude3(floatArrayOf(3f, 0f, 4f)), 1e-6f)
    }

    @Test
    fun magnitude3_negative() {
        assertEquals(5f, magnitude3(floatArrayOf(-3f, 0f, -4f)), 1e-6f)
    }

    @Test
    fun magnitude3_gravity() {
        assertEquals(9.81f, magnitude3(floatArrayOf(0f, 0f, 9.81f)), 1e-2f)
    }

    // ── angleBetweenDeg ─────────────────────────────────────────────────────

    @Test
    fun angleBetweenDeg_aligned() {
        val angle = angleBetweenDeg(1f, 0f, 0f, 1f, 0f, 0f)
        assertEquals(0f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_opposite() {
        val angle = angleBetweenDeg(1f, 0f, 0f, -1f, 0f, 0f)
        assertEquals(180f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_perpendicular() {
        val angle = angleBetweenDeg(1f, 0f, 0f, 0f, 1f, 0f)
        assertEquals(90f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_45deg() {
        val angle = angleBetweenDeg(1f, 0f, 0f, 1f, 1f, 0f)
        assertEquals(45f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_zeroVector() {
        val angle = angleBetweenDeg(0f, 0f, 0f, 1f, 0f, 0f)
        assertEquals(0f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_gravityVsZ() {
        // Gravity along +Z → angle from +Z should be 0
        val angle = angleBetweenDeg(0f, 0f, 9.81f, 0f, 0f, 1f)
        assertEquals(0f, angle, 1e-4f)
    }

    @Test
    fun angleBetweenDeg_gravityTilted() {
        // Gravity at 45° from Z: (1, 0, 1) normalized vs (0, 0, 1) → ~45°
        val angle = angleBetweenDeg(1f, 0f, 1f, 0f, 0f, 1f)
        assertEquals(45f, angle, 1f)
    }

    // ── linearAccelMag ──────────────────────────────────────────────────────

    @Test
    fun linearAccelMag_stationary() {
        // Phone at rest: accel == gravity
        val mag = linearAccelMag(0f, 0f, 9.81f, 0f, 0f, 9.81f)
        assertEquals(0f, mag, 1e-4f)
    }

    @Test
    fun linearAccelMag_spikeOnZ() {
        // Tap on screen/back while on table (Z-axis spike)
        val mag = linearAccelMag(0f, 0f, 15f, 0f, 0f, 9.81f)
        assertEquals(5.19f, mag, 0.1f)
    }

    @Test
    fun linearAccelMag_spikeOnX() {
        // Tap on side while holding: sqrt(10² + 9.81²) - 9.81 ≈ 14.01 - 9.81 = 4.20
        val mag = linearAccelMag(10f, 0f, 9.81f, 0f, 0f, 9.81f)
        assertEquals(4.20f, mag, 0.1f)
    }

    @Test
    fun linearAccelMag_freeFall() {
        // Phone in free-fall: accel ≈ 0, gravity = 9.81
        val mag = linearAccelMag(0f, 0f, 0f, 0f, 0f, 9.81f)
        assertEquals(9.81f, mag, 1e-4f)
    }

    @Test
    fun linearAccelMag_negativeSpike() {
        // Sudden deceleration: accel < gravity
        val mag = linearAccelMag(0f, 0f, 2f, 0f, 0f, 9.81f)
        assertEquals(7.81f, mag, 1e-4f)
    }
}
