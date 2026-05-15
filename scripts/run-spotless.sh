#!/bin/sh

# We only stage files altered by Spotless that were previously staged
# by the user to prevent staging files that were not intended to be staged by the user.
stagedFiles=$(git diff --staged --name-only)

echo "Running spotlessApply. Formatting code..."
./gradlew spotlessApply

# Last exit code
RESULT=$?

for file in $stagedFiles; do
  if test -f "$file"; then
    git add $file
  fi
done

exit $RESULT
