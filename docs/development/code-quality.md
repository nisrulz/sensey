---
title: "Building and Code Quality"
weight: 2
---

# Building and Code Quality

## Building

```sh
./gradlew assembleDebug
```

## Code Quality

### ktlint

This project uses [ktlint](https://github.com/pinterest/ktlint) via the [ktlint-gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle) to enforce consistent Kotlin code style.

```sh
# Check for violations
./gradlew ktlintCheck

# Auto-format all Kotlin files
./gradlew ktlintFormat
```

Configuration is in `.editorconfig` at the project root.
