package in.excogitation.sensey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import in.excogitation.lib.sensey.FlipDetector;
import in.excogitation.lib.sensey.OrientationDetector;
import in.excogitation.lib.sensey.ProximityDetector;
import in.excogitation.lib.sensey.Sensey;
import in.excogitation.lib.sensey.ShakeDetector;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private final String LOGTAG = getClass().getSimpleName().toString();

    private SwitchCompat swt1, swt2, swt3, swt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Sensey
        Sensey.getInstance().init(MainActivity.this);

        swt1 = (SwitchCompat) findViewById(R.id.Switch1);
        swt1.setOnCheckedChangeListener(this);
        swt1.setChecked(false);

        swt2 = (SwitchCompat) findViewById(R.id.Switch2);
        swt2.setOnCheckedChangeListener(this);
        swt2.setChecked(false);

        swt3 = (SwitchCompat) findViewById(R.id.Switch3);
        swt3.setOnCheckedChangeListener(this);
        swt3.setChecked(false);

        swt4 = (SwitchCompat) findViewById(R.id.Switch4);
        swt4.setOnCheckedChangeListener(this);
        swt4.setChecked(false);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.Switch1:
                if (isChecked) {
                    Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
                        @Override
                        public void onShakeDetected() {
                            Toast.makeText(MainActivity.this, "Shake Detected !", Toast.LENGTH_SHORT).show();
                            Log.i(LOGTAG, "Shake Detected!");
                        }
                    });
                } else {
                    Sensey.getInstance().stopShakeDetection();
                }
                break;
            case R.id.Switch2:
                if (isChecked) {
                    Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
                        @Override
                        public void onFaceUp() {
                            Log.i(LOGTAG, "FaceUp");
                        }

                        @Override
                        public void onFaceDown() {
                            Log.i(LOGTAG, "FaceDown");
                        }
                    });
                } else {
                    Sensey.getInstance().stopFlipDetection();
                }

                break;
            case R.id.Switch3:
                if (isChecked) {
                    Sensey.getInstance().startOrientationDetection(new OrientationDetector.OrientationListener() {
                        @Override
                        public void onTopSideUp() {
                            Log.i(LOGTAG, "Top Up");
                        }

                        @Override
                        public void onBottomSideUp() {
                            Log.d(LOGTAG, "Bottom Up");
                        }

                        @Override
                        public void onRightSideUp() {
                            Log.i(LOGTAG, "Right Up");
                        }

                        @Override
                        public void onLeftSideUp() {
                            Log.i(LOGTAG, "Left Up");
                        }
                    });
                } else {
                    Sensey.getInstance().stopOrientationDetection();
                }

                break;
            case R.id.Switch4:
                if (isChecked) {
                    Sensey.getInstance().startProximityDetection(new ProximityDetector.ProximityListener() {
                        @Override
                        public void onNear() {
                            Log.i(LOGTAG, "Near");
                        }

                        @Override
                        public void onFar() {
                            Log.i(LOGTAG, "Far");
                        }
                    });
                } else {
                    Sensey.getInstance().stopProximityDetector();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop Gesture Detections
        Sensey.getInstance().stopShakeDetection();
        Sensey.getInstance().stopFlipDetection();
        Sensey.getInstance().stopOrientationDetection();
        Sensey.getInstance().stopProximityDetector();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Start Gesture Detections
    }
}
