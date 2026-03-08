@echo off
cd /d "c:\Users\David\OneDrive\Documents\proyectosJava\ClinicaDefinitiva"
echo Compilando proyecto...
call mvnw.cmd clean compile -X > compile_output.txt 2>&1
echo.
echo === RESULTADO DE COMPILACION ===
type compile_output.txt | findstr /I "error" > nul && (
  echo ERRORES ENCONTRADOS:
  type compile_output.txt | findstr /I "error"
) || (
  echo Compilacion exitosa!
)
echo.
echo Ejecutando tests...
call mvnw.cmd test > test_output.txt 2>&1
echo.
echo === RESULTADO DE TESTS ===
type test_output.txt
pause
