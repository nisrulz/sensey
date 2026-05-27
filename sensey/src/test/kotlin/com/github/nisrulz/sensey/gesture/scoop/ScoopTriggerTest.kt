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
package com.github.nisrulz.sensey.gesture.scoop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScoopTriggerTest {
    @Test
    fun scoopedWhenValuesExceedThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertEquals(ScoopEvent.Scooped, trigger.evaluate(floatArrayOf(20f, -20f, 20f), 0L))
    }

    @Test
    fun notScoopedWhenBelowThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun notScoopedWhenPartialConditionMet() {
        val trigger = ScoopTrigger(impulseThreshold = 15f)
        assertNull(trigger.evaluate(floatArrayOf(20f, -5f, 20f), 0L))
    }

    @Test
    fun scoopedWithCustomThreshold() {
        val trigger = ScoopTrigger(impulseThreshold = 5f)
        assertEquals(ScoopEvent.Scooped, trigger.evaluate(floatArrayOf(10f, -10f, 10f), 0L))
    }
}
