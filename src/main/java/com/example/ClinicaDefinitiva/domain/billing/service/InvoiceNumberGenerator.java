package com.example.ClinicaDefinitiva.domain.billing.service;



import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumber;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.BillingVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio: InvoiceNumberGenerator
 *
 * Responsabilidades:
 * - Generar números de factura secuenciales siguiendo las reglas de negocio.
 * - Delegar la validación de formato al VO InvoiceNumber.
 * - Mantener un contador interno para la secuencia.
 *
 * Notas:
 * - En producción, este servicio debería integrarse con un repositorio
 *   o con la numeración oficial de la DIAN (Colombia).
 * - Aquí se implementa una versión simplificada con AtomicLong.
 * 
 * // TODO: En producción, la secuencia debe persistirse en BD para sobrevivir reinicios
// y debe sincronizarse con la numeración oficial DIAN.
 */
@Service
public  class InvoiceNumberGenerator {

    private final String prefix;
    private final AtomicLong sequence;

    /**
     * Crea un generador de números de factura con un prefijo inicial.
     *
     * @param prefix prefijo de la factura (ej. "FAC", "INV")
     * @param initialSequence número inicial de la secuencia
     */

    public InvoiceNumberGenerator(@Value("${clinic.billing.invoice.prefix:FAC}") String prefix,
            @Value("${clinic.billing.invoice.initial-sequence:0}") long initialSequence) {

        if (prefix == null || prefix.isBlank()) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_NUMBER_PREFIX_REQUIRED, VOContext.BILLING);
        }
        if (initialSequence < 0) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_NUMBER_INITIAL_SEQUENCE_NEGATIVE,VOContext.BILLING);
        }
        this.prefix = prefix.toUpperCase();
        this.sequence = new AtomicLong(initialSequence);
    }
    /**
     * Genera el siguiente número de factura en la secuencia.
     *
     * @return instancia de InvoiceNumber válida
     */
    public InvoiceNumber next() {
        long nextValue = sequence.incrementAndGet();
        return InvoiceNumber.from(prefix, nextValue);
    }

    /**
     * Obtiene el último número generado sin avanzar la secuencia.
     *
     * @return instancia de InvoiceNumber actual
     */
    public InvoiceNumber current() {
        long currentValue = sequence.get();
        return InvoiceNumber.from(prefix, currentValue);
    }

    /**
     * Reinicia la secuencia a un valor específico.
     *
     * @param newValue nuevo valor de la secuencia
     */
    public void reset(long newValue) {
        if (newValue < 0) {
            throw new ValueObjectValidationException(BillingVOError.ERR_INVOICE_NUMBER_RESET_NEGATIVE,VOContext.BILLING);
        }
        sequence.set(newValue);
    }
}

