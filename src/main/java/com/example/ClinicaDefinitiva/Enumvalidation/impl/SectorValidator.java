package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.Enumvalidation.SectorValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SectorValidator implements ConstraintValidator<SectorValido,SectorValido> {

    private Set<Sector> allowedSector;
    @Override
    public void initialize(SectorValido constraintAnnotation) {
        // Leer la lista de Roles permitidos desde el atributo de la anotación
        allowedSector = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));

    }

    @Override
    public boolean isValid(SectorValido sectorValido, ConstraintValidatorContext constraintValidatorContext) {
        // Validar: no nulo y esté en el conjunto permitido
        return sectorValido != null && allowedSector.contains(sectorValido);
    }
}
