package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.Enumvalidation.TipoResponsableValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TipoResponsableValidator implements ConstraintValidator<TipoResponsableValido, TipoResponsable> {

    private Set<TipoResponsable > allowedTipoResponsable;
    @Override
    public void initialize(TipoResponsableValido constraintAnnotation) {

        // Leer la lista de Roles permitidos desde el atributo de la anotación
        allowedTipoResponsable = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));

    }

    @Override
    public boolean isValid(TipoResponsable tipoResponsable, ConstraintValidatorContext constraintValidatorContext) {
        // Validar: no nulo y esté en el conjunto permitido
        return  tipoResponsable != null && allowedTipoResponsable.contains(tipoResponsable);

    }

}
