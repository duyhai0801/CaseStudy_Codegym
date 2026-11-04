@echo off
REM Bien dich va chay chuong trinh khong can VS Code / IntelliJ
chcp 65001 >nul
cd /d "%~dp0"
if exist bin rmdir /s /q bin
javac -encoding UTF-8 -d bin -sourcepath src\main\java src\main\java\Main.java
if errorlevel 1 (
    echo Bien dich that bai!
    pause
    exit /b 1
)
java -cp bin Main
pause
