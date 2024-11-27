#!/bin/bash

SRC_DIR="src"
OUT_DIR="out"

MAIN_CLASS="com.anonify.main.Main" 

mkdir -p "$OUT_DIR"

echo "Compiling Java files..."
# Use the -sourcepath flag to ensure proper package compilation
javac -d "$OUT_DIR" -sourcepath "$SRC_DIR" $(find "$SRC_DIR" -name "*.java")
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Running the Java program..."
# Run the program with the fully qualified main class name
sudo java -cp "$OUT_DIR" "$MAIN_CLASS"
