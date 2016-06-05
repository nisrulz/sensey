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

package com.github.nisrulz.senseysample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.LightDetector;
import com.github.nisrulz.sensey.OrientationDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

public class MainActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener {

  private final String LOGTAG = getClass().getSimpleName().toString();
  private boolean DEBUG = false;

  private SwitchCompat swt1, swt2, swt3, swt4, swt5;
  private TextView txt_result;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Init Sensey
    Sensey.getInstance().init(MainActivity.this);

    txt_result = (TextView) findViewById(R.id.textView_result);

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

    swt5 = (SwitchCompat) findViewById(R.id.Switch5);
    swt5.setOnCheckedChangeListener(this);
    swt5.setChecked(false);
  }

  void resetResultInView(final TextView txt) {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        txt.setText("..Results show here...");
      }
    }, 2000);
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch (buttonView.getId()) {

      case R.id.Switch1:
        if (isChecked) {
          Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
            @Override public void onShakeDetected() {
              txt_result.setText("Shake Detected!");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "Shake Detected!");
            }
          });
        } else {
          Sensey.getInstance().stopShakeDetection();
        }
        break;
      case R.id.Switch2:
        if (isChecked) {
          Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
            @Override public void onFaceUp() {
              txt_result.setText("FaceUp");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "FaceUp");
            }

            @Override public void onFaceDown() {
              txt_result.setText("FaceDown");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "FaceDown");
            }
          });
        } else {
          Sensey.getInstance().stopFlipDetection();
        }

        break;
      case R.id.Switch3:
        if (isChecked) {
          Sensey.getInstance()
              .startOrientationDetection(new OrientationDetector.OrientationListener() {
                @Override public void onTopSideUp() {
                  txt_result.setText("Top Side Up");
                  resetResultInView(txt_result);
                  if (DEBUG) Log.i(LOGTAG, "Top Side Up");
                }

                @Override public void onBottomSideUp() {
                  txt_result.setText("Bottom Side Up");
                  resetResultInView(txt_result);
                  if (DEBUG) Log.d(LOGTAG, "Bottom Side Up");
                }

                @Override public void onRightSideUp() {
                  txt_result.setText("Right Side Up");
                  resetResultInView(txt_result);
                  if (DEBUG) Log.i(LOGTAG, "Right Side Up");
                }

                @Override public void onLeftSideUp() {
                  txt_result.setText("Left Side Up");
                  resetResultInView(txt_result);
                  if (DEBUG) Log.i(LOGTAG, "Left Side Up");
                }
              });
        } else {
          Sensey.getInstance().stopOrientationDetection();
        }

        break;
      case R.id.Switch4:
        if (isChecked) {
          Sensey.getInstance().startProximityDetection(new ProximityDetector.ProximityListener() {
            @Override public void onNear() {
              txt_result.setText("Near");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "Near");
            }

            @Override public void onFar() {
              txt_result.setText("Far");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "Far");
            }
          });
        } else {
          Sensey.getInstance().stopProximityDetection();
        }
        break;
      case R.id.Switch5:
        if (isChecked) {
          Sensey.getInstance().startLightetection(new LightDetector.LightListener() {
            @Override public void onDark() {
              txt_result.setText("Dark");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "Dark");
            }

            @Override public void onLight() {
              txt_result.setText("Light");
              resetResultInView(txt_result);
              if (DEBUG) Log.i(LOGTAG, "Light");
            }
          });
        } else {
          Sensey.getInstance().stopLightDetection();
        }
        break;
    }
  }

  @Override protected void onPause() {
    super.onPause();
    // Stop Gesture Detections
    Sensey.getInstance().stopShakeDetection();
    Sensey.getInstance().stopFlipDetection();
    Sensey.getInstance().stopOrientationDetection();
    Sensey.getInstance().stopProximityDetection();
    Sensey.getInstance().stopLightDetection();
  }

  @Override protected void onResume() {
    super.onResume();
    // TODO: Start Gesture Detections
  }
}
