package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Afeccion;

import com.example.ClinicaDefinitiva.Enumvalidation.AfeccionValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AfeccionValidator implements ConstraintValidator<AfeccionValido, Afeccion> {

    private Set<Afeccion> allowedAfeccion;


    @Override
    public void initialize(AfeccionValido constraintAnnotation) {
      //  ConstraintValidator.super.initialize(constraintAnnotation);
        allowedAfeccion = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));
    }

    @Override
    public boolean isValid(Afeccion afeccion, ConstraintValidatorContext constraintValidatorContext) {
        // Validar: no nulo y esté en el conjunto permitido
        return afeccion != null && allowedAfeccion.contains(afeccion);
    }
}
