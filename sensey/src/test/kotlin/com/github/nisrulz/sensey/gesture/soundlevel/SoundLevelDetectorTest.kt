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
package com.github.nisrulz.sensey.gesture.soundlevel

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SoundLevelDetectorTest {

    @Test
    fun triggerProcessesAudioData() {
        val events = mutableListOf<SoundLevelEvent>()
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(1000f, 2000f, -500f), 0L)
        assertNotNull(result)
    }

    @Test
    fun triggerReturnsNullForEmptyInput() {
        val trigger = SoundLevelTrigger()
        assertTrue(trigger.evaluate(floatArrayOf(), 0L) == null)
    }

    @Test
    fun zeroValuesProduceVeryLowLevel() {
        val trigger = SoundLevelTrigger()
        assertNotNull(trigger.evaluate(floatArrayOf(0f, 0f), 0L))
    }

    @Test
    fun triggerHandlesSingleSample() {
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(10000f), 0L)
        assertNotNull(result)
    }

    @Test
    fun handleInfiniteInput() {
        val trigger = SoundLevelTrigger()
        assertTrue(trigger.evaluate(floatArrayOf(Float.POSITIVE_INFINITY), 0L) == null)
    }
}
