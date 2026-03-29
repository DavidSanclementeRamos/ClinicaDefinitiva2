
package com.example.ClinicaDefinitiva.application.payment.dto;

/**
 * DTO: PayerRequestDto
 * 
 * Información del pagador en el request.
 */
public record PayerRequestDto(
    String type,        // PATIENT, EPS, INSURANCE, COMPANY, OTHER
    String identifier,  // NIT, documento, código (opcional)
    String name
) {
   
}
 
