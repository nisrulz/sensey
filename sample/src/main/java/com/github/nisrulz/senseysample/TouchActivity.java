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
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

public class TouchActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener {

  private final String LOGTAG = getClass().getSimpleName();
  private final boolean DEBUG = true;

  private SwitchCompat swt6;
  private TextView txt_result;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_touch);

    // Init Sensey
    Sensey.getInstance().init(TouchActivity.this);

    txt_result = (TextView) findViewById(R.id.textView_result);

    swt6 = (SwitchCompat) findViewById(R.id.Switch6);
    swt6.setOnCheckedChangeListener(this);
    swt6.setChecked(false);
  }

  @Override public void onCheckedChanged(CompoundButton switchbtn, boolean isChecked) {
    switch (switchbtn.getId()) {

      case R.id.Switch6:
        if (isChecked) {
          Sensey.getInstance().startTouchTypeDetection(new TouchTypeDetector.TouchTypListener() {
            @Override public void onDoubleTap() {
              setResultTextView("Double Tap");
            }

            @Override public void onScroll(boolean scrollingTop) {

              if (scrollingTop) {
                setResultTextView("Scrolling Top");
              } else {
                setResultTextView("Scrolling Down");
              }
            }

            @Override public void onSingleTap() {
              setResultTextView("Single Tap");
            }

            @Override public void onSwipeLeft() {
              setResultTextView("Swipe Left");
            }

            @Override public void onSwipeRight() {
              setResultTextView("Swipe Right");
            }

            @Override public void onLongPress() {
              setResultTextView("Long press");
            }
          });
        } else {
          Sensey.getInstance().stopTouchTypeDetection();
        }
        break;
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    // Setup onTouchEvent for detecting type of touch gesture
    Sensey.getInstance().setupDispatchTouchEvent(event);
    return super.dispatchTouchEvent(event);
  }

  @Override protected void onPause() {
    super.onPause();

    Sensey.getInstance().stopTouchTypeDetection();
  }

  private void setResultTextView(String text) {
    if (txt_result != null) {
      txt_result.setText(text);
      resetResultInView(txt_result);
      if (DEBUG) Log.i(LOGTAG, text);
    }
  }

  private void resetResultInView(final TextView txt) {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        txt.setText("..Results show here...");
      }
    }, 3000);
  }
}
