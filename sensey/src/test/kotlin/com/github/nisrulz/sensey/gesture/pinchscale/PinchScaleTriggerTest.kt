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
package com.github.nisrulz.sensey.gesture.pinchscale

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PinchScaleTriggerTest {

    private val trigger = PinchScaleTrigger()

    @Test
    fun noEventBelowScaleCount() {
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun scaleOutDetectedAfterThirdScaleIn() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        assertEquals(PinchScaleEvent(1.5f, false), trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun scaleInDetectedAfterThirdScaleOut() {
        trigger.evaluate(floatArrayOf(0.5f), 0L)
        trigger.evaluate(floatArrayOf(0.5f), 0L)
        assertEquals(PinchScaleEvent(0.5f, true), trigger.evaluate(floatArrayOf(0.5f), 0L))
    }

    @Test
    fun scaleOutOnlyOnce() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }

    @Test
    fun resetClearsState() {
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.evaluate(floatArrayOf(1.5f), 0L)
        trigger.reset()
        assertNull(trigger.evaluate(floatArrayOf(1.5f), 0L))
    }
}
