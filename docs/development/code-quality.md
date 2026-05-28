---
title: "Building and Code Quality"
weight: 2
---

# Building and Code Quality

## Build

```sh
./gradlew assembleDebug
```

## Lint

Uses [ktlint](https://github.com/pinterest/ktlint) via [ktlint-gradle](https://github.com/JLLeitschuh/ktlint-gradle).

```sh
./gradlew ktlintCheck   # check formatting
./gradlew ktlintFormat  # auto-fix
```

Config: `.editorconfig` at project root.
