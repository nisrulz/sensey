#!/usr/bin/env bash
set -euo pipefail

CURRENT_VERSION="${1:-latest}"
DOCS_DIR="docs"

cat > "$DOCS_DIR/index.html" << EOF
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Sensey Documentation</title>
  <style>
    body { font-family: system-ui, sans-serif; max-width: 640px; margin: 40px auto; padding: 0 20px; }
    h1 { border-bottom: 2px solid #eee; padding-bottom: 8px; }
    ul { list-style: none; padding: 0; }
    li { margin: 8px 0; }
    a { color: #0366d6; text-decoration: none; }
    a:hover { text-decoration: underline; }
  </style>
</head>
<body>
  <h1>Sensey Documentation</h1>
  <ul>
    <li><a href="./latest/index.html">Latest ($CURRENT_VERSION)</a></li>
EOF

for dir in $(ls -d "$DOCS_DIR"/*/ 2>/dev/null | sort -V -r); do
  name=$(basename "$dir")
  if [ "$name" != "latest" ]; then
    echo "    <li><a href=\"./$name/index.html\">$name</a></li>" >> "$DOCS_DIR/index.html"
  fi
done

cat >> "$DOCS_DIR/index.html" << EOF
  </ul>
</body>
</html>
EOF
