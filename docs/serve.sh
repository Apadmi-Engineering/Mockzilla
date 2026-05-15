#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
FRAGMENT="$SCRIPT_DIR/docs/overrides/homepage-content.html"

cd "$SCRIPT_DIR/homepage" && npm run build:fragment

cd "$SCRIPT_DIR"
"$SCRIPT_DIR/.venv/bin/zensical" serve
