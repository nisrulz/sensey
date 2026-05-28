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

- **Push to `master`**: deploys to site root → latest docs
- **Tag push (`v*`)**: deploys to `/vX.Y.Z/` → versioned snapshot
- **Manual**: workflow dispatch from GitHub UI

Built with `hugo --gc --minify`, published to `gh-pages` branch.

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
