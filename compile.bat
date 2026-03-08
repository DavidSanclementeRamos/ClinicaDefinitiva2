@echo off
cd /d "c:\Users\David\OneDrive\Documents\proyectosJava\ClinicaDefinitiva"
call mvnw.cmd clean compile 2>&1 > compile_output.txt
type compile_output.txt
