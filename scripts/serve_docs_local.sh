#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

hugo server \
  --buildDrafts \
  --disableFastRender \
  --navigateToChanged \
  --watch \
  --port 1313
