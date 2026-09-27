@echo off
set "EXE=%~dp0..\dist\win\DesertStormfront\DesertStormfront.exe"
if not exist "%EXE%" (
  echo exe not found: %EXE%
  echo please run package-exe.bat first
  pause
  exit /b 1
)
start "" "%EXE%"
