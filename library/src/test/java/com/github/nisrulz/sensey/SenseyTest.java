package com.github.nisrulz.sensey;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.github.nisrulz.sensey.FlipDetector.FlipListener;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowSensorManager;

import java.lang.reflect.Field;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_PROXIMITY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SenseyTest {
    private Sensey sensey;
    private ShadowSensorManager shadowSensorManager;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        shadowSensorManager = Shadows.shadowOf((SensorManager) context.getSystemService(SENSOR_SERVICE));

        sensey = Sensey.getInstance();
        sensey.init(context);
    }

    @After
    public void tearDown() {
        sensey = null;
        shadowSensorManager = null;
    }

    @Test
    public void detectListenerWithStartShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartShakeDetectionWithCustomThreshold() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(4, fakeListener);
        ShakeDetector detector = getDetector(ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        sensey.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake", shadowSensorManager.hasListener(detector.sensorEventListener));
            sensey.stopShakeDetection();
            assertFalse("There should be no more sensor event listener in sensor manager", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(fakeListener);
        LightDetector detector = getDetector(LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetectionWithCustomThreshold() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(4, fakeListener);
        LightDetector detector = getDetector(LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        sensey.startLightDetection(fakeListener);
        LightDetector detector = getDetector(LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light", shadowSensorManager.hasListener(detector.sensorEventListener));
            sensey.stopLightDetection();
            assertFalse("There should be no more sensor event listener in sensor manager", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        sensey.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        sensey.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip", shadowSensorManager.hasListener(detector.sensorEventListener));
            sensey.stopFlipDetection();
            assertFalse("There should be no more sensor event listener in sensor manager", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        sensey.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        sensey.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation", shadowSensorManager.hasListener(detector.sensorEventListener));
            sensey.stopOrientationDetection();
            assertFalse("There should be no more sensor event listener in sensor manager", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        sensey.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        sensey.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity", shadowSensorManager.hasListener(detector.sensorEventListener));
            sensey.stopProximityDetection();
            assertFalse("There should be no more sensor event listener in sensor manager", shadowSensorManager.hasListener(detector.sensorEventListener));
        } else {
            fail("There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    private void addSensor(int type) {
        shadowSensorManager.addSensor(type, mock(Sensor.class));
    }

    //Hardcode because of can not get appropriate detector from Sensey.class
    private <T> T getDetector(Class<T> aClass) {
        String fieldName = getFieldName(aClass);
        if (fieldName != null) {
            return getDetector(fieldName);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getDetector(String fieldName) {
        T result = null;

        try {
            Field field = sensey.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = (T) field.get(sensey);
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
        } else {
            return null;
        }
    }
}
