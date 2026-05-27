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
package com.github.nisrulz.sensey.gesture.light

import org.junit.Assert.assertEquals
import org.junit.Test

class LightTriggerTest {

    @Test
    fun darkWhenLuxBelowCustomThreshold() {
        val trigger = LightTrigger(threshold = 9f)
        assertEquals(LightEvent.Dark, trigger.evaluate(floatArrayOf(3f), 0L))
    }

    @Test
    fun darkWhenLuxBelowDefaultThreshold() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Dark, trigger.evaluate(floatArrayOf(1f), 0L))
    }

    @Test
    fun lightWithExtraValues() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(10f, 0f, 43f, 3f, -423f), 0L))
    }

    @Test
    fun lightWhenLuxEqualsCustomThreshold() {
        val trigger = LightTrigger(threshold = 9f)
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(9f), 0L))
    }

    @Test
    fun lightWhenLuxEqualsDefaultThreshold() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(3f), 0L))
    }

    @Test
    fun lightWhenLuxAboveCustomThreshold() {
        val trigger = LightTrigger(threshold = 9f)
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(12f), 0L))
    }

    @Test
    fun lightWhenLuxAboveDefaultThreshold() {
        val trigger = LightTrigger()
        assertEquals(LightEvent.Light, trigger.evaluate(floatArrayOf(10f), 0L))
    }
}
