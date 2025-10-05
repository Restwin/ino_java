#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
# Путь к главному классу был изменен
java -cp "$ROOT/out" ru.rtk.java.homeworks.hw9.dungeon.Main