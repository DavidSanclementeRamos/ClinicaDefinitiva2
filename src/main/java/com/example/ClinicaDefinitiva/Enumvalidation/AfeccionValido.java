package com.example.ClinicaDefinitiva.Enumvalidation;


import com.example.ClinicaDefinitiva.Enum.Afeccion;
import com.example.ClinicaDefinitiva.Enumvalidation.impl.AfeccionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD }) // le indica a java en donde se puede aplicar esa anotacion.
@Retention(RUNTIME) // le dice a java que la anotacion debe concervarse durante la ejecucion del programa
@Constraint(validatedBy = AfeccionValidator.class)//Está marcando que esta anotación será utilizada como una restricción de validación, indica que clase se encarga de la logica
public @interface AfeccionValido {

    String message() default "Afeccio no válido"; // mensaje de error puede sobre escribirse: @RolValido(message = "Este rol no está permitido")
    Class<?>[] groups() default {}; // - Se usa para agrupar validaciones, por ejemplo, si tienes validaciones diferentes para crear vs actualizar.
    Class<? extends Payload>[] payload() default {};// - Es un campo reservado para pasar metadatos adicionales, aunque en la mayoría de los casos no se usa.

    // Nuevo atributo: lista de roles permitidos
    Afeccion[] allowed() default { Afeccion.CARIES, Afeccion.BRUXISMO,Afeccion.DIENTES_IMPACTADOS,Afeccion.GINGIVITIS,
            Afeccion.DOLOR_ODONTOGENICO,Afeccion.HALITOSIS,Afeccion.MALOCLUSION,Afeccion.PERIODONTITIS,Afeccion.PLACA_BACTERIANA,
            Afeccion.SENSIBILIDAD_DENTAL};

}
