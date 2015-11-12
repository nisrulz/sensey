package in.excogitation.lib.sensey;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class OrientationDetector {

    private OrientationListener orientationListener;


    void init(OrientationListener orientationListener) {
        this.orientationListener = orientationListener;
    }


    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float azimuth = sensorEvent.values[0];
            float pitch = sensorEvent.values[1];
            float roll = sensorEvent.values[2];
            if (pitch < -45 && pitch > -135) {
                orientationListener.onTopSideUp();
            } else if (pitch > 45 && pitch < 135) {
                orientationListener.onBottomSideUp();
            } else if (roll > 45) {
                orientationListener.onRightSideUp();
            } else if (roll < -45) {
                orientationListener.onLeftSideUp();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public interface OrientationListener {
        void onTopSideUp();

        void onBottomSideUp();

        void onRightSideUp();

        void onLeftSideUp();
    }
}

