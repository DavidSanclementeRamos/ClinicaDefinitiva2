package com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject;

import java.util.UUID;
/**
 * Entidad: InvoiceItem (Ítem de Factura)
 *
 * Representa un servicio facturado dentro de una factura.
 *
 * Características críticas:
 * - ⭐ Snapshot inmutable de precio: Preserva tarifa al momento de facturar
 * - ⭐ Trazabilidad: Referencia a Rate usado (evidencia forense)
 * - ⭐ Auditoría DIAN: Precio no cambia aunque Rate se ajuste después
 * - ⭐ Invariante matemático: totalPrice = unitPrice × quantity
 *
 * Inmutabilidad:
 * - Todos los campos son final (no hay setters)
 * - Una vez creado, el ítem NO puede modificarse
 * - Para cambios, eliminar y crear nuevo ítem

 */
public final class InvoiceItemId {

   /** public InvoiceItemId(String value) {
        this.value = value;
    }
    public static InvoiceItemId generate(){
        return new InvoiceItemId(UUID.randomUUID().toString());
    }
    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static InvoiceItemId fromString(String value) {
        if (value == null) return null; // decisión: devuelve null si no hay valor; cambia a throw si prefieres
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("InvoiceItemId string is empty");
        return new InvoiceItemId(trimmed);
    }
    public String getValue() {
        return value;
    }**/





}
