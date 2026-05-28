---
title: "Documentation"
weight: 6
---

# Documentation

## Local development server

Start the Hugo dev server with live reload:

```sh
./scripts/serve_docs_local.sh
```

Opens at [http://localhost:1313/](http://localhost:1313/). Changes to any file under `docs/` auto-reload the browser.

## Building the site

Generate the static site for production:

```sh
hugo --gc --minify
```

Output goes to `public/`.

## Project structure

```
docs/
├── _index.md              # Home page
├── usage/
│   ├── _index.md          # Usage section index
│   ├── setup.md           # Dependency setup
│   ├── overview.md        # Plugin architecture
│   ├── context-specific-usage.md
│   └── gestures/
│       ├── _index.md      # Gesture list
│       └── *.md           # Per-gesture reference
└── development/
    ├── _index.md          # Development section index
    ├── code-quality.md
    ├── publishing.md
    ├── architecture.md
    ├── creating-a-custom-plugin.md
    └── documentation.md   # This page
```

## Adding a new page

1. Create the `.md` file in the appropriate directory
2. Add front matter with `title` and `weight`
3. Reference it from the relevant `_index.md`
4. Links use relative `.md` paths — `[Setup](setup.md)` — resolved automatically by Hugo Book
