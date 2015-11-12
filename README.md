
![enter image description here](https://github.com/nisrulz/Sensey/raw/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

#Sensey

Making gesture detection a breeze.

Checkout the code on [here](https://github.com/nisrulz/Sensey).

#Integration
- Include the below into your app's ***build.gradle*** right at the very bottom.
```gradle
repositories {
    maven{
        url 'http://maven.excogitation.in/'
    }
}
```
- Next add the dependency
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

# License

 <a rel="license" href="http://www.apache.org/licenses/LICENSE-2.0.html" target="_blank">Apache License 2.0</a>
