# Dev Documentation

## Publishing

- To release library to MavenLocal(~/.m2/):

  ```sh
  ./gradlew releaseToMavenLocal
  ```

- To release library
  to [MavenCentral](https://search.maven.org/artifact/com.github.nisrulz/sensey):

  ```sh
  ./gradlew releaseToMavenCentral
  ```

## Build Environment

- AGP 9.2.1
- Gradle 9.5.1
- Kotlin 2.3.21
- Java 21
- compileSdk 36 / minSdk 23 / targetSdk 35
