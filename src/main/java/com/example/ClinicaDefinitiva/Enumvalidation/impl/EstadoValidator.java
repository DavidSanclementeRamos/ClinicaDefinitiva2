package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enumvalidation.EstadoValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EstadoValidator implements ConstraintValidator<EstadoValido, Estado> {

   private Set<Estado> allowedAEstado;
    @Override
    public void initialize(EstadoValido constraintAnnotation) {
        //  ConstraintValidator.super.initialize(constraintAnnotation);
        allowedAEstado = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));

    }




    @Override
    public boolean isValid(Estado estado, ConstraintValidatorContext constraintValidatorContext) {
        // Validar: no nulo y esté en el conjunto permitido
        return estado != null && allowedAEstado.contains(estado);

    }

}
