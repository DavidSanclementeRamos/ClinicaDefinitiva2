package com.example.ClinicaDefinitiva.domain.billing.service;


import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;

import java.time.LocalDateTime;



import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;

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
        private final ProvidedServiceRepository serviceRepository;

    public InvoiceDomainService(ContractRepository contractRepository, RateRepository rateRepository, ProvidedServiceRepository serviceRepository) {
        this.contractRepository = contractRepository;
        this.rateRepository = rateRepository;
        this.serviceRepository = serviceRepository;
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
    
    // Valida que un servicio este activo al momento de factura.
    public void validanteService(ServiceId serviceId){
                
            ProvidedService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        ProvidedServiceError.ERR_SERVICE_NOT_FOUND,
                        EntityContext.DENTAL_SERVICE
                ));

        if (!service.isActive()) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_INACTIVE,
                    EntityContext.DENTAL_SERVICE
            );
        }
    }
}


