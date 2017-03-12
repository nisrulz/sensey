### Specs
[ ![Download](https://api.bintray.com/packages/nisrulz/maven/com.github.nisrulz%3Asensey/images/download.svg) ](https://bintray.com/nisrulz/maven/com.github.nisrulz%3Asensey/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=9) <a href="http://www.methodscount.com/?lib=com.github.nisrulz%3Asensey%3A1.7.0"><img src="https://img.shields.io/badge/Methods and size-core: 227 | deps: 6596 | 26 KB-e91e63.svg"/></a>

### Badges/Featured In
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sensey-green.svg?style=true)](https://android-arsenal.com/details/1/3550) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23209-blue.svg)](http://androidweekly.net/issues/issue-209) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23245-blue.svg)](http://androidweekly.net/issues/issue-245) [![AndroidSweets](https://img.shields.io/badge/AndroidSweets-%2320-ff69b4.svg)](https://androidsweets.ongoodbits.com/2016/05/26/issue-20) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23100-blue.svg)](https://www.androiddevdigest.com/digest-100/) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23131-blue.svg)](https://www.androiddevdigest.com/digest-131/) [![awesome-android](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://snowdream.github.io/awesome-android/Other.html#Gesture)

**Also included in**
+ [Best Android Libraries for Developers](https://cloudrail.com/best-android-libraries-for-developers/)

+ [COBE’s Top 5 Android Libraries — September 2016](https://medium.cobeisfresh.com/cobes-top-5-android-libraries-september-2016-883757e61bf0#.oe2lzaxyn)

+ [DZone Post](https://dzone.com/articles/this-week-in-mobile-may-15)

+ [Codepath's Must Have Libraries](https://github.com/codepath/android_guides/wiki/Must-Have-libraries#convenience)

+ Awesome Android Newsletter

	+ [Issue 5](https://android.libhunt.com/newsletter/5)
	
	+ [Issue 21](https://android.libhunt.com/newsletter/21)
	
+ [Changelog Weekly Issue #115](http://email.changelog.com/t/t-310383437622D164)

### Show some :heart:
[![GitHub stars](https://img.shields.io/github/stars/nisrulz/sensey.svg?style=social&label=Star)](https://github.com/nisrulz/sensey) [![GitHub forks](https://img.shields.io/github/forks/nisrulz/sensey.svg?style=social&label=Fork)](https://github.com/nisrulz/sensey/fork) [![GitHub watchers](https://img.shields.io/github/watchers/nisrulz/sensey.svg?style=social&label=Watch)](https://github.com/nisrulz/sensey) [![GitHub followers](https://img.shields.io/github/followers/nisrulz.svg?style=social&label=Follow)](https://github.com/nisrulz/sensey)  
[![Twitter Follow](https://img.shields.io/twitter/follow/nisrulz.svg?style=social)](https://twitter.com/nisrulz) 

Android library which makes detecting gestures a breeze.

The library is built for simplicity and ease of use. It eliminates most boilerplate code for dealing with setting up gesture detection on Android.

Go build awesome stuff with it!  :smile:

# Changelog

Starting with `1.0.1`, Changes exist in the [releases tab](https://github.com/nisrulz/sensey/releases).

# Supported gestures

1. [Flip](https://github.com/nisrulz/sensey/wiki/Usage#flip)
    + onFaceUp
    + onFaceDown
 1. [Light](https://github.com/nisrulz/sensey/wiki/Usage#light)
    + onDark
    + onLight
 1. [Orientation](https://github.com/nisrulz/sensey/wiki/Usage#orientation)
    + onTopSideUp
    + onBottomSideUp
    + onLeftSideUp
    + onRightSideUp
 1. [PinchScale](https://github.com/nisrulz/sensey/wiki/Usage#pinchscale)
    + OnScale
    + OnScaleStart
    + OnScaleEnd
 1. [Proximity](https://github.com/nisrulz/sensey/wiki/Usage#proximity)
    + onNear
    + onFar
 1. [Shake](https://github.com/nisrulz/sensey/wiki/Usage#shake)
 1. [Wave](https://github.com/nisrulz/sensey/wiki/Usage#wave)
 1. [Chop](https://github.com/nisrulz/sensey/wiki/Usage#chop)
 1. [WristTwist](https://github.com/nisrulz/sensey/wiki/Usage#wristtwist)
 1. [Movement](https://github.com/nisrulz/sensey/wiki/Usage#movement)
 1. [SoundLevel](https://github.com/nisrulz/sensey/wiki/Usage#soundlevel)
 1. [RotationAngle](https://github.com/nisrulz/sensey/wiki/Usage#rotationangle)
 1. [TiltDirection](https://github.com/nisrulz/sensey/wiki/Usage#tiltdirection)
 1. [TouchType](https://github.com/nisrulz/sensey/wiki/Usage#touchtype)
    + onDoubleTap
    + onScroll(direction)
    + onSingleTap
    + onSwipeLeft
    + onSwipeRight
    + onLongPress
    + onTwoFingerSingleTap
    + onThreeFingerSingleTap
    
# Including in your project
Sensey is available in the Jcenter, so getting it as simple as adding it as a dependency

```gradle
compile 'com.github.nisrulz:sensey:{latest version}'
```

where `{latest version}` corresponds to published version in [ ![Download](https://api.bintray.com/packages/nisrulz/maven/com.github.nisrulz%3Asensey/images/download.svg) ](https://bintray.com/nisrulz/maven/com.github.nisrulz%3Asensey/_latestVersion)


## Setup

### Initialize Sensey,under your `onCreate()` in the activity/service, call

```java
Sensey.getInstance().init(context);
```

If you wish to alter the sampling period of data by Sensey, use the below `init` function 

```java
Sensey.getInstance().init(context,samplingPeriod)
```

where `samplingPeriod` can be anyone from the below options
  + `Sensey.SAMPLING_PERIOD_FASTEST`
  + `Sensey. SAMPLING_PERIOD_GAME`  
  + `Sensey. SAMPLING_PERIOD_NORMAL` // Default
  + `Sensey. SAMPLING_PERIOD_UI`


### To stop Sensey, under your `onDestroy()` in the activity/service, call

```java
 // *** IMPORTANT ***
 // Stop Sensey and release the context held by it
 Sensey.getInstance().stop();
```

## Next to enable detection for

### Shake
+ Create an instance of ShakeListener

```java
ShakeDetector.ShakeListener shakeListener=new ShakeDetector.ShakeListener() {
   @Override public void onShakeDetected() {
       // Shake detected, do something
   }
   
   @Override public void onShakeStopped() {
       // Shake stopped, do something
   }

};
```

+ Now to start listening for Shake gesture, pass the instance `shakeListener` to `startShakeDetection()` function

```java
Sensey.getInstance().startShakeDetection(shakeListener);
```

If you want to modify the `threshold` and `time` before declaring that shake gesture is stopped, use

```java
Sensey.getInstance().startShakeDetection(threshold,timeBeforeDeclaringShakeStopped,shakeListener);
```

+ To stop listening for Shake gesture, pass the instance `shakeListener` to `stopShakeDetection()` function

```java
Sensey.getInstance().stopShakeDetection(shakeListener);
```

### Movement

+ Create an instance of MovementListener

```java
MovementDetector.MovementListener movementListener=new MovementDetector.MovementListener() {
   @Override public void onMovement() {
       // Movement detected, do something
   }
   
   @Override public void onStationary() {
       // Movement stopped, do something
   }

};
```

+ Now to start listening for Movement gesture, pass the instance `movementListener` to `startMovementDetection()` function

```java
Sensey.getInstance().startMovementDetection(movementListener);
```

If you want to modify the `threshold` and `time` before declaring that movement gesture is stopped, use

```java
Sensey.getInstance().startMovementDetection(threshold,timeBeforeDeclaringStationary,movementListener);
```

+ To stop listening for Movement gesture, pass the instance `movementListener` to `stopMovementDetection()` function

```java
Sensey.getInstance().stopMovementDetection(movementListener);
```

### Chop

+ Create an instance of ChopListener

```java
ChopDetector.ChopListener chopListener=new ChopDetector.ChopListener() {
   @Override public void onChop() {
       // Chop gesture detected, do something
   }
};
```

+ Now to start listening for Chop gesture, pass the instance `chopListener` to `startChopDetection()` function

```java
Sensey.getInstance().startChopDetection(chopListener);
```

If you want to modify the `threshold` and `time` for chop gesture, use

```java
Sensey.getInstance().startChopDetection(threshold,timeForChopGesture,chopListener);
```

+ To stop listening for Chop gesture, pass the instance `chopListener` to `stopChopDetection()` function

```java
Sensey.getInstance().stopChopDetection(chopListener);
```

### WristTwist

+ Create an instance of WristTwistListener

```java
WristTwistDetector.WristTwistListener wristTwistListener=new WristTwistDetector.WristTwistListener() {
   @Override public void onWristTwist() {
       // Wrist Twist gesture detected, do something
   }
};
```

+ Now to start listening for Wrist Twist gesture, pass the instance `wristTwistListener` to `startWristTwistDetection()` function

```java
Sensey.getInstance().startWristTwistDetection(wristTwistListener);
```

If you want to modify the `threshold` and `time` for wrist twist gesture, use

```java
Sensey.getInstance().startWristTwistDetection(threshold,timeForWristTwistGesture,wristTwistListener);
```

+ To stop listening for Wrist Twist gesture, pass the instance `wristTwistListener` to `stopWristTwistDetection()` function

```java
Sensey.getInstance().stopWristTwistDetection(wristTwistListener);
```

### SoundLevel

+ Create an instance of SoundLevelListener

```java
SoundLevelDetector.SoundLevelListener soundLevelListener=new SoundLevelDetector.SoundLevelListener() {
   @Override public void onSoundDetected(float level) {
       //Sound Level in dB is detected, do something
   }
};
```

+ Now to start listening for Sound Level, pass the instance `soundLevelListener` to `startSoundLevelDetection()` function

```java
Sensey.getInstance().startSoundLevelDetection(soundLevelListener);
```

+ To stop listening for Sound Level, pass the instance `soundLevelListener` to `stopSoundLevelDetection()` function

```java
Sensey.getInstance().stopSoundLevelDetection(soundLevelListener);
```

### Flip

+ Create an instance of FlipListener

```java
FlipDetector.FlipListener flipListener=new FlipDetector.FlipListener() {
    @Override public void onFaceUp() {
       // Device Facing up
    }

    @Override public void onFaceDown() {
      // Device Facing down
    }
};
```

+ Now to start listening for Flip gesture, pass the instance `flipListener` to `startFlipDetection()` function

```java
Sensey.getInstance().startFlipDetection(flipListener);
```

+ To stop listening for Flip gesture, pass the instance `flipListener` to `stopFlipDetection()` function

```java
Sensey.getInstance().stopFlipDetection(flipListener);
```


### RotationAngle

+ Create an instance of RotationAngleListener

```java
RotationAngleDetector.RotationAngleListener rotationAngleListener =new RotationAngleDetector.RotationAngleListener() {
    @Override
    public void onRotation(float angleInAxisX, float angleInAxisY, float angleInAxisZ) {
      // Do something with the angles, values are in degrees
  }
};
```

+ Now to start listening for RotationAngle gesture, pass the instance `rotationAngleListener` to `startRotationAngleDetection()` function

```java
Sensey.getInstance().startRotationAngleDetection(rotationAngleListener);
```

+ To stop listening for RotationAngle gesture, pass the instance `rotationAngleListener` to `stopRotationAngleDetection()` function

```java
Sensey.getInstance().stopRotationAngleDetection(rotationAngleListener);
```

### TiltDirection

+ Create an instance of TiltDirectionListener

```java
TiltDirectionDetector.TiltDirectionListener tiltDirectionListener=new TiltDirectionDetector.TiltDirectionListener() {
  @Override
  public void onTiltInAxisX(int direction) {
    // Do something with tilt direction on x-axis
  }

  @Override
  public void onTiltInAxisY(int direction) {
    // Do something with tilt direction on y-axis
  }

  @Override
  public void onTiltInAxisZ(int direction) {
    // Do something with tilt direction on z-axis
  }
};
```

where `direction` can have either of the value:`TiltDirectionDetector.DIRECTION_CLOCKWISE` or `TiltDirectionDetector.DIRECTION_ANTICLOCKWISE`

+ Now to start listening for TiltDirection gesture, pass the instance `TiltDirectionListener` to `startTiltDirectionDetection()` function

```java
Sensey.getInstance().startTiltDirectionDetection(TiltDirectionListener);
```

+ To stop listening for TiltDirection gesture, pass the instance `TiltDirectionListener` to `stopTiltDirectionDetection()` function

```java
Sensey.getInstance().stopTiltDirectionDetection(TiltDirectionListener);
```

### Orientation

+ Create an instance of OrientationListener

```java
OrientationDetector.OrientationListener orientationListener=new OrientationDetector.OrientationListener() {
  @Override public void onTopSideUp() {
     // Top side of device is up
  }

  @Override public void onBottomSideUp() {
     // Bottom side of device is up
  }

  @Override public void onRightSideUp() {
     // Right side of device is up
  }

  @Override public void onLeftSideUp() {
     // Left side of device is up       
  }
};
```

+ Now to start listening for Orientation gesture, pass the instance `orientationListener` to `startOrientationDetection()` function

```java
Sensey.getInstance().startOrientationDetection(orientationListener);
```

+ To stop listening for Orientation gesture, pass the instance `orientationListener` to `stopOrientationDetection()` function

```java
Sensey.getInstance().stopOrientationDetection(orientationListener);
```

### Proximity

+ Create an instance of ProximityListener

```java
ProximityDetector.ProximityListener proximityListener=new ProximityDetector.ProximityListener() {
   @Override public void onNear() {
        // Near to device
   }

   @Override public void onFar() {
        // Far from device
   }
};
```

+ Now to start listening for Orientation gesture, pass the instance `proximityListener` to `startProximityDetection()` function

```java
Sensey.getInstance().startProximityDetection(proximityListener);
```

+ To stop listening for Orientation gesture, pass the instance `proximityListener` to `stopProximityDetection()` function

```java
Sensey.getInstance().stopProximityDetection(proximityListener);
```

### Wave

+ Create an instance of WaveListener

```java
WaveDetector.WaveListener waveListener=new WaveDetector.WaveListener() {
   @Override public void onWave() {
        // Wave of hand gesture detected
   }
};
```

+ Now to start listening for Wave gesture, pass the instance `waveListener` to `startWaveDetection()` function

```java
Sensey.getInstance().startWaveDetection(waveListener);
```

+ To stop listening for Wave gesture, pass the instance `waveListener` to `stopWaveDetection()` function

```java
Sensey.getInstance().stopWaveDetection(waveListener);
```

### Light

+ Create an instance of LightListener

```java
LightDetector.LightListener lightListener=new LightDetector.LightListener() {
   @Override public void onDark() {
      // Dark
   }

   @Override public void onLight() {
      // Not Dark
   }
};
```

+ Now to start listening for Orientation gesture, pass the instance `lightListener` to `startLightDetection()` function

```java
Sensey.getInstance().startLightDetection(lightListener);
```

+ To stop listening for Orientation gesture, pass the instance `lightListener` to `stopLightDetection()` function

```java
Sensey.getInstance().stopLightDetection(lightListener);
```


### For Touch based gestures

***IMPORTANT*** : Implement this to intercept touch actions in activity by overriding the `dispatchTouchEvent`.

```java
 @Override public boolean dispatchTouchEvent(MotionEvent event) {
    // Setup onTouchEvent for detecting type of touch gesture
    Sensey.getInstance().setupDispatchTouchEvent(event);
    return super.dispatchTouchEvent(event);
 }
```

### PinchScale

+ Create an instance of PinchScaleListener

```java
PinchScaleDetector.PinchScaleListener pinchScaleListener=new PinchScaleDetector.PinchScaleListener() {
   @Override public void onScale(ScaleGestureDetector scaleGestureDetector, boolean isScalingOut) {
        if (isScalingOut) {
           // Scaling Out;
        } else {
          // Scaling In
        }
   }

   @Override public void onScaleStart(ScaleGestureDetector scaleGestureDetector) {
          // Scaling Started
   }

   @Override public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
          // Scaling Stopped
   }
};
```

+ Now to start listening for PinchScale gesture, pass the instance `pinchScaleListener` to `startPinchScaleDetection()` function

```java
Sensey.getInstance().startPinchScaleDetection(pinchScaleListener);
```

+ To stop listening for PinchScale gesture, simply call  `stopPinchScaleDetection()` function

```java
Sensey.getInstance().stopPinchScaleDetection();
```

> Don't forget to implement `dispatchTouchEvent()` as explained [here](https://github.com/nisrulz/sensey/wiki/Usage#for-touch-based-gestures)


### TouchType

+ Create an instance of TouchTypListener

```java
TouchTypeDetector.TouchTypListener touchTypListener=new TouchTypeDetector.TouchTypListener() {
   @Override public void onTwoFingerSingleTap() {
         // Two finger single tap
   }

   @Override public void onThreeFingerSingleTap() {
         // Three finger single tap
   }

   @Override public void onDoubleTap() {
         // Double tap
   }

   @Override public void onScroll(int scrollDirection) {
     switch (scrollDirection) {
      case TouchTypeDetector.SCROLL_DIR_UP:
        // Scrolling Up
        break;
      case TouchTypeDetector.SCROLL_DIR_DOWN:
        // Scrolling Down
        break;
      case TouchTypeDetector.SCROLL_DIR_LEFT:
        // Scrolling Left
        break;
      case TouchTypeDetector.SCROLL_DIR_RIGHT:
        // Scrolling Right
        break;
      default:
        // Do nothing
        break;
    }
   }

   @Override public void onSingleTap() {
         // Single tap
   }

   @Override public void onSwipe(int swipeDirection) {
     switch (swipeDirection) {
      case TouchTypeDetector.SWIPE_DIR_UP:
        // Swipe Up
        break;
      case TouchTypeDetector.SWIPE_DIR_DOWN:
        // Swipe Down
        break;
      case TouchTypeDetector.SWIPE_DIR_LEFT:
        // Swipe Left
        break;
      case TouchTypeDetector.SWIPE_DIR_RIGHT:
        // Swipe Right
        break;
      default:
        //do nothing
        break;
    }
   }

   @Override public void onLongPress() {
         // Long press
   }
};
```

+ Now to start listening for TouchType gesture, pass the instance `touchTypListener` to `startTouchTypeDetection()` function

```java
Sensey.getInstance().startTouchTypeDetection(touchTypListener);
```

+ To stop listening for TouchType gesture, simply call  `stopTouchTypeDetection()` function

```java
Sensey.getInstance().stopTouchTypeDetection();
```

> Don't forget to implement `dispatchTouchEvent()` as explained [here](https://github.com/nisrulz/sensey/wiki/Usage#for-touch-based-gestures)

# Pull Requests
I welcome and encourage all pull requests. It usually will take me within 24-48 hours to respond to any issue or request. Here are some basic rules to follow to ensure timely addition of your request:

  1. Match coding style (braces, spacing, etc.) This is best achieved using CMD+Option+L (Reformat code) on Mac (not sure for Windows) with Android Studio defaults.
  
  2. If its a feature, bugfix, or anything please only change code to what you specify.
  
  3. Please keep PR titles easy to read and descriptive of changes, this will make them easier to merge :)
  
  4. Pull requests _must_ be made against `develop` branch. Any other branch (unless specified by the maintainers) will get rejected.
  
  5. Check for existing [issues](https://github.com/nisrulz/sensey/issues) first, before filing an issue.
  
  6. Have fun!
  

## Apps using Sensey
If you are using Sensey in your app and would like to be listed here, please let me know by opening a [new issue](https://github.com/nisrulz/sensey/issues/new)!

+ [Push-Ups](https://play.google.com/store/apps/details?id=com.mk.push)

### Created & Maintained By
[Nishant Srivastava](https://github.com/nisrulz) ([@nisrulz](https://www.twitter.com/nisrulz))

> If you found this library helpful or you learned something from the source code and want to thank me, consider [buying me a cup of](https://www.paypal.me/nisrulz) :coffee:

License
=======

    Copyright 2016 Nishant Srivastava

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

