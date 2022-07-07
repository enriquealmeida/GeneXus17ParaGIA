@ECHO OFF
   IF (%1)==() GOTO USAGE
   ECHO Compress KB Started
   msbuild CompressKB.msbuild /P:kbLocation=%1
   GOTO END
:USAGE
ECHO Usage: Compress KbLocation
ECHO i.e.: Compress.bat "C:\My Knowledgebases\KBTest\"
:END 