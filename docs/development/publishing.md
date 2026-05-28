---
title: "Publishing"
weight: 3
---

# Publishing

## Maven Local

```sh
./gradlew releaseToMavenLocal # → ~/.m2/
```

## Maven Central

```sh
./gradlew releaseToMavenCentral
```

## Docs

Docs are versioned by git tag. Pushing a tag triggers the [CI workflow](https://github.com/nisrulz/sensey/blob/master/.github/workflows/hugo.yml)
to snapshot the docs at that point:

```sh
git tag v1.2.3
git push origin v1.2.3
```

The snapshot is published to `https://nisrulz.github.io/sensey/v1.2.3/`.
Latest docs (from `master`) are always at the site root.
