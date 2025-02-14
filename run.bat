@echo off
setlocal enabledelayedexpansion

REM Diretórios de origem e saída
set "SRC_DIR=src"
set "OUT_DIR=out"
set "MAIN_CLASS=com.anonify.main.Main"

REM Cria o diretório de saída, se não existir
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

echo Compilando arquivos...

REM Acumula todos os arquivos .java recursivamente na variável FILES
set "FILES="
for /r "%SRC_DIR%" %%f in (*.java) do (
    set "FILES=!FILES! "%%f""
)

REM Compila os arquivos, especificando o diretório de saída e o sourcepath
javac -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" %FILES%
if errorlevel 1 (
    echo A compilacao falhou.
    exit /b 1
)

echo Executando o programa...

REM Usa "start /wait" para aguardar o encerramento do programa
start /wait javaw -cp "%OUT_DIR%" %MAIN_CLASS%

endlocal

exit
