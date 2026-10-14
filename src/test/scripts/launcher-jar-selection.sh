#!/bin/sh
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Run with: sh src/test/scripts/launcher-jar-selection.sh [path/to/camel.sh]
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
LAUNCHER_SCRIPT=${1:-"$SCRIPT_DIR/../../main/resources/bin/camel.sh"}
TEST_DIR=$(mktemp -d "${TMPDIR:-/tmp}/camel-launcher-selection.XXXXXX")
TEST_DIR=$(CDPATH= cd -- "$TEST_DIR" && pwd)
trap 'rm -rf "$TEST_DIR"' EXIT HUP INT TERM
mkdir "$TEST_DIR/bin with spaces"
cp "$LAUNCHER_SCRIPT" "$TEST_DIR/bin with spaces/camel.sh"
cat > "$TEST_DIR/java" <<'JAVA'
#!/bin/sh
if [ "${1:-}" = "-version" ]; then
  echo 'openjdk version "17.0.1"' >&2
  exit 0
fi
printf '%s\n' "$@"
JAVA
chmod +x "$TEST_DIR/java"
LAUNCHER_JAR="$TEST_DIR/bin with spaces/camel-launcher-9.9.9.jar"
: > "$LAUNCHER_JAR"

assert_launcher_jar() {
  actual=$(JAVACMD="$TEST_DIR/java" JAVA_OPTS=-Xmx512m sh "$TEST_DIR/bin with spaces/camel.sh" hawtio "argument with spaces")
  expected=$(printf '%s\n' -Xmx512m -jar "$LAUNCHER_JAR" hawtio "argument with spaces")
  if [ "$actual" != "$expected" ]; then
    printf 'Unexpected launcher arguments:\n%s\n' "$actual" >&2
    exit 1
  fi
}

assert_launcher_jar
: > "$TEST_DIR/bin with spaces/camel-launcher-9.9.9-sources.jar"
assert_launcher_jar
printf 'PASS: executable JAR selected with and without sources JAR; arguments and paths preserved\n'
