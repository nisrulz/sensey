![enter image description here](https://github.com/nisrulz/Sensey/raw/master/sample/src/main/res/mipmap-xhdpi/ic_launcher.png)

#Sensey 

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.nisrulz/sensey/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.nisrulz/sensey) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sensey-green.svg?style=true)](https://android-arsenal.com/details/1/3550) <a href="http://www.methodscount.com/?lib=com.github.nisrulz%3Asensey%3A1.0.1"><img src="https://img.shields.io/badge/Methods and size-60 | 9 KB-e91e63.svg"></img></a> [![Twitter](https://img.shields.io/badge/Twitter-@nisrulz-blue.svg?style=flat)](http://twitter.com/nisrulz)

Making gesture detection a breeze.

#Integration
- Sensey is available in the MavenCentral, so getting it as simple as adding it as a dependency
```gradle
compile 'com.github.nisrulz:sensey:1.0.1'
```

#Usage
+ Initialize Sensey under your onCreate() in the activity/service
```java
Sensey.getInstance().init(context);
```

+ Next to enable detection for
  + Shake
    ```java
    Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
                      @Override
                      public void onShakeDetected() {
                          // Shake detected, do something
                      }
                  });
    ```

  + Flip
    ```java
    Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
                        @Override
                        public void onFaceUp() {
                            // Face up detected, do something
                        }

                        @Override
                        public void onFaceDown() {
                            // Face down detected, do something
                        }
                    });
    ```
  + Orientation
    ```java
    Sensey.getInstance().startOrientationDetection(new OrientationDetector.OrientationListener() {
                        @Override
                        public void onTopSideUp() {
                            // Top side up detected, do something
                        }

                        @Override
                        public void onBottomSideUp() {
                             // Bottom side up detected, do something
                        }

                        @Override
                        public void onRightSideUp() {
                            // Right side up detected, do something
                        }

                        @Override
                        public void onLeftSideUp() {
                             // Left side up detected, do something
                        }
                    });
    ```
  + Proximity
    ```java
    Sensey.getInstance().startProximityDetection(new ProximityDetector.ProximityListener() {
                        @Override
                        public void onNear() {
                            // Near, do something
                        }

                        @Override
                        public void onFar() {
                            // far, do something
                        }
                    });
    ```


+ To disable detection for
    + Shake
    ```java
     Sensey.getInstance().stopShakeDetection();
    ```
    + Flip
    ```java
      Sensey.getInstance().stopFlipDetection();
    ```
    + Orientation
    ```java
     Sensey.getInstance().stopOrientationDetection();
    ```
    + Proximity
    ```java
     Sensey.getInstance().stopProximityDetection();
    ```

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

