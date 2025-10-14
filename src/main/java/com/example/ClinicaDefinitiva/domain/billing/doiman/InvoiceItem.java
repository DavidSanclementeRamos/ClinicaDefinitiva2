package com.example.ClinicaDefinitiva.domain.billing.doiman;

import java.time.LocalDateTime;

public class InvoiceItem {
    // Representa un servicio facturado.

    String id;                   // Identificador único
    String invoice_id;           // Referencia a la factura
    String service_id;           // Referencia al servicio prestado
    String description;          // Descripción del ítem (ej. "Extracción cordal")
    int quantity;             // Cantidad (ej. 1, 2 unidades)
    float unit_price;         // Precio unitario aplicado
    float total_price;        // Total (quantity * unit_price)
    String rate_id;              //Referencia a la tarifa usada
    LocalDateTime performed_at;    // Fecha/hora de prestación
    String provider_id;          // Profesional que realizó el servicio


}
