package com.github.nisrulz.sensey;

import android.content.Context;
import android.view.MotionEvent;

import com.github.nisrulz.sensey.TouchTypeDetector.TouchTypListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLooper;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Michael Spitsin
 * @since 2016-06-23
 */
@RunWith(RobolectricTestRunner.class)
public class TouchTypeDetectorTest {

    private TouchTypListener mockListener;
    private TouchTypeDetector testTouchTypeDetector;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mockListener = mock(TouchTypListener.class);
        testTouchTypeDetector = new TouchTypeDetector(context, mockListener);
    }

    @Test
    public void detectOnDoubleTap() {
        testTouchTypeDetector.listener.onDoubleTap(null);
        verify(mockListener, only()).onDoubleTap();
    }

    @Test
    public void detectOnDown() {
        testTouchTypeDetector.listener.onDown(null);
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectOnLongPress() {
        testTouchTypeDetector.listener.onLongPress(null);
        verify(mockListener, only()).onLongPress();
    }

    @Test
    public void detectOnSingleTapConfirmed() {
        testTouchTypeDetector.listener.onSingleTapConfirmed(null);
        verify(mockListener, only()).onSingleTap();
    }

    @Test
    public void detectOnSingleTapUp() {
        testTouchTypeDetector.listener.onSingleTapUp(null);
        verifyNoMoreInteractions(mockListener);
    }
}
