package in.excogitation.lib.sensey;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ProximityDetector {


    private ProximityListener proximityListener;


    float max;

    void init(ProximityListener proximityListener) {
        this.proximityListener = proximityListener;
    }


    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float distance = sensorEvent.values[0];

            if (distance < max) {
                proximityListener.onNear();
            } else {
                proximityListener.onFar();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    public interface ProximityListener {
        void onNear();

        void onFar();
    }
}
