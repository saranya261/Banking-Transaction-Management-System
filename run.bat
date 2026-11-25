@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
if not exist lib mkdir lib
if exist sources.txt del sources.txt
for %%f in (src\*.java) do echo src/%%~nxf>> sources.txt
javac -cp "lib/*" -d bin @sources.txt
if errorlevel 1 goto fail
del sources.txt
echo.
echo Open http://localhost:8080 in your browser
java -cp "bin;lib/*" WebServer
pause
exit /b 0
:fail
echo Compile failed
pause
exit /b 1
