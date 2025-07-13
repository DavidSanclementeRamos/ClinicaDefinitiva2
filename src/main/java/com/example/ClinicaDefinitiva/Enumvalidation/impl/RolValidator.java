package com.example.ClinicaDefinitiva.Enumvalidation.impl;

import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.Enumvalidation.RolValido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RolValidator implements ConstraintValidator<RolValido, Roles> {

        private Set<Roles> allowedRoles;

        @Override
        public void initialize(RolValido annotation) {
            // Leer la lista de Roles permitidos desde el atributo de la anotación
            allowedRoles = new HashSet<>(Arrays.asList(annotation.allowed()));
        }

        @Override
        public boolean isValid(Roles value, ConstraintValidatorContext context) {
            // Validar: no nulo y esté en el conjunto permitido
            return value != null && allowedRoles.contains(value);
        }

    }
