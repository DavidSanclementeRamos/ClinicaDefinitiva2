package com.example.ClinicaDefinitiva.Enumvalidation;


import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.Enumvalidation.impl.TipoSangreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD }) // le indica a java en donde se puede aplicar esa anotacion.
@Retention(RUNTIME) // le dice a java que la anotacion debe concervarse durante la ejecucion del programa
@Constraint(validatedBy = TipoSangreValidator.class)//Está marcando que esta anotación será utilizada como una restricción de validación, indica que clase se encarga de la logica


public @interface TipoSangreValido {

    String message() default "tipo de sangre no válido"; // mensaje de error puede sobre escribirse: @RolValido(message = "Este rol no está permitido")

    Class<?>[] groups() default {}; // - Se usa para agrupar validaciones, por ejemplo, si tienes validaciones diferentes para crear vs actualizar.

    Class<? extends Payload>[] payload() default {};// - Es un campo reservado para pasar metadatos adicionales, aunque en la mayoría de los casos no se usa.

    // Nuevo atributo: lista de roles permitidos
    Tipo_sangre[] allowed() default {Tipo_sangre.A_NEGATIVO, Tipo_sangre.AB_NEGATIVO, Tipo_sangre.B_NEGATIVO, Tipo_sangre.A_NEGATIVO, Tipo_sangre.A_POSITIVO, Tipo_sangre.B_POSITIVO, Tipo_sangre.O_POSITIVO,
            Tipo_sangre.AB_POSITIVO , Tipo_sangre.O_NEGATIVO};

}
