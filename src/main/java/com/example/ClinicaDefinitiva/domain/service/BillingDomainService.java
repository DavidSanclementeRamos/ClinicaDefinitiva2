package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.InvoiceItemBuilder;
import com.example.ClinicaDefinitiva.application.dto.ServiceRendered;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.administration.model.Contract;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.InvoiceFactory;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Money;
import com.example.ClinicaDefinitiva.domain.portsInput.ContractRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.RateRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BillingDomainService {
    private final ContractRepository contractRepo;
    private final RateRepository rateRepo;

    public BillingDomainService(ContractRepository contractRepo, RateRepository rateRepo) {
        this.contractRepo = contractRepo;
        this.rateRepo = rateRepo;
    }

    /**
     * Construye y valida una factura lista para persistir.
     * No persiste nada; la capa de aplicación debe abrir la transacción y guardar.
     */
    public Invoice buildInvoice(Patient patient, String providerId, List<ServiceRendered> services, LocalDateTime issuedAt) {
        //No aseguramos de la existencia de un convenio relacionado con ese paciente
       // Long contractId
        Contract contract = contractRepo.findById(patient.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("Contract not found, el paciente no tiene ese convenio"));

        // Si la relation existe se orquestan las reglas de negocio de un Contract
        if (!contract.isActiveAt(issuedAt)) {
            if (contract.isExpiredAt(issuedAt)) {
                throw new IllegalArgumentException("Contract expired");
            } else {
                throw new IllegalArgumentException("Contract not active (status not ACTIVE)");
            }
        }

        // Determinar moneda/payer por el primer ítem (se exigirá uniformidad)
        ServiceRendered first = services.get(0);
        Rate firstRate = rateRepo.findActiveRateForService(first.getServiceCode(), patient.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("Rate not found for service " + first.getServiceCode()));

        if (!firstRate.isValidAt(first.getPerformedAt())) {
            throw new IllegalArgumentException("Rate not valid at performedAt for service " + first.getServiceCode());
        }

        final Money invoiceCurrency = firstRate.getCurrency();
        final String invoicePayer = firstRate.getPayer_type();


        //  Invoice InvoiceFactory;
        Invoice invoice = InvoiceFactory.createDraft(patient.getId(), providerId, issuedAt, patient.getContractId(), invoiceCurrency, invoicePayer);

        for (ServiceRendered s : services) {
            Rate rate = rateRepo.findActiveRateForService(s.getServiceCode(), patient.getContractId())
                    .orElseThrow(() -> new IllegalArgumentException("Rate not found for service " + s.getServiceCode()));

            if (!rate.isValidAt(s.getPerformedAt())) {
                throw new IllegalArgumentException("Rate not valid at performedAt for service " + s.getServiceCode());
            }

            // Obligamos que todas las rates usen la misma moneda
            if (!invoiceCurrency.equals(rate.getCurrency())) {
                throw new IllegalArgumentException("Inconsistent rate currency for service " + s.getServiceCode() +
                        ". Expected " + invoiceCurrency + " but found " + rate.getCurrency());
            }


            double unitPrice = rate.getAmount();
            InvoiceItem item = new InvoiceItemBuilder()
                    .withId(UUID.randomUUID().toString())
                    .withService(s.getServiceCode())
                    .withDescription(s.getDescription())
                    .withQuantity(s.getQuantity())
                    .withUnitPrice(unitPrice)
                    .with(rate.getCurrency())
                    .withRateId(rate.getId())
                    .withPerformedAt(s.getPerformedAt())
                    .withProviderId(s.getProviderId())
                    // con esta simplificación unitPriceInvoice == unitPriceOriginal y sin exchange metadata
                    .withInvoiceUnitPrice(unitPrice)
                    .withInvoiceCurrency(rate.getCurrency())


                    .build();

            invoice.addItem(item);
        }

        invoice.validateBeforeEmit(); // chequea "al menos un ítem" y total positivo
        return invoice;


    }
}
