package com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling;


import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * Catálogo de errores específicos del agregado Rate.
 * Siguiendo estrategia de ADR-22: Numeración consecutiva por agregado.
 * Contexto: Tarifas con vigencia temporal y contratos con EPSs (Colombia).
 */
public enum RateError implements ErrorCatalog {

    ERR_RATE_INVALID_AMOUNT(
            "RN-RATE-001",
            "El monto de la tarifa debe ser mayor a cero",
            "Rate"
    ),


    ERR_RATE_EXCESSIVE_ADJUSTMENT(
            "RN-RATE-015",
            "Ajuste de tarifa excesivo ({percentage}%). Máximo permitido: 300% sin aprobación gerencial",
            "Rate"
    ),


    ERR_RATE_INVALID_VALIDITY_RANGE(
            "RN-RATE-002",
            "Fecha de fin de vigencia debe ser posterior a fecha de inicio. Inicio: {validFrom}, Fin: {validTo}",
            "Rate"
    ),


    ERR_RATE_EXPIRED(
            "RN-RATE-003",
            "Tarifa vencida al momento de facturar. Vigencia: {validFrom} - {validTo}, Fecha factura: {invoiceDate}",
            "Rate"
    ),

    ERR_RATE_VALIDITY_CONFLICT(
            "RN-RATE-004",
            "Ya existe tarifa activa con vigencia solapada para Servicio: {serviceCode}, Pagador: {payerType}, Contrato: {contractId}",
            "Rate"
    ),

    ERR_RATE_EXPIRED_BUT_ACTIVE(
            "RN-RATE-013",
            "Tarifa vencida sigue marcada como activa. Vigencia hasta: {validTo}, Fecha actual: {now}",
            "Rate"
    ),

    ERR_RATE_CANNOT_SHORTEN_VALIDITY(
            "RN-RATE-014",
            "No se puede acortar vigencia porque existen facturas emitidas después de {newValidTo}",
            "Rate"
    ),

    WARN_RATE_FUTURE_VALIDITY(
            "RN-RATE-012",
            "Tarifa con vigencia futura lejana ({validFrom}). Verificar fecha correcta",
            "Rate"
    ),

    ERR_RATE_MISSING_CONTRACT(
            "RN-RATE-005",
            "Tarifa para EPS requiere contrato asociado. Pagador: {payerType}",
            "Rate"
    ),

    ERR_RATE_INACTIVE_SERVICE(
            "RN-RATE-010",
            "No se puede crear tarifa para servicio inactivo. Servicio: {serviceCode}",
            "Rate"
    ),

    ERR_RATE_CURRENCY_MISMATCH(
            "RN-RATE-011",
            "Moneda de tarifa ({rateCurrency}) no coincide con moneda del servicio ({serviceCurrency})",
            "Rate"
    ),

    ERR_RATE_INACTIVE(
            "RN-RATE-009",
            "No se puede editar tarifa inactiva. Crear nueva tarifa con nueva vigencia",
            "Rate"
    ),

    ERR_RATE_ADJUSTMENT_REQUIRES_JUSTIFICATION(
            "RN-RATE-008",
            "Ajuste de tarifa requiere justificación (mínimo 10 caracteres) para auditoría",
            "Rate"
    ),

    ERR_RATE_HAS_ACTIVE_INVOICES(
            "RN-RATE-006",
            "No se puede desactivar tarifa con facturas activas. Cantidad de facturas pendientes: {activeInvoicesCount}",
            "Rate"
    ),

    ERR_RATE_DEACTIVATION_REQUIRES_REASON(
            "RN-RATE-007",
            "La desactivación de tarifa requiere motivo de al menos 10 caracteres para auditoría",
            "Rate"
    ),

    ERR_RATE_NOT_FOUND(
            "AUX-RATE-001",
            "No existe tarifa vigente para Servicio: {serviceCode}, Pagador: {payerType}, Fecha: {date}",
            "Rate"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    RateError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }
    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }

}
