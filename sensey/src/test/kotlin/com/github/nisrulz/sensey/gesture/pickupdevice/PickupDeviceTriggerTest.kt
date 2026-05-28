
package com.github.nisrulz.sensey.gesture.pickupdevice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PickupDeviceTriggerTest {
    @Test
    fun noEventOnStableFlatSurface() {
        val trigger = PickupDeviceTrigger(windowSize = 4, settleReadings = 3)
        repeat(10) {
            assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))
        }
    }

    @Test
    fun pickedUpWhenValuesBecomeUnstable() {
        val trigger = PickupDeviceTrigger(stableRange = 0.5f, movingRange = 1.5f, windowSize = 4)
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) }
        val result = trigger.evaluate(floatArrayOf(5f, 0f, 5f), 0L)
        assertEquals(PickupDeviceEvent.PickedUp, result)
    }

    @Test
    fun notPickedUpWithSmallFluctuation() {
        val trigger = PickupDeviceTrigger(movingRange = 3f, windowSize = 4)
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) }
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 10.5f), 0L))
    }

    @Test
    fun putDownAfterBeingHeldAndSettled() {
        val trigger =
            PickupDeviceTrigger(
                stableRange = 0.5f,
                movingRange = 1.5f,
                windowSize = 4,
                settleReadings = 3,
            )

        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) }
        trigger.evaluate(floatArrayOf(5f, 0f, 5f), 0L) // PickedUp
        repeat(4) { trigger.evaluate(floatArrayOf(5f, 0f, 5.1f), 0L) } // held

        // Return to table: fill buffer with gravity values
        val putDownFired =
            (1..10).any {
                trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) == PickupDeviceEvent.PutDown
            }
        assert(putDownFired)
    }

    @Test
    fun noPutDownWithoutPriorPickup() {
        val trigger = PickupDeviceTrigger(stableRange = 0.5f, windowSize = 4, settleReadings = 3)
        repeat(20) {
            assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))
        }
    }

    @Test
    fun putDownRequiresStabilization() {
        val trigger =
            PickupDeviceTrigger(
                stableRange = 0.5f,
                movingRange = 1.5f,
                windowSize = 4,
                settleReadings = 5,
            )

        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) }
        trigger.evaluate(floatArrayOf(5f, 0f, 5f), 0L) // PickedUp
        repeat(4) { trigger.evaluate(floatArrayOf(5f, 0f, 5.1f), 0L) }

        // One stable reading isn't enough
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))

        // Full flush of buffer + settleReadings
        val putDownFired =
            (1..10).any {
                trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L) == PickupDeviceEvent.PutDown
            }
        assert(putDownFired)
    }
}
