
![enter image description here](https://github.com/nisrulz/Sensey/raw/master/sample/src/main/res/mipmap-xhdpi/ic_launcher.png)

#Sensey

Making gesture detection a breeze.

Checkout the code on [here](https://github.com/nisrulz/Sensey).

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

  ---
  > *NOTE : Some variables used in build.gradle correspond to*
  > **COMPILE_SDK_VERSION**=23
  > **BUILDTOOLS_VERSION**=23.0.1
  > **VERSION_NAME**=1.0.1
  > **VERSION_CODE**=2

# License

 <a rel="license" href="http://www.apache.org/licenses/LICENSE-2.0.html" target="_blank">Apache License 2.0</a>
