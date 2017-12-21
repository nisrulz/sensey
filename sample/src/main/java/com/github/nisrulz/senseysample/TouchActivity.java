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
import android.view.ScaleGestureDetector;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.github.nisrulz.sensey.PinchScaleDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

/**
 * The type Touch activity.
 */
public class TouchActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    private static final String LOGTAG = "TouchActivity";

    private static final boolean DEBUG = true;

    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        // Init Sensey
        Sensey.getInstance().init(this);

        txtResult = (TextView) findViewById(R.id.textView_result);

        SwitchCompat swt6 = (SwitchCompat) findViewById(R.id.Switch6);
        swt6.setOnCheckedChangeListener(this);
        swt6.setChecked(false);

        SwitchCompat swt7 = (SwitchCompat) findViewById(R.id.Switch7);
        swt7.setOnCheckedChangeListener(this);
        swt7.setChecked(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop Detections
        Sensey.getInstance().stopTouchTypeDetection();
        Sensey.getInstance().stopPinchScaleDetection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // *** IMPORTANT ***
        // Stop Sensey and release the context held by it
        Sensey.getInstance().stop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onCheckedChanged(final CompoundButton switchbtn, boolean isChecked) {
        switch (switchbtn.getId()) {
            case R.id.Switch6:
                if (isChecked) {
                    startTouchTypeDetection();
                } else {
                    Sensey.getInstance().stopTouchTypeDetection();
                }
                break;
            case R.id.Switch7:
                if (isChecked) {
                    startPinchDetection();
                } else {
                    Sensey.getInstance().stopPinchScaleDetection();
                }
                break;

            default:
                // Do nothing
                break;
        }
    }

    private void resetResultInView(final TextView txt) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt.setText(getString(R.string.results_show_here));
            }
        }, 3000);
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

    private void startPinchDetection() {
        Sensey.getInstance()
                .startPinchScaleDetection(TouchActivity.this, new PinchScaleDetector.PinchScaleListener() {
                    @Override
                    public void onScale(ScaleGestureDetector scaleGestureDetector, boolean isScalingOut) {
                        if (isScalingOut) {
                            setResultTextView("Scaling Out");
                        } else {
                            setResultTextView("Scaling In");
                        }
                    }

                    @Override
                    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                        setResultTextView("Scaling : Stopped");
                    }

                    @Override
                    public void onScaleStart(ScaleGestureDetector scaleGestureDetector) {
                        setResultTextView("Scaling : Started");
                    }
                });
    }

    private void startTouchTypeDetection() {
        Sensey.getInstance()
                .startTouchTypeDetection(TouchActivity.this, new TouchTypeDetector.TouchTypListener() {
                    @Override
                    public void onDoubleTap() {
                        setResultTextView("Double Tap");
                    }

                    @Override
                    public void onLongPress() {
                        setResultTextView("Long press");
                    }

                    @Override
                    public void onScroll(int scrollDirection) {
                        switch (scrollDirection) {
                            case TouchTypeDetector.SCROLL_DIR_UP:
                                setResultTextView("Scrolling Up");
                                break;
                            case TouchTypeDetector.SCROLL_DIR_DOWN:
                                setResultTextView("Scrolling Down");
                                break;
                            case TouchTypeDetector.SCROLL_DIR_LEFT:
                                setResultTextView("Scrolling Left");
                                break;
                            case TouchTypeDetector.SCROLL_DIR_RIGHT:
                                setResultTextView("Scrolling Right");
                                break;
                            default:
                                // Do nothing
                                break;
                        }
                    }

                    @Override
                    public void onSingleTap() {
                        setResultTextView("Single Tap");
                    }

                    @Override
                    public void onSwipe(int swipeDirection) {
                        switch (swipeDirection) {
                            case TouchTypeDetector.SWIPE_DIR_UP:
                                setResultTextView("Swipe Up");
                                break;
                            case TouchTypeDetector.SWIPE_DIR_DOWN:
                                setResultTextView("Swipe Down");
                                break;
                            case TouchTypeDetector.SWIPE_DIR_LEFT:
                                setResultTextView("Swipe Left");
                                break;
                            case TouchTypeDetector.SWIPE_DIR_RIGHT:
                                setResultTextView("Swipe Right");
                                break;
                            default:
                                //do nothing
                                break;
                        }
                    }

                    @Override
                    public void onThreeFingerSingleTap() {
                        setResultTextView("Three Finger Tap");
                    }

                    @Override
                    public void onTwoFingerSingleTap() {
                        setResultTextView("Two Finger Tap");
                    }
                });
    }
}
