/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.sensey.gesture.movement

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovementTriggerTest {
    private val trigger = MovementTrigger(threshold = 0.3f, timeBeforeDeclaringStationary = 5000L)

    @Test
    fun noEventWithStableGravityValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))
    }

    @Test
    fun movementDetectedWhenDeltaExceedsThreshold() {
        assertEquals(MovementEvent.Moved(MovementEvent.Direction.Z_NEG), trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun stationaryAfterTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(MovementEvent.Stationary, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 6000L))
    }

    @Test
    fun noStationaryBeforeTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 3000L))
    }

    @Test
    fun movingAgainAfterStationary() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 6000L)
        assertEquals(
            MovementEvent.Moved(MovementEvent.Direction.Z_POS),
            trigger.evaluate(floatArrayOf(0f, 0f, 10f), 7000L),
        )
    }
}
