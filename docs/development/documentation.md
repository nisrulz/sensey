---
title: "Documentation"
weight: 6
---

# Documentation

## Local dev server

```sh
./scripts/serve_docs_local.sh # hot-reload at http://localhost:1313/
```

## Build for production

```sh
hugo --gc --minify # output → public/ (gitignored)
```

## CI / CD

Auto-deployed via [`.github/workflows/hugo.yml`](https://github.com/nisrulz/sensey/blob/master/.github/workflows/hugo.yml):

- **Trigger**: push to `master` or manual dispatch
- **Build**: `hugo --gc --minify`
- **Deploy**: output published to `gh-pages` branch

Site: `https://nisrulz.github.io/sensey/`.

## Project structure

```
docs/
├── _index.md              # Home
├── usage/                 # User docs
│   ├── setup.md
│   ├── overview.md
│   ├── context-specific-usage.md
│   └── gestures/*.md      # Per-gesture reference
└── development/           # Contributor docs
    ├── code-quality.md
    ├── publishing.md
    ├── architecture.md
    ├── creating-a-custom-plugin.md
    └── documentation.md   # This page
```

## Adding a page

1. Create `.md` in the right directory
2. Add front matter: `title` + `weight`
3. Link from the relevant `_index.md`
4. Use relative `.md` paths — `[Setup](setup.md)` — Hugo Book resolves them
