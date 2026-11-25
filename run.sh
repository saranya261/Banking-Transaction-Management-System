#!/bin/bash
# Put your MySQL connector jar inside the "lib" folder first.
mkdir -p bin lib
javac -cp "lib/*" -d bin src/*.java || exit 1
echo "Open http://localhost:8080 in your browser"
java -cp "bin:lib/*" WebServer
