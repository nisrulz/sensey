
package com.github.nisrulz.sensey.internal

import org.junit.Assert.assertEquals
import org.junit.Test

class EmaSmootherTest {
    @Test
    fun startsAtZero() {
        val s = EmaSmoother()
        assertEquals(0f, s.current, 1e-6f)
    }

    @Test
    fun firstUpdateConverges() {
        val s = EmaSmoother(alpha = 0.9f)
        assertEquals(1f, s.update(10f), 1e-4f)
    }

    @Test
    fun convergesToConstant() {
        val s = EmaSmoother(alpha = 0.5f)
        s.update(100f)
        s.update(100f)
        assertEquals(87.5f, s.update(100f), 1e-4f)
    }

    @Test
    fun resetClearsState() {
        val s = EmaSmoother(alpha = 0.5f)
        s.update(100f)
        assertEquals(50f, s.current, 1e-4f)
        s.reset()
        assertEquals(0f, s.current, 1e-4f)
        assertEquals(5f, s.update(10f), 1e-4f)
    }

    @Test
    fun followsGradualChanges() {
        val s = EmaSmoother(alpha = 0.8f)
        s.update(0f)
        assertEquals(0.2f, s.update(1f), 0.01f)
        assertEquals(0.56f, s.update(2f), 0.01f)
        assertEquals(1.05f, s.update(3f), 0.01f)
        assertEquals(1.64f, s.update(4f), 0.01f)
    }

    @Test
    fun jerkFromSpike() {
        val s = EmaSmoother(alpha = 0.9f)
        s.update(0f)
        val smooth = s.update(10f)
        assertEquals(1f, smooth, 1e-4f)
        assertEquals(9f, kotlin.math.abs(10f - smooth), 1e-4f)
    }
}
