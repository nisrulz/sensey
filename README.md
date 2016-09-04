![Image](https://github.com/nisrulz/sensey/blob/master/img/github_banner.png)


### Specs
[ ![Jcenter](https://api.bintray.com/packages/nisrulz/maven/sensey/images/download.svg) ](https://bintray.com/nisrulz/maven/sensey/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=9) <a href="http://www.methodscount.com/?lib=com.github.nisrulz%3Asensey%3A1.4.1"><img src="https://img.shields.io/badge/Size-15 KB-e91e63.svg"/></a>

### Featured in
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sensey-green.svg?style=true)](https://android-arsenal.com/details/1/3550) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23209-blue.svg)](http://androidweekly.net/issues/issue-209) [![AndroidSweets](https://img.shields.io/badge/AndroidSweets-%2320-ff69b4.svg)](https://androidsweets.ongoodbits.com/2016/05/26/issue-20) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23100-blue.svg)](https://www.androiddevdigest.com/digest-100/) [![awesome-android](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://snowdream.github.io/awesome-android/Other.html#Gesture)

### Show some :heart:
[![GitHub stars](https://img.shields.io/github/stars/nisrulz/sensey.svg?style=social&label=Star)](https://github.com/nisrulz/sensey) [![GitHub forks](https://img.shields.io/github/forks/nisrulz/sensey.svg?style=social&label=Fork)](https://github.com/nisrulz/sensey/fork) [![GitHub watchers](https://img.shields.io/github/watchers/nisrulz/sensey.svg?style=social&label=Watch)](https://github.com/nisrulz/sensey) [![GitHub followers](https://img.shields.io/github/followers/nisrulz.svg?style=social&label=Follow)](https://github.com/nisrulz/sensey)  
[![Twitter Follow](https://img.shields.io/twitter/follow/nisrulz.svg?style=social)](https://twitter.com/nisrulz) 

Android library which makes detecting gestures a breeze.

The library is built for simplicity and ease of use. It eliminates most boilerplate code for dealing with setting up gesture detection on Android.

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
where `{latest version}` corresponds to published version in [ ![Jcenter](https://api.bintray.com/packages/nisrulz/maven/sensey/images/download.svg) ](https://bintray.com/nisrulz/maven/sensey/_latestVersion)

# Simple example

+ Initialize Sensey under your onCreate() in the activity/service
```java
Sensey.getInstance().init(context);
```

+ Next to enable shake detection 
  + Create an instance of ShakeListener
  ```java
  ShakeDetector.ShakeListener shakeListener=new ShakeDetector.ShakeListener() {
      @Override public void onShakeDetected() {
         // Shake detected, do something
     }
  };
  ```
  + Now to start listening for Shake gesture, pass the instance `shakeListener` to `startShakeDetection()` function
  ```java
  Sensey.getInstance().startShakeDetection(shakeListener);
  ```
  
  If you want to modify the `threshold` , pass an `int` as value
  ```java
  Sensey.getInstance().startShakeDetection(threshold,shakeListener);
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

### Created & Maintained By
[Nishant Srivastava](https://github.com/nisrulz) ([@nisrulz](https://www.twitter.com/nisrulz))