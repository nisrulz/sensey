package in.excogitation.lib.sensey;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class FlipDetector {

    private FlipListener flipListener;


    void init(FlipListener flipListener) {
        this.flipListener = flipListener;
    }


    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float z = sensorEvent.values[2];
            if (z > 9 && z < 10) {
                flipListener.onFaceUp();
            } else if (z > -10 && z < -9) {
                flipListener.onFaceDown();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public interface FlipListener {
        void onFaceUp();

        void onFaceDown();
    }
}
