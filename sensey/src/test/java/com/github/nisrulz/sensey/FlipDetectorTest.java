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

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlipDetectorTest {

    @Mock
    private FlipDetector.FlipListener mockListener;

    private FlipDetector testFlipDetector;

    @Test
    public void detectFlipWithMiddleFaceDownValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, -9.5f}));
        verify(this.mockListener, only()).onFaceDown();
    }

    @Test
    public void detectFlipWithMiddleFaceUpValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 9.5f}));
        verify(this.mockListener, only()).onFaceUp();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithArrayLessThenThreeElements() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void notDetectFlipWithMaxFaceDownValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, -9}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void notDetectFlipWithMaxFaceUpValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 10}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void notDetectFlipWithMinFaceDownValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, -10}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void notDetectFlipWithMinFaceUpValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 9}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void notDetectFlipWithOtherValue() {
        this.testFlipDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 0}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Before
    public void setUp() {
        this.testFlipDetector = new FlipDetector(this.mockListener);
    }
}
