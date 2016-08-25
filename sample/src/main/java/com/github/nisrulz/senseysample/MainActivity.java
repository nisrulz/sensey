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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.LightDetector;
import com.github.nisrulz.sensey.OrientationDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

public class MainActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener, ShakeDetector.ShakeListener,
    FlipDetector.FlipListener, LightDetector.LightListener, OrientationDetector.OrientationListener,
    ProximityDetector.ProximityListener {

  private static final String LOGTAG = "MainActivity";
  private static final boolean DEBUG = true;

  private SwitchCompat swt1;
  private SwitchCompat swt2;
  private SwitchCompat swt3;
  private SwitchCompat swt4;
  private SwitchCompat swt5;
  private TextView txtResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Init Sensey
    Sensey.getInstance().init(MainActivity.this);

    txtResult = (TextView) findViewById(R.id.textView_result);

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

    Button btnTouchEvent = (Button) findViewById(R.id.btn_touchevent);
    btnTouchEvent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, TouchActivity.class));
      }
    });
  }

  @Override
  public void onCheckedChanged(CompoundButton switchbtn, boolean isChecked) {
    switch (switchbtn.getId()) {

      case R.id.Switch1:
        if (isChecked) {
          Sensey.getInstance().startShakeDetection(10, this);
        }
        else {
          Sensey.getInstance().stopShakeDetection(this);
        }
        break;
      case R.id.Switch2:
        if (isChecked) {
          Sensey.getInstance().startFlipDetection(this);
        }
        else {
          Sensey.getInstance().stopFlipDetection(this);
        }

        break;
      case R.id.Switch3:
        if (isChecked) {
          Sensey.getInstance().startOrientationDetection(this);
        }
        else {
          Sensey.getInstance().stopOrientationDetection(this);
        }

        break;
      case R.id.Switch4:
        if (isChecked) {
          Sensey.getInstance().startProximityDetection(this);
        }
        else {
          Sensey.getInstance().stopProximityDetection(this);
        }
        break;
      case R.id.Switch5:
        if (isChecked) {
          Sensey.getInstance().startLightDetection(10, this);
        }
        else {
          Sensey.getInstance().stopLightDetection(this);
        }
        break;

      default:
        // Do nothing
        break;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Stop Gesture Detections
    Sensey.getInstance().stopShakeDetection(this);
    Sensey.getInstance().stopFlipDetection(this);
    Sensey.getInstance().stopOrientationDetection(this);
    Sensey.getInstance().stopProximityDetection(this);
    Sensey.getInstance().stopLightDetection(this);
  }

  @Override
  public void onFaceUp() {
    setResultTextView("Face UP");
  }

  private void setResultTextView(String text) {
    if (txtResult != null) {
      txtResult.setText(text);
      resetResultInView(txtResult);
      if (DEBUG) {
        Log.i(LOGTAG, text);
      }
    }
  }

  private void resetResultInView(final TextView txt) {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        txt.setText("..Results show here...");
      }
    }, 3000);
  }

  @Override
  public void onFaceDown() {
    setResultTextView("Face Down");
  }

  @Override
  public void onDark() {
    setResultTextView("Dark");
  }

  @Override
  public void onLight() {
    setResultTextView("Not Dark");
  }

  @Override
  public void onTopSideUp() {
    setResultTextView("Topside UP");
  }

  @Override
  public void onBottomSideUp() {
    setResultTextView("Bottomside UP");
  }

  @Override
  public void onRightSideUp() {
    setResultTextView("Rightside UP");
  }

  @Override
  public void onLeftSideUp() {
    setResultTextView("Leftside UP");
  }

  @Override
  public void onNear() {
    setResultTextView("Near");
  }

  @Override
  public void onFar() {
    setResultTextView("Far");
  }

  @Override
  public void onShakeDetected() {
    setResultTextView("Shake Detected!");
  }
}
