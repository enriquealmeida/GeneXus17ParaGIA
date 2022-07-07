@echo off
pushd %~dp0
if not exist %1 goto nomakeprogram
set MAKE_PROGRAM=%1
if not exist %2 goto nojavac
set JAVAC=%2
set CFLAGS=
set FILE=%3
set PATHPACK=%4
set BLOCKSIZE=500
set SYNCHRONIZEALL=n

if "%4" == "options:" set CFLAGS=%5
if "%4" == "options:" set PATHPACK=

:loop
if .%6. == .. goto compile
   set CFLAGS=%CFLAGS% %6
   shift
goto loop

:compile
%MAKE_PROGRAM% -f %FILE%.mak
:end
if ERRORLEVEL 1 goto dele

goto end2
:dele
if "%PATHPACK%" == "com\genexus\" goto dm
if exist %PATHPACK%%FILE%.class del %PATHPACK%%FILE%.class > nul
exit 1
goto end2

:dm
del developermenu.class > nul

:nomakeprogram
echo Make program could not be located. Please set the correct path.
exit 1
goto end2

:nojavac
echo Java compiler could not be located. Please set the correct path.
exit 1

:end2
popd