package com.example.ClinicaDefinitiva.domain.billing.service;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceDomainServiceTest {

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private RateRepository rateRepository;
    @Mock
    private ProvidedServiceRepository serviceRepository;

    @InjectMocks
    private InvoiceDomainService domainService;

    @Test
    @DisplayName("RN-INVOICE-007: Contrato institucional válido pasa")
    void validateInstitutionalContract_validContract_shouldNotThrow() {
        Invoice invoice = mock(Invoice.class);
        when(invoice.getContractId()).thenReturn(ContractId.of(1L));
        Contract contract = mock(Contract.class);
        when(contract.isActiveAndValid()).thenReturn(true);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        assertThatCode(() -> domainService.validateInstitutionalContract(invoice))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("RN-INVOICE-007: Contrato inexistente lanza excepción")
    void validateInstitutionalContract_missingContract_shouldThrow() {
        Invoice invoice = mock(Invoice.class);
        when(invoice.getContractId()).thenReturn(ContractId.of(1L));
        when(contractRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainService.validateInstitutionalContract(invoice))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Factura a EPS requiere contrato vigente. Asociar ContractId antes de emitir");
    }

    @Test
    @DisplayName("RN-INVOICE-003: Tarifas vigentes en la fecha de emisión")
    void validateRates_allRatesValid_shouldNotThrow() {
        Invoice invoice = mock(Invoice.class);
        InvoiceItem item = mock(InvoiceItem.class);
        when(item.getServiceId()).thenReturn(ServiceId.of(1L));
        when(invoice.getItems()).thenReturn(List.of(item));
        when(invoice.getContractId()).thenReturn(null);

        Rate rate = mock(Rate.class);
        doNothing().when(rate).ensureValidAt(any());

        when(rateRepository.findActiveRateForService(any(), any())).thenReturn(Optional.of(rate));

        assertThatCode(() -> domainService.validateRates(invoice, LocalDateTime.now()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("RN-INVOICE-003: Tarifa no encontrada lanza excepción")
    void validateRates_rateNotFound_shouldThrow() {
        Invoice invoice = mock(Invoice.class);
        InvoiceItem item = mock(InvoiceItem.class);
        when(item.getServiceId()).thenReturn(ServiceId.of(1L));
        when(invoice.getItems()).thenReturn(List.of(item));

        when(rateRepository.findActiveRateForService(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainService.validateRates(invoice, LocalDateTime.now()))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("No se puede emitir factura con tarifas vencidas. Validar vigencia de Rate al momento de facturar");
    }
}
