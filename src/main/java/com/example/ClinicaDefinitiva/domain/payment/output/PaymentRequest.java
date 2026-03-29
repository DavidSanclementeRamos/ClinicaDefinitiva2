package com.example.ClinicaDefinitiva.domain.payment.output;

import com.example.ClinicaDefinitiva.domain.vo.Price;

/**
 * DTO: PaymentRequest
 * 
 * Datos necesarios para procesar un pago con un gateway.
 * 
 * Este DTO es parte del dominio (port) porque define el contrato
 * entre el dominio y los adapters de gateway.
 */
public record PaymentRequest(
    Price amount,
    String currency,
    String customerEmail,
    String customerName,
    String description,
    String invoiceNumber
) {
    
    /**
     * Constructor compacto con validaciones.
     */
    public PaymentRequest {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be null or empty");
        }
    }
    
    /**
     * Crea un PaymentRequest con datos mínimos.
     */
    public static PaymentRequest of(
            Price amount, 
            String currency, 
            String customerName, 
            String invoiceNumber) {
        
        return new PaymentRequest(
            amount,
            currency,
            null, // email opcional
            customerName,
            "Payment for invoice " + invoiceNumber,
            invoiceNumber
        );
    }
}
