package in.excogitation.lib.sensey;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class Sensey {


    private SensorManager sensorManager;

    private ShakeDetector shakeDetector;
    private FlipDetector flipDetector;
    private OrientationDetector orientationDetector;
    private ProximityDetector proximityDetector;

    private Sensey() {
    }

    private static class LazyHolder {
        private static final Sensey INSTANCE = new Sensey();
    }

    public static Sensey getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void init(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void startShakeDetection(ShakeDetector.ShakeListener shakeListener) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            shakeDetector = new ShakeDetector();
            sensorManager.registerListener(shakeDetector.sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            shakeDetector.init(shakeListener);
        }
    }

    public void stopShakeDetection() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null && shakeDetector != null)
            sensorManager.unregisterListener(shakeDetector.sensorEventListener);
    }

    public void startFlipDetection(FlipDetector.FlipListener flipListener) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            flipDetector = new FlipDetector();
            sensorManager.registerListener(flipDetector.sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            flipDetector.init(flipListener);
        }
    }

    public void stopFlipDetection() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null && flipDetector != null)
            sensorManager.unregisterListener(flipDetector.sensorEventListener);
    }


    public void startOrientationDetection(OrientationDetector.OrientationListener orientationListener) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
            orientationDetector = new OrientationDetector();

            sensorManager.registerListener(orientationDetector.sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_NORMAL);

            orientationDetector.init(orientationListener);
        }
    }

    public void stopOrientationDetection() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null && orientationDetector != null)
            sensorManager.unregisterListener(orientationDetector.sensorEventListener);
    }

    public void startProximityDetection(ProximityDetector.ProximityListener proximityListener) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {

            proximityDetector = new ProximityDetector();
            proximityDetector.max = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY).getMaximumRange();

            sensorManager.registerListener(proximityDetector.sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL);

            proximityDetector.init(proximityListener);
        }
    }


    public void stopProximityDetection() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null && proximityDetector != null) {
            sensorManager.unregisterListener(proximityDetector.sensorEventListener);
        }
    }
}
