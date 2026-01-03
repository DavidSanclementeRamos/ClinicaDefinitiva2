package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.application.dto.billing.BuildInvoiceRequest;
import com.example.ClinicaDefinitiva.application.dto.billing.InvoiceDto;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ServiceRendered;
import com.example.ClinicaDefinitiva.application.dto.billing.UpdateInvoiceRequest;
import com.example.ClinicaDefinitiva.application.mapper.InvoiceMapper;
import com.example.ClinicaDefinitiva.application.usecase.BillingUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ContractRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.RateRepository;
import com.example.ClinicaDefinitiva.domain.service.InvoiceFactoryDomainService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class BillingApplicationService implements BillingUseCase {

    private static final int DEFAULT_PAYMENT_DAYS = 30;

    private final ContractRepository contractRepository;
    private final RateRepository rateRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper mapper;

    public BillingApplicationService(ContractRepository contractRepository,
                                     RateRepository rateRepository,
                                     InvoiceRepository invoiceRepository,
                                     InvoiceMapper mapper) {
        this.contractRepository = contractRepository;
        this.rateRepository = rateRepository;
        this.invoiceRepository = invoiceRepository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceDto buildInvoice(BuildInvoiceRequest request) {
        // convierto una vez el contractId del request a VO
        ContractId contractId = ContractId.fromString(request.contractId);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + request.contractId));

        if (!contract.isActiveAt(request.issuedAt)) {
            if (contract.isExpiredAt(request.issuedAt)) throw new IllegalArgumentException("Contract expired");
            throw new IllegalArgumentException("Contract not active");
        }

        List<ServiceRendered> services = request.services.stream()
                .map(mapper::toServiceRenderedDomain)
                .collect(Collectors.toList());

        List<Rate> rates = new ArrayList<>();
        for (ServiceRendered s : services) {
            Rate rate = rateRepository.findActiveRateForService(s.getServiceCode(), contractId)
                    .orElseThrow(() -> new IllegalArgumentException("Rate not found for service " + s.getServiceCode()));
            if (!rate.isValidAt(s.getPerformedAt()))
                throw new IllegalArgumentException("Rate not valid at performedAt for service " + s.getServiceCode());
            rates.add(rate);
        }

        // calcular dueDate: prioridad request.dueDate -> contract policy -> default
        LocalDateTime dueDate = computeDueDate(request.issuedAt, request.dueDate, contract);

        Invoice invoice = InvoiceFactoryDomainService.createDraft(
                mapper.nextInvoiceId(),                     // InvoiceId VO
                mapper.toPatientId(request.patientId),     // PatientId VO
                mapper.toProviderId(request.providerId),   // DentistId VO
                services,
                rates,
                request.issuedAt,
                dueDate,
                ContractId.fromString(String.valueOf(contractId))                                 // pasamos el VO ContractId
        );

        invoiceRepository.save(invoice);

        return mapper.toInvoiceDto(invoice);
    }

    private LocalDateTime computeDueDate(LocalDateTime issuedAt, LocalDateTime explicitDueDate, Contract contract) {
        if (explicitDueDate != null) {
            if (explicitDueDate.isBefore(issuedAt))
                throw new IllegalArgumentException("Due date cannot be before issued date");
            return explicitDueDate;
        }
        Integer paymentDays = contract.getPaymentTermsDays();
        if (paymentDays != null) {
            return issuedAt.plusDays(paymentDays);
        }
        return issuedAt.plusDays(DEFAULT_PAYMENT_DAYS);
    }

    @Override
    public InvoiceDto findId(Long invoiceId) {
        // convertir Long -> InvoiceId VO y buscar
       InvoiceId id = InvoiceId.fromString(String.valueOf(invoiceId));
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));
        return mapper.toInvoiceDto(invoice);
    }

    @Override
    public InvoiceDto update(UpdateInvoiceRequest invoiceRequest) {
        InvoiceId invoiceId = mapper.invoiceIdFromString(invoiceRequest.invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceRequest.invoiceId));

        // obtener ContractId: preferir el de la petición (si viene) sino el del invoice
        ContractId contractId = ContractId.fromString(invoiceRequest.contractId);
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        List<ServiceRendered> services = invoiceRequest.services == null ? Collections.emptyList()
                : invoiceRequest.services.stream().map(mapper::toServiceRenderedDomain).collect(Collectors.toList());

        List<Rate> rates = new ArrayList<>();
        for (ServiceRendered s : services) {
            Rate rate = rateRepository.findActiveRateForService(s.getServiceCode(), contractId)
                    .orElseThrow(() -> new IllegalArgumentException("Rate not found for service " + s.getServiceCode()));
            if (!rate.isValidAt(s.getPerformedAt()))
                throw new IllegalArgumentException("Rate not valid at performedAt for service " + s.getServiceCode());
            rates.add(rate);
        }

        LocalDateTime issuedAt = invoiceRequest.issuedAt != null ? invoiceRequest.issuedAt : invoice.getIssuedAt();
        LocalDateTime dueDate = computeDueDate(issuedAt, invoiceRequest.dueDate, contract);

        Invoice updated = InvoiceFactoryDomainService.update(
                invoice,
                services,
                rates,
                issuedAt,
                dueDate,
                invoiceRequest.notes
        );

        invoiceRepository.save(updated);
        return mapper.toInvoiceDto(updated);
    }

    @Override
    public Page<InvoiceDto> findAll(Pageable pageable) {
        List<Invoice> all = invoiceRepository.findAll();
        List<InvoiceDto> dtos = all.stream().map(mapper::toInvoiceDto).collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    public void deleById(Long id) {
        // convertir Long -> InvoiceId VO y delegar al repo que usa VO
        InvoiceId invoiceId = InvoiceId.fromString(String.valueOf(id));
        invoiceRepository.deleteById(invoiceId);
    }
}



