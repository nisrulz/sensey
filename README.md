<div align="center">
  <img src="/img/logo_color.png" height="128" />
</div>

<h1 align="center">Sensey <a href="https://twitter.com/intent/tweet?text=Sensey%3A%20Android%20library%20which%20makes%20playing%20with%20sensor%20events%20%26%20detecting%20gestures%20a%20breeze%F0%9F%98%8E&url=https://github.com/nisrulz/sensey&via=nisrulz&hashtags=AndroidDev">
        <img src="https://img.shields.io/twitter/url/http/shields.io.svg?style=social"/>
    </a></h1>

<div align="center">
  <strong>Android library which makes playing with sensor events & detecting gestures a breeze.</strong>
  <p>The library is built for simplicity and ease of use. It eliminates most boilerplate code for dealing with setting up sensor based event and gesture detection on Android.</p>
</div>
<br/>
<div align="center">
    <!-- Maven Central -->
    <a href="https://search.maven.org/artifact/com.github.nisrulz/sensey">
        <img src="https://img.shields.io/maven-central/v/com.github.nisrulz/sensey"/>
    </a>
    <!-- API -->
    <a href="https://android-arsenal.com/api?level=23">
        <img src="https://img.shields.io/badge/API-23%2B-orange.svg?style=flat"/>
    </a>
    <!-- Android Arsenal -->
    <a href="https://android-arsenal.com/details/1/3550">
        <img src="https://img.shields.io/badge/Android%20Arsenal-Sensey-green.svg?style=true"/>
    </a>
    <!-- Android Sweets -->
    <a href="https://androidsweets.ongoodbits.com/2016/05/26/issue-20">
        <img src="https://img.shields.io/badge/AndroidSweets-%2320-ff69b4.svg"/>
    </a>
    <!-- Android Dev Digest -->
    <a href="https://www.androiddevdigest.com/digest-100/">
        <img src="https://img.shields.io/badge/AndroidDev%20Digest-%23100-blue.svg"/>
    </a>
    <a href="https://www.androiddevdigest.com/digest-131/">
        <img src="https://img.shields.io/badge/AndroidDev%20Digest-%23131-blue.svg"/>
    </a>
    <!-- Android Weekly -->
    <a href="http://androidweekly.net/issues/issue-209">
        <img src="https://img.shields.io/badge/Android%20Weekly-%23209-blue.svg"/>
    </a>
    <a href="http://androidweekly.net/issues/issue-245">
        <img src="https://img.shields.io/badge/Android%20Weekly-%23245-blue.svg"/>
    </a>
    <!-- Number of Android apps (AppBrain) -->
    <a href="https://www.appbrain.com/stats/libraries/details/sensey/sensey">
        <img src="https://www.appbrain.com/stats/libraries/shield/sensey.svg">
    </a>
    <!-- Awesome Android -->
    <a href="https://snowdream.github.io/awesome-android/Other.html#Gesture">
        <img src="https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg"/>
    </a>
    <!-- GitHub stars -->
    <a href="https://github.com/nisrulz/sensey">
        <img src="https://img.shields.io/github/stars/nisrulz/sensey.svg?style=social&label=Star"/>
    </a>
    <!-- GitHub forks -->
    <a href="https://github.com/nisrulz/sensey/fork">
        <img src="hhttps://img.shields.io/github/forks/nisrulz/sensey.svg?style=social&label=Fork"/>
    </a>
    <!-- GitHub watchers -->
    <a href="https://github.com/nisrulz/sensey">
        <img src="https://img.shields.io/github/watchers/nisrulz/sensey.svg?style=social&label=Watch"/>
    </a>
    <!-- Say Thanks! -->
    <a href="https://saythanks.io/to/nisrulz">
        <img src="https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg"/>
    </a>
    <a href="https://www.paypal.me/nisrulz/5usd">
        <img src="https://img.shields.io/badge/$-donate-ff69b4.svg?maxAge=2592000&amp;style=flat">
    </a>
    <br/>
     <!-- GitHub followers -->
    <a href="https://github.com/nisrulz/sensey">
        <img src="https://img.shields.io/github/followers/nisrulz.svg?style=social&label=Follow%20@nisrulz"/>
    </a>
    <!-- Twitter Follow -->
    <a href="https://twitter.com/nisrulz">
        <img src="https://img.shields.io/twitter/follow/nisrulz.svg?style=social"/>
    </a>
</div>

<div align="center">
    Also featured in [Awesome Android Newsletter
    <a href="https://android.libhunt.com/newsletter/5">
         #Issue 5
    </a>, 
    <a href="https://android.libhunt.com/newsletter/21">
         #Issue 21
    </a>],
    <a href="https://github.com/codepath/android_guides/wiki/Must-Have-libraries#convenience">
         Codepath's Must Have Libraries
    </a>, 
    <a href="https://medium.cobeisfresh.com/cobes-top-5-android-libraries-september-2016-883757e61bf0#.oe2lzaxyn">
         COBE's Top 5 Android Libraries — September 2016
    </a>, 
    <a href="https://cloudrail.com/best-android-libraries-for-developers/">
         Best Android Libraries for Developers
    </a>, 
    <a href="https://dzone.com/articles/this-week-in-mobile-may-15">
         DZone
    </a>, 
    <a href="http://email.changelog.com/t/t-310383437622D164">
        Changelog Weekly Issue #115
    </a>
</div>

<div align="center">
    Blog Post: 
    <a href="https://android.jlelse.eu/i-could-not-find-a-simple-gesture-detection-android-library-so-i-built-one-334c0a307c16#.1us4zgise">
         I could not find a simple Gesture Detection android library, so I built one
    </a>
</div>

<div align="center">
  <sub>Built with ❤︎ by
  <a href="https://twitter.com/nisrulz">Nishant Srivastava</a> and
  <a href="https://github.com/nisrulz/sensey/graphs/contributors">
    contributors
  </a>
</div>
<br/>
<br/>

### Screenshot of sample app

![sc1](img/sc1.png) ![sc2](img/sc2.png)

# Supported gestures/events

| Gesture | Event type |
|---------|------------|
| Shake | `ShakeEvent.Detected`, `ShakeEvent.Stopped` |
| Flip | `FlipEvent.FaceUp`, `FlipEvent.FaceDown` |
| Light | `LightEvent.Dark`, `LightEvent.Light` |
| Proximity | `ProximityEvent.Near`, `ProximityEvent.Far` |
| Movement | `MovementEvent.Moved`, `MovementEvent.Stationary` |
| Orientation | `OrientationEvent.TopSideUp`, `BottomSideUp`, `LeftSideUp`, `RightSideUp` |
| Chop | `ChopEvent.Chopped` |
| TapOnBack | `TapOnBackEvent` |
| WristTwist | `WristTwistEvent.Twisted` |
| Wave | `WaveEvent.Waved` |
| Scoop | `ScoopEvent.Scooped` |
| PickupDevice | `PickupDeviceEvent.PickedUp`, `PutDown` |
| TiltDirection | `AxisXTilt`, `AxisYTilt`, `AxisZTilt` (with clockwise/anticlockwise) |
| RotationAngle | `RotationAngleEvent` (angles in degrees) |
| PinchScale | `PinchScaleEvent` (scale factor) |
| TouchType | `DoubleTap`, `LongPress`, `SingleTap`, `Swipe` (8 dirs), `Scroll` (4 dirs) |
| SoundLevel | `SoundLevelEvent` (dB level) |
| Step | `StepEvent` (step count, distance, activity type) |

---

# Quick start

```kotlin
// Initialize
Sensey.init(this)

// Start detection with a dispatcher lambda
Sensey.startShakeDetection { event ->
    when (event) {
        ShakeEvent.Detected -> println("Shake detected!")
        ShakeEvent.Stopped  -> println("Shake stopped")
    }
}

// Stop all detection
Sensey.stop()
```

See the **[full usage guide](sensey/USAGE.md)** for every gesture with parameter options.

# Including in your project

Sensey is available on Maven Central:

```gradle
implementation 'com.github.nisrulz:sensey:{latest version}'
```

where `{latest version}` corresponds to the latest published version on [Maven Central](https://search.maven.org/artifact/com.github.nisrulz/sensey).

# Architecture

Each gesture is split into three components:

- **`GestureTrigger<T>`** — Pure Kotlin contract. The detection algorithm lives here, with no Android dependencies.
- **Trigger implementation** — e.g., `ShakeTrigger`, `FlipTrigger`. Can be unit tested without a device or emulator.
- **Detector** — Thin Android-aware bridge that converts `SensorEvent` → trigger → your callback.

# Changelog

Starting with `1.0.1`, Changes exist in the [releases tab](https://github.com/nisrulz/sensey/releases).

# Pull Requests

I welcome and encourage all pull requests. It usually will take me within 24-48 hours to respond to any issue or request. Here are some basic rules to follow to ensure timely addition of your request:

1. Match coding style (braces, spacing, etc.) This is best achieved using CMD+Option+L (Reformat code) on Mac (not sure for Windows) with Android Studio defaults. This project uses a [modified version of Grandcentrix's code style](https://github.com/nisrulz/AndroidCodeStyle/tree/nishant-config), so please use the same when editing this project.
2. If its a feature, bugfix, or anything please only change code to what you specify.
3. Please keep PR titles easy to read and descriptive of changes, this will make them easier to merge :)
4. Pull requests _must_ be made against `develop` branch. Any other branch (unless specified by the maintainers) will get rejected.
5. Check for existing [issues](https://github.com/nisrulz/sensey/issues) first, before filing an issue.
6. Have fun!

## References of Sensey

Sensey is being used in production apps as well as research & development for thesis at universities.

[Click here for the full list of references](references/Readme.md)

## License

Licensed under the Apache License, Version 2.0, [click here for the full license](/LICENSE.txt).

# Author & Contributors

This project was created by [Nishant Srivastava](https://github.com/nisrulz/nisrulz.github.io#nishant-srivastava) but hopefully developed and maintained by many others. See the [the list of contributors here](https://github.com/nisrulz/sensey/graphs/contributors).

# Ways You Can Help/Support this project

- **Star** this repository and tell all your friends about it.
- **Watch** for new releases to get an update if something happens.
- [**Open an Issue**](https://github.com/nisrulz/sensey/issues/new/choose) if you catch any error in copy text or within the project itself.
- **Open a Pull Request** to add more reference links of where Sensey is being used or fixes against [existing issues](https://github.com/nisrulz/sensey/issues).

- If you can spare a few 💵:

  - [Buy me a coffee :coffee:](https://www.buymeacoffee.com/nisrulz) (one-time),
  - or [Sponsor me on GitHub](https://github.com/sponsors/nisrulz) (recurring monthly).

- Tell me you like this project or how it helped you out!

  - [Comment in the project's guestbook](https://github.com/nisrulz/sensey/issues/54) :blush:,
  - Reach out on [Twitter](https://twitter.com/nisrulz),
  - or [send a nice email my way](mailto:nisrulz@gmail.com)!

Thanks for your interest in this software :heart:

<img src="http://forthebadge.com/images/badges/built-for-android.svg" />
