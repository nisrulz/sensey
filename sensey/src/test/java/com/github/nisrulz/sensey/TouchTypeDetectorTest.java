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

package com.github.nisrulz.sensey;

import static com.github.nisrulz.sensey.TouchTypeDetector.SCROLL_DIR_DOWN;
import static com.github.nisrulz.sensey.TouchTypeDetector.SCROLL_DIR_LEFT;
import static com.github.nisrulz.sensey.TouchTypeDetector.SCROLL_DIR_RIGHT;
import static com.github.nisrulz.sensey.TouchTypeDetector.SCROLL_DIR_UP;
import static com.github.nisrulz.sensey.TouchTypeDetector.SWIPE_DIR_DOWN;
import static com.github.nisrulz.sensey.TouchTypeDetector.SWIPE_DIR_LEFT;
import static com.github.nisrulz.sensey.TouchTypeDetector.SWIPE_DIR_RIGHT;
import static com.github.nisrulz.sensey.TouchTypeDetector.SWIPE_DIR_UP;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.MotionEvent;
import com.github.nisrulz.sensey.TouchTypeDetector.TouchTypListener;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class TouchTypeDetectorTest {

    private TouchTypListener mockListener;

    private TouchTypeDetector testTouchTypeDetector;

    @Test
    public void detectNoScrollWhenEventCoorsAreEqual() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 2, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 2, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectNothingForSlightlyScrollDown() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 1, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 2, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectNothingForSlightlyScrollLeft() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 1, 0, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 2, 0, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectNothingForSlightlyScrollRight() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 2, 0, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 1, 0, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectNothingForSlightlyScrollUp() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 2, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 1, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectOnDoubleTap() {
        testTouchTypeDetector.gestureListener.onDoubleTap(null);
        verify(mockListener, only()).onDoubleTap();
    }

    @Test
    public void detectOnLongPress() {
        testTouchTypeDetector.gestureListener.onLongPress(null);
        verify(mockListener, only()).onLongPress();
    }

    @Test
    public void detectOnScrollDown() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 50, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 200, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verify(mockListener, only()).onScroll(SCROLL_DIR_DOWN);
    }

    @Test
    public void detectOnScrollLeft() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 200, 0, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 0, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verify(mockListener, only()).onScroll(SCROLL_DIR_LEFT);
    }

    @Test
    public void detectOnScrollRight() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 0, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 200, 0, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verify(mockListener, only()).onScroll(SCROLL_DIR_RIGHT);
    }

    @Test
    public void detectOnScrollUp() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 200, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 50, 0);
        testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
        verify(mockListener, only()).onScroll(SCROLL_DIR_UP);
    }

    @Test
    public void detectOnSingleTapConfirmed() {
        testTouchTypeDetector.gestureListener.onSingleTapConfirmed(null);
        verify(mockListener, only()).onSingleTap();
    }

    @Test
    public void detectOnSwipeLeft() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 60, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        verify(mockListener, only()).onSwipe(SWIPE_DIR_LEFT);
    }

    @Test
    public void detectOnSwipeLeftWithPowerBottomSwipe() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 160, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 50, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        verify(mockListener, only()).onSwipe(SWIPE_DIR_LEFT);
    }

    @Test
    public void detectOnSwipeRight() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 100, 50, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        verify(mockListener, only()).onSwipe(SWIPE_DIR_RIGHT);
    }

    @Test
    public void detectSwipeLeftAndDownForTwoLeftAndDownSwipes() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 160, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        MotionEvent ev3 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
        MotionEvent ev4 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
        testTouchTypeDetector.gestureListener.onFling(ev3, ev4, 201, 201);
        verify(mockListener, times(1)).onSwipe(SWIPE_DIR_LEFT);
        verify(mockListener, times(1)).onSwipe(SWIPE_DIR_DOWN);
    }

    @Test
    public void ignoreOnSwipeBottom() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        verify(mockListener, only()).onSwipe(SWIPE_DIR_DOWN);
    }

    @Test
    public void ignoreOnSwipeTop() {
        MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
        MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
        testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
        verify(mockListener, only()).onSwipe(SWIPE_DIR_UP);
    }

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mockListener = mock(TouchTypListener.class);
        testTouchTypeDetector = new TouchTypeDetector(context, mockListener);
    }
}
