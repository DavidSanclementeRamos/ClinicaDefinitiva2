
package com.example.ClinicaDefinitiva.application.payment.dto;


/**
 * DTO: PayerDto
 * 
 * Información del pagador.
 */
public record PayerDto(
    String type,
    String identifier,
    String name
) {
   
}
