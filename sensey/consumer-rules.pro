# Keep Sensey event sealed interfaces and data classes for consumer apps
-keepnames class com.github.nisrulz.sensey.**Event { *; }
-keepnames class com.github.nisrulz.sensey.**Event$* { *; }

# Keep GestureTrigger interface for reflection
-keepnames class com.github.nisrulz.sensey.contract.** { *; }

# Keep public API entry point
-keepnames class com.github.nisrulz.sensey.Sensey { *; }
