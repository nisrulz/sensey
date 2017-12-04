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

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_PROXIMITY;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.github.nisrulz.sensey.FlipDetector.FlipListener;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowSensorManager;

@RunWith(RobolectricTestRunner.class)
public class SenseyTest {

    private Sensey sensey;

    private ShadowSensorManager shadowSensorManager;

    @Test
    public void detectListenerWithStartFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        sensey.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(fakeListener, FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetectionWithCustomThreshold() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(4, fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        sensey.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartOrientationDetectionWithCustomSmoothness() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        sensey.startOrientationDetection(3, fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        sensey.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(fakeListener, ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartShakeDetectionWithCustomThreshold() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(4f, 1000, fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        sensey.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(fakeListener, FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip",
                    shadowSensorManager.hasListener(detector));
            sensey.stopFlipDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
            sensey.stopLightDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        sensey.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
            sensey.stopOrientationDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        sensey.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(fakeListener, ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity",
                    shadowSensorManager.hasListener(detector));
            sensey.stopProximityDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
            sensey.stopShakeDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStoppingTwoSameDetections() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener1 = mock(ProximityListener.class);
        ProximityListener fakeListener2 = mock(ProximityListener.class);
        ProximityDetector detector1 = startProximityDetection(fakeListener1);
        ProximityDetector detector2 = startProximityDetection(fakeListener2);
        sensey.stopProximityDetection(fakeListener1);
        sensey.stopProximityDetection(fakeListener2);
        assertFalse("Sensor manager need to contain no detectors",
                shadowSensorManager.hasListener(detector2));
        assertFalse("Sensor manager need to contain no detectors",
                shadowSensorManager.hasListener(detector1));
    }

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        shadowSensorManager =
                Shadows.shadowOf((SensorManager) context.getSystemService(SENSOR_SERVICE));

        sensey = Sensey.getInstance();
        sensey.init(context);
    }

    @After
    public void tearDown() {
        sensey = null;
        shadowSensorManager = null;
    }

    private void addSensor(int type) {
        shadowSensorManager.addSensor(type, mock(Sensor.class));
    }

    //Hardcode because of can not get appropriate detector from Sensey.class
    private <T> T getDetector(Object listener, Class<T> aClass) {
        T result = null;

        try {
            Field field = sensey.getClass().getDeclaredField("defaultSensorsMap");
            field.setAccessible(true);
            Map<Object, SensorDetector> defaults = (Map<Object, SensorDetector>) field.get(sensey);
            result = aClass.cast(defaults.get(listener));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getFieldName(Class aClass) {
        if (aClass == ShakeDetector.class) {
            return "shakeDetector";
        } else if (aClass == ProximityDetector.class) {
            return "proximityDetector";
        } else if (aClass == OrientationDetector.class) {
            return "orientationDetector";
        } else if (aClass == LightDetector.class) {
            return "lightDetector";
        } else if (aClass == FlipDetector.class) {
            return "flipDetector";
        } else if (aClass == WaveDetector.class) {
            return "waveDetector";
        } else {
            return null;
        }
    }

    private ProximityDetector startProximityDetection(ProximityListener listener) {
        sensey.startProximityDetection(listener);
        return getDetector(listener, ProximityDetector.class);
    }
}
