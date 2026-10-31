@echo off
title ShopEase Application
cd /d "%~dp0"

set "JAR=lib\mysql-connector-j-26.10.0\mysql-connector-j-26.10.0\mysql-connector-j-26.10.0.jar"

echo ==================================
echo       ShopEase Application
echo ==================================

if not exist "%JAR%" (
    echo MySQL Connector JAR not found!
    pause
    exit /b 1
)

if not exist "out\gui\LoginFrame.class" (
    echo Compiling application...
    javac -cp "%JAR%" -d out src\Main.java src\util\*.java src\dao\*.java src\gui\*.java
    if errorlevel 1 (
        echo Compilation failed!
        pause
        exit /b 1
    )
)

echo Starting ShopEase...
java -cp "out;%JAR%" gui.LoginFrame

pause
