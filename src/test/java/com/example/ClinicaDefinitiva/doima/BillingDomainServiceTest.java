package com.example.ClinicaDefinitiva.doima;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ServiceRendered;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.administration.accounting.Contract;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ContractRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.RateRepository;
import com.example.ClinicaDefinitiva.domain.service.BillingDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingDomainServiceTest {

    private ContractRepository contractRepo;
    private RateRepository rateRepo;
    private BillingDomainService billingService;

    private Patient patient;
    private Contract contract;
    private Rate rate;

    @BeforeEach
    void setUp() {
        contractRepo = mock(ContractRepository.class);
        rateRepo = mock(RateRepository.class);
        billingService = new BillingDomainService(contractRepo, rateRepo);

        patient = new Patient("p1", 123L );
        contract = mock(Contract.class);
        rate = mock(Rate.class);
    }

    @Nested
    class HappyPath {
        @Test
        void buildInvoice_successful() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered service = new ServiceRendered("SVC1", "Consulta", 1, issuedAt,"prov1",  "COP");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(true);

            when(rateRepo.findActiveRateForService("SVC1", patient.getContractId())).thenReturn(Optional.of(rate));
            when(rate.isValidAt(issuedAt)).thenReturn(true);
            when(rate.getCurrency()).thenReturn(Price.COP);
            when(rate.getPayer_type()).thenReturn("EPS");
            when(rate.getAmount()).thenReturn(50000.0);
            when(rate.getId()).thenReturn("rate-1");

            Invoice invoice = billingService.buildInvoice(patient, "prov1", List.of(service), issuedAt);

            assertNotNull(invoice);
            assertEquals(1, invoice.getItems().size());
            assertEquals("COP", invoice.getCurrency().getCode());
        }
    }

    @Nested
    class ContractValidation {
        @Test
        void contractNotFound_throwsException() {
            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(), LocalDateTime.now()));
        }

        @Test
        void contractExpired_throwsException() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered service = new ServiceRendered("SVC1", "desc", 1, issuedAt, "prov1");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(false);
            when(contract.isExpiredAt(issuedAt)).thenReturn(true);

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(service), issuedAt));
        }

        @Test
        void contractInactiveButNotExpired_throwsException() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered service = new ServiceRendered("SVC1", "desc", 1, issuedAt, "prov1");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(false);
            when(contract.isExpiredAt(issuedAt)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(service), issuedAt));
        }
    }

    @Nested
    class RateValidation {
        @Test
        void rateNotFound_throwsException() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered service = new ServiceRendered("SVC1", "Consulta", 1, issuedAt, "prov1");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(true);

            when(rateRepo.findActiveRateForService("SVC1", patient.getContractId())).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(service), issuedAt));
        }

        @Test
        void rateInvalidAtDate_throwsException() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered service = new ServiceRendered("SVC1", "Consulta", 1, issuedAt, "prov1");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(true);

            when(rateRepo.findActiveRateForService("SVC1", patient.getContractId())).thenReturn(Optional.of(rate));
            when(rate.isValidAt(issuedAt)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(service), issuedAt));
        }

        @Test
        void inconsistentCurrency_throwsException() {
            LocalDateTime issuedAt = LocalDateTime.now();
            ServiceRendered s1 = new ServiceRendered("SVC1", "Consulta", 1, issuedAt, "prov1");
            ServiceRendered s2 = new ServiceRendered("SVC2", "Examen", 1, issuedAt, "prov1");

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(true);

            // Primer rate
            when(rateRepo.findActiveRateForService("SVC1", patient.getContractId())).thenReturn(Optional.of(rate));
            when(rate.isValidAt(issuedAt)).thenReturn(true);
            when(rate.getCurrency()).thenReturn(new Price("COP"));
            when(rate.getPayer_type()).thenReturn("EPS");
            when(rate.getAmount()).thenReturn(50000.0);
            when(rate.getId()).thenReturn("rate-1");

            // Segundo rate con otra moneda
            Rate rate2 = mock(Rate.class);
            when(rateRepo.findActiveRateForService("SVC2", patient.getContractId())).thenReturn(Optional.of(rate2));
            when(rate2.isValidAt(issuedAt)).thenReturn(true);
            when(rate2.getCurrency()).thenReturn(new Price("USD"));
            when(rate2.getAmount()).thenReturn(100.0);
            when(rate2.getId()).thenReturn("rate-2");

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(s1, s2), issuedAt));
        }
    }

    @Nested
    class InvoiceValidation {
        @Test
        void emptyServices_shouldFailValidation() {
            LocalDateTime issuedAt = LocalDateTime.now();

            when(contractRepo.findById(patient.getContractId())).thenReturn(Optional.of(contract));
            when(contract.isActiveAt(issuedAt)).thenReturn(true);

            assertThrows(IllegalArgumentException.class, () ->
                    billingService.buildInvoice(patient, "prov1", List.of(), issuedAt));
        }
    }
}
