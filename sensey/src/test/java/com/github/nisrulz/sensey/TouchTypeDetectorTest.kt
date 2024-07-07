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
package com.github.nisrulz.sensey

import android.content.Context
import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider
import com.github.nisrulz.sensey.TouchTypeDetector.TouchTypListener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TouchTypeDetectorTest {
    private var mockListener: TouchTypListener? = null

    private var testTouchTypeDetector: TouchTypeDetector? = null

    @Test
    fun detectNoScrollWhenEventCoorsAreEqual() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 50f, 2f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 50f, 2f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectNothingForSlightlyScrollDown() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 0f, 1f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 0f, 2f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectNothingForSlightlyScrollLeft() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 1f, 0f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 2f, 0f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectNothingForSlightlyScrollRight() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 2f, 0f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 1f, 0f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectNothingForSlightlyScrollUp() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 0f, 2f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 0f, 1f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectOnScrollDown() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 0f, 50f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 0f, 200f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verify(mockListener, Mockito.only())?.onScroll(TouchTypeDetector.SCROLL_DIR_DOWN)
    }

    @Test
    fun detectOnScrollLeft() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 200f, 0f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 50f, 0f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verify(mockListener, Mockito.only())?.onScroll(TouchTypeDetector.SCROLL_DIR_LEFT)
    }

    @Test
    fun detectOnScrollRight() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 50f, 0f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 200f, 0f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verify(mockListener, Mockito.only())?.onScroll(TouchTypeDetector.SCROLL_DIR_RIGHT)
    }

    @Test
    fun detectOnScrollUp() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 0f, 200f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 0f, 50f, 0)
        testTouchTypeDetector?.gestureListener?.onScroll(ev1, ev2, 0f, 0f)
        Mockito.verify(mockListener, Mockito.only())?.onScroll(TouchTypeDetector.SCROLL_DIR_UP)
    }

    @Test
    fun detectOnSwipeLeft() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 300f, 50f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 100f, 60f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        Mockito.verify(mockListener, Mockito.only())?.onSwipe(TouchTypeDetector.SWIPE_DIR_LEFT)
    }

    @Test
    fun detectOnSwipeLeftWithPowerBottomSwipe() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 300f, 160f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 100f, 50f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        Mockito.verify(mockListener, Mockito.only())?.onSwipe(TouchTypeDetector.SWIPE_DIR_LEFT)
    }

    @Test
    fun detectOnSwipeRight() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 100f, 50f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 300f, 50f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        Mockito.verify(mockListener, Mockito.only())?.onSwipe(TouchTypeDetector.SWIPE_DIR_RIGHT)
    }

    @Test
    fun detectSwipeLeftAndDownForTwoLeftAndDownSwipes() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 300f, 50f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 100f, 160f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        val ev3 = MotionEvent.obtain(10, 10, 0, 160f, 50f, 0)
        val ev4 = MotionEvent.obtain(10, 10, 0, 50f, 500f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev3, ev4, 201f, 201f)
        Mockito.verify(mockListener, Mockito.times(1))?.onSwipe(TouchTypeDetector.SWIPE_DIR_LEFT)
        Mockito.verify(mockListener, Mockito.times(1))?.onSwipe(TouchTypeDetector.SWIPE_DIR_DOWN)
    }

    @Test
    fun ignoreOnSwipeBottom() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 160f, 50f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 50f, 500f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        Mockito.verify(mockListener, Mockito.only())?.onSwipe(TouchTypeDetector.SWIPE_DIR_DOWN)
    }

    @Test
    fun ignoreOnSwipeTop() {
        val ev1 = MotionEvent.obtain(10, 10, 0, 50f, 500f, 0)
        val ev2 = MotionEvent.obtain(10, 10, 0, 160f, 50f, 0)
        testTouchTypeDetector?.gestureListener?.onFling(ev1, ev2, 201f, 201f)
        Mockito.verify(mockListener, Mockito.only())?.onSwipe(TouchTypeDetector.SWIPE_DIR_UP)
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mockListener = mock(TouchTypListener::class.java)
        testTouchTypeDetector = TouchTypeDetector(context, mockListener)
    }
}
