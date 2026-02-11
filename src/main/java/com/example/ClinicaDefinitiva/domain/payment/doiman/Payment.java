package com.example.ClinicaDefinitiva.domain.payment.doiman;


import java.time.LocalDateTime;

public class Payment {
    String id;                     // Identificador único del pago
    String invoice_id;             // Referencia a la factura
    float amount;               // Monto del pago
    String currency;               // Moneda (ej. COP, USD)
    String payment_method;         // Método de pago (cash, card, transfer, EPS, Stripe)
    String transaction_ref;        // Referencia de la transacción (voucher, código EPS, ID Stripe)
    LocalDateTime date;              // Fecha del pago
    String status;                 // Estado: Pending, Confirmed, Failed, Refunded
    String payer;                  // Quién paga (paciente, EPS, aseguradora)
    String notes;                  // Observaciones adicionales
    LocalDateTime created_at;           // Fecha de creación en el sistema
    LocalDateTime updated_at;        // Última actualización
}
