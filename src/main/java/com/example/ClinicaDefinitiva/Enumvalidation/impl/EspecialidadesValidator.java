package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enumvalidation.EspecialidadesValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EspecialidadesValidator  implements ConstraintValidator<EspecialidadesValido, Especialidades> {

   private Set<Especialidades> allowedAEspecialidad;
    @Override
    public void initialize(EspecialidadesValido constraintAnnotation) {
        //  ConstraintValidator.super.initialize(constraintAnnotation);
        allowedAEspecialidad = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));

    }

    @Override
    public boolean isValid(Especialidades especialidades, ConstraintValidatorContext constraintValidatorContext) {
        // Validar: no nulo y esté en el conjunto permitido
        return especialidades != null && allowedAEspecialidad.contains(especialidades);


    }
}
