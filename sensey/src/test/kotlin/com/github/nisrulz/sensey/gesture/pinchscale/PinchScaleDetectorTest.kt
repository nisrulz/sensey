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

import android.content.Context
import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PinchScaleDetectorTest {

    @Test
    fun dispatcherNotCalledOnActionDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var called = false
        val detector = PinchScaleDetector(
            context, PinchScaleTrigger(),
            dispatcher = { called = true },
        )
        val event = MotionEvent.obtain(10, 10, MotionEvent.ACTION_DOWN, 100f, 100f, 0)
        detector.onTouchEvent(event)
        assert(!called)
    }

    @Test
    fun initDoesNotCrash() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val detector = PinchScaleDetector(
            context, PinchScaleTrigger(),
            dispatcher = {},
            onScaleStart = {},
            onScaleEnd = {},
        )
        assertNotNull(detector)
    }
}
