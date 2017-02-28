![Image](/img/github_banner.png)

### Specs
[ ![Download](https://api.bintray.com/packages/nisrulz/maven/com.github.nisrulz%3Asensey/images/download.svg) ](https://bintray.com/nisrulz/maven/com.github.nisrulz%3Asensey/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=9) <a href="http://www.methodscount.com/?lib=com.github.nisrulz%3Asensey%3A1.6.1"><img src="https://img.shields.io/badge/Methods and size-core: 209 | deps: 6596 | 24 KB-e91e63.svg"/></a>

### Badges/Featured In
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sensey-green.svg?style=true)](https://android-arsenal.com/details/1/3550) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23209-blue.svg)](http://androidweekly.net/issues/issue-209) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23245-blue.svg)](http://androidweekly.net/issues/issue-245) [![AndroidSweets](https://img.shields.io/badge/AndroidSweets-%2320-ff69b4.svg)](https://androidsweets.ongoodbits.com/2016/05/26/issue-20) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23100-blue.svg)](https://www.androiddevdigest.com/digest-100/) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23131-blue.svg)](https://www.androiddevdigest.com/digest-131/) [![awesome-android](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://snowdream.github.io/awesome-android/Other.html#Gesture)

**Also included in**
+ [Blog Post: I could not find a simple Gesture Detection android library, so I built one](https://android.jlelse.eu/i-could-not-find-a-simple-gesture-detection-android-library-so-i-built-one-334c0a307c16#.1us4zgise)
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

![sc1](https://github.com/nisrulz/sensey/blob/master/img/sc1.png) ![sc2](https://github.com/nisrulz/sensey/blob/master/img/sc2.png)

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

# Simple example

+ To initialize Sensey under your `onCreate()`` in the activity/service, call
```java
Sensey.getInstance().init(context);
```

+ To stop Sensey, under your `onDestroy()` in the activity/service, call
```java
 // *** IMPORTANT ***
 // Stop Sensey and release the context held by it
 Sensey.getInstance().stop();
```

+ Next to enable shake detection 
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

### <center> :page_with_curl: For more info , check the **[Wiki Docs](https://github.com/nisrulz/sensey/wiki/Usage)** </center>

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

