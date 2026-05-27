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
package com.github.nisrulz.sensey.gesture.rotationangle

import org.junit.Assert.assertEquals
import org.junit.Test

class RotationAngleTriggerTest {
    private val trigger = RotationAngleTrigger()

    @Test
    fun returnsRotationEvent() {
        val result = trigger.evaluate(floatArrayOf(45f, 30f, 90f), 0L)
        assertEquals(RotationAngleEvent(45f, 30f, 90f), result)
    }

    @Test
    fun returnsEventWithZeroAngles() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(RotationAngleEvent(0f, 0f, 0f), result)
    }

    @Test
    fun handlesNegativeAngles() {
        val result = trigger.evaluate(floatArrayOf(-45f, -30f, -90f), 0L)
        assertEquals(RotationAngleEvent(-45f, -30f, -90f), result)
    }
}
