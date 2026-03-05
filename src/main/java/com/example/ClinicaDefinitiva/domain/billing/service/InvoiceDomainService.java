package com.example.ClinicaDefinitiva.domain.billing.service;


import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDateTime;



import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;

/**
 * Domain Service: InvoiceDomainService
 *
 * Centraliza validaciones externas al agregado Invoice:
 * - RN-INVOICE-003: Tarifas vigentes
 * - RN-INVOICE-007: Facturas institucionales requieren contrato
 * - RN-INVOICE-016: El contrato es la entidad pagadora institucional
 */
public final class InvoiceDomainService {

    private final ContractRepository contractRepository;
    private final RateRepository rateRepository;

    public InvoiceDomainService(ContractRepository contractRepository,
                                RateRepository rateRepository) {
        this.contractRepository = contractRepository;
        this.rateRepository = rateRepository;
    }

    /**
     * Valida que el contrato institucional exista y esté activo/vigente.
     */
    public void validateInstitutionalContract(Invoice invoice) {
        if (invoice.getContractId() != null) {
            Contract contract = contractRepository.findById(invoice.getContractId())
                    .orElseThrow(() -> new BusinessRuleViolationException(
                            InvoiceError.ERR_INVOICE_MISSING_CONTRACT,
                            EntityContext.INVOICE
                    ));

            if (!contract.isActiveAndValid()) {
                throw new BusinessRuleViolationException(
                        InvoiceError.ERR_INVOICE_CONTRACT_NOT_VALID,
                        EntityContext.INVOICE
                );
            }
        }
    }

    /**
     * Valida que todas las tarifas de los ítems estén vigentes en la fecha de emisión.
     */
    public void validateRates(Invoice invoice, LocalDateTime emitDate) {
        for (InvoiceItem item : invoice.getItems()) {
            Rate rate = rateRepository.findActiveRateForService(item.getServiceId(), invoice.getContractId())
                    .orElseThrow(() -> new BusinessRuleViolationException(
                            InvoiceError.ERR_INVOICE_EXPIRED_RATE,
                            EntityContext.INVOICE
                    ));

            rate.ensureValidAt(emitDate);
        }
    }
}


