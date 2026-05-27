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
import org.junit.Test

class SoundLevelDetectorTest {

    @Test
    fun triggerProcessesAudioData() {
        val events = mutableListOf<SoundLevelEvent>()
        val trigger = SoundLevelTrigger()
        val result = trigger.evaluate(floatArrayOf(1000f, 2000f, -500f), 0L)
        assertNotNull(result)
    }
}
