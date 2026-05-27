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
package com.github.nisrulz.sensey.gesture.touchtype

import android.content.Context
import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TouchTypeDetectorTest {

    @Test
    fun dispatchesNothingOnActionDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val events = mutableListOf<TouchTypeEvent>()
        val detector = TouchTypeDetector(context, TouchTypeTrigger(), { events.add(it) })
        detector.onTouchEvent(MotionEvent.obtain(10, 10, MotionEvent.ACTION_DOWN, 100f, 100f, 0))
        assertTrue(events.isEmpty())
    }

    @Test
    fun initDoesNotCrash() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val detector = TouchTypeDetector(context, TouchTypeTrigger(), { })
        assertTrue(detector is TouchTypeDetector)
    }
}
