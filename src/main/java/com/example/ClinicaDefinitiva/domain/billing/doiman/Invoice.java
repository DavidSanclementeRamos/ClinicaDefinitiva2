package com.example.ClinicaDefinitiva.domain.billing.doiman;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    String id;                     // Identificador único de la factura
    String patient_id;             // Referencia al paciente (Patient)
    String provider_id;            // Profesional o clínica que emite la factura
    LocalDateTime date_issued;       // Fecha de emisión
    LocalDateTime due_date;          // Fecha de vencimiento
    String status;                 // Estado: Draft, Pending, Paid, Cancelled
    List<InvoiceItem> itemList;    // Lista de ítems facturados
    float subtotal;               // Suma de los ítems antes de impuestos
    float tax;                  // Impuestos aplicados
    float total;                // Total a pagar
    String currency;               // Moneda (ej. COP, USD)
    String payer;                  // EPS, aseguradora o paciente particular
    String contract_id;            // Referencia a contrato/convenio (opcional)
    String notes;                  // Observaciones adicionales
    LocalDateTime created_at;        // Fecha de creación en el sistema
    LocalDateTime updated_at;        // Última actualización


}
