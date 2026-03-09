
package com.example.ClinicaDefinitiva.domain.billing.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.vo.ProviderId;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceDomainServiceTest {

 /*   private ContractRepository contractRepository;
    private RateRepository rateRepository;
    private InvoiceDomainService service;

    @BeforeEach
    void setUp() {
        contractRepository = mock(ContractRepository.class);
        rateRepository = mock(RateRepository.class);
        service = new InvoiceDomainService(contractRepository, rateRepository);
    }

    @Test
    void shouldValidateInstitutionalContractSuccessfully() {
        Contract contract = mock(Contract.class);
        when(contract.isActiveAndValid()).thenReturn(true);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(10L),
                ProviderId.of(2L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
                
                
        );

        assertDoesNotThrow(() -> service.validateInstitutionalContract(invoice));
    }

    @Test
    void shouldThrowExceptionWhenInstitutionalContractMissing() {
        when(contractRepository.findById(any())).thenReturn(Optional.empty());

        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(10L),
                ProviderId.of(2L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
        );

        assertThrows(BusinessRuleViolationException.class,
                () -> service.validateInstitutionalContract(invoice));
    }

    @Test
    void shouldThrowExceptionWhenInstitutionalContractNotValid() {
        Contract contract = mock(Contract.class);
        when(contract.isActiveAndValid()).thenReturn(false);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(30L),
                ProviderId.of(5L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
        );

        assertThrows(BusinessRuleViolationException.class,
                () -> service.validateInstitutionalContract(invoice));
    }

    @Test
    void shouldValidateRatesSuccessfully() {
        Rate rate = mock(Rate.class);
        doNothing().when(rate).ensureValidAt(any());
        when(rateRepository.findActiveRateForService(any(), any())).thenReturn(Optional.of(rate));

        InvoiceItem item = InvoiceItem.fromRateSnapshot(
                ServiceId.of(100L),
                "SRV001",
                "Consulta",
                RateId.of(5L),
                Price.of(200, Currency.getInstance("COP")),
                Quantity.of(1),
                
                LocalDateTime.now().plusDays(7)
        );

        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(10L),
                ProviderId.of(2L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
        );

        assertDoesNotThrow(() -> service.validateRates(invoice, LocalDateTime.now()));
    }

    @Test
    void shouldThrowExceptionWhenRateMissing() {
        when(rateRepository.findActiveRateForService(any(), any())).thenReturn(Optional.empty());

        InvoiceItem item = InvoiceItem.fromRateSnapshot(
                ServiceId.of(200L),
                "SRV002",
                "Tratamiento",
                RateId.of(6L),

                Price.of(300, Currency.getInstance("COP")),
                Quantity.of(1),
                LocalDateTime.now().plusDays(7)
        );

        Invoice invoice = Invoice.createInstitutional(
               ContractId.of(10L),
                ProviderId.of(2L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
        );
        invoice.addItem(item); 

        assertThrows(BusinessRuleViolationException.class,
                () -> service.validateRates(invoice, LocalDateTime.now().plusDays(20)));
    }

    @Test
    void shouldThrowExceptionWhenRateNotValidAtDate() {
        Rate rate = mock(Rate.class);
        doThrow(new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_EXPIRED_RATE,
                EntityContext.INVOICE
        )).when(rate).ensureValidAt(any());

        when(rateRepository.findActiveRateForService(any(), any())).thenReturn(Optional.of(rate));

        InvoiceItem item = InvoiceItem.fromRateSnapshot(
                ServiceId.of(300L),
                "SRV003",
                "Radiografía",
                RateId.of(7L),
                Price.of(400, Currency.getInstance("COP")),
                Quantity.of(1),
                
                
                LocalDateTime.now().plusDays(7)
        );

        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(10L),
                ProviderId.of(2L),
                DentistId.of(5L),
                CurrencyCode.of("COP"),
                Notes.of("Notes"),
                LocalDateTime.now().plusDays(7)
        );
        invoice.addItem(item); 

        assertThrows(BusinessRuleViolationException.class,
                () -> service.validateRates(invoice, LocalDateTime.now().plusDays(67)));
    }*/
}
