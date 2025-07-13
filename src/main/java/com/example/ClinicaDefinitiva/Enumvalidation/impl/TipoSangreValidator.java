package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.Enumvalidation.TipoSangreValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TipoSangreValidator implements ConstraintValidator<TipoSangreValido, String> {
    private Set<String> etiquetasValidas;

    public void initialize(TipoSangreValido anotacion) {
        etiquetasValidas = Arrays.stream(anotacion.allowed())
                .map(Tipo_sangre::getEtiqueta)
                .collect(Collectors.toSet());
    }

    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        return value != null && etiquetasValidas.contains(value);
    }


}
