#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

cd "$SCRIPT_DIR/homepage" && npm run build:fragment

cd "$SCRIPT_DIR"
if [[ -x "$SCRIPT_DIR/.venv/bin/zensical" ]]; then
   "$SCRIPT_DIR/.venv/bin/zensical" serve
 else
   zensical serve
 fi