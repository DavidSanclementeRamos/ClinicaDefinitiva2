
package com.example.ClinicaDefinitiva.application.administration.operations.dto;

import java.time.LocalTime;

/**
 * DTO para bloques excluidos dentro de un turno 
 * y para excluir un bloque de tiempo dentro de un turno.
 */
public record ExcludedBlockDto(
        LocalTime start,
        LocalTime end,
        String reason
) {


}
