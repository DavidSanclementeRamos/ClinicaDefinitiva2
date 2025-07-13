package com.example.ClinicaDefinitiva.Enumvalidation;

import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.Enumvalidation.impl.RolValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD }) // le indica a java en donde se puede aplicar esa anotacion.
@Retention(RUNTIME) // le dice a java que la anotacion debe concervarse durante la ejecucion del programa
@Constraint(validatedBy = RolValidator.class)//Está marcando que esta anotación será utilizada como una restricción de validación, indica que clase se encarga de la logica

public @interface RolValido { // Estás creando una anotación personalizada.

    String message() default "Rol no válido"; // mensaje de error puede sobre escribirse: @RolValido(message = "Este rol no está permitido")
    Class<?>[] groups() default {}; // - Se usa para agrupar validaciones, por ejemplo, si tienes validaciones diferentes para crear vs actualizar.
    Class<? extends Payload>[] payload() default {};// - Es un campo reservado para pasar metadatos adicionales, aunque en la mayoría de los casos no se usa.

    // Nuevo atributo: lista de roles permitidos
    Roles[] allowed() default { Roles.ADMINISTRADOR, Roles.ODONTOLOGO, Roles.PACIENTE, Roles.RESPONSABLE, Roles.SECRETARIO };


}

