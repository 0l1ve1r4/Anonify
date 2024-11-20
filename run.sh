#!/bin/bash

SRC_DIR="src"
OUT_DIR="out"

MAIN_CLASS="Main"

mkdir -p "$OUT_DIR"

echo "Compiling Java files..."
javac -d "$OUT_DIR" "$SRC_DIR"/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Running the Java program..."
java -cp "$OUT_DIR" "$MAIN_CLASS"
