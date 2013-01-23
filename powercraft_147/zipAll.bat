

set direc="F:\Programme\7-Zip"


IF exists reobf\minecraft\powercraft goto START
goto :EOF
:START
cd reobf\minecraft\powercraft
FOR /D %%f IN (*) DO (
call :PROGRAM %%f
)
goto :EOF

:PROGRAM
if /i "%1"=="nei" goto :EOF
cd ..
%direc%\7z.exe a %1.zip powercraft\%1
pause
IF /i "%1"=="management" %direc%\7z.exe a %1.zip powercraft\nei
IF /i "%1"=="weasel" %direc%\7z.exe a %1.zip %1
cd powercraft
goto :EOF