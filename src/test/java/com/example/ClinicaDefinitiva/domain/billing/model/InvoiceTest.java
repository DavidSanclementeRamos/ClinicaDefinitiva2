
package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.ProviderId;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

class InvoiceTest {

    private Invoice buildParticularInvoice() {
        return new Invoice.Builder()
                .id(InvoiceId.of(1L)) // 👈 obligatorio
                .patientId(PatientId.of(1L))
                .providerId(ProviderId.of(1L))
                .dentistId(DentistId.of(1L))
                .currency(CurrencyCode.of("COP"))
                .notes(Notes.of("Factura de prueba"))
                .dueDate(LocalDateTime.now().plusDays(5))
                .build();
    }

    private Invoice buildInstitutionalInvoice() {
        return new Invoice.Builder()
                .id(InvoiceId.of(2L)) // 👈 obligatorio
                .contractId(ContractId.of(10L))
                .patientId(PatientId.of(999L))
                .providerId(ProviderId.of(1L))
                .dentistId(DentistId.of(1L))
                .currency(CurrencyCode.of("COP"))
                .notes(Notes.of("Factura institucional"))
                .dueDate(LocalDateTime.now().plusDays(10))
                .build();
    }

    private InvoiceItem buildValidItem() {
        return InvoiceItem.builder()
                .id(InvoiceItemId.of(1L))
                .serviceId(ServiceId.of(10L))
                .serviceCode("SRV001")
                .serviceDescription("Consulta odontológica")
                .unitPrice(Price.of(200, Currency.getInstance("COP")))
                .quantity(Quantity.of(2))
                .rateId(RateId.of(5L))
                .performedAt(LocalDateTime.now())
                .build();
    }

    // ===== CREACIÓN =====

    @Test
    void shouldCreateInstitutionalInvoiceSuccessfully() {
        Invoice invoice = buildInstitutionalInvoice();
        assertNotNull(invoice.getContractId());
        assertEquals("COP", invoice.getCurrency().getCode());
        assertTrue(invoice.getStatus().isDraft());
    }

    @Test
    void shouldThrowExceptionWhenDueDateIsInvalid() {
        assertThrows(BusinessRuleViolationException.class, () ->
                new Invoice.Builder()
                        .id(InvoiceId.of(3L))
                        .patientId(PatientId.of(1L))
                        .providerId(ProviderId.of(1L))
                        .dentistId(DentistId.of(1L))
                        .currency(CurrencyCode.of("COP"))
                        .notes(Notes.of("Notas"))
                        .dueDate(LocalDateTime.now().minusDays(1)) // inválida
                        .build()
        );
    }

    // ===== AGREGACIÓN DE ÍTEMS =====

    @Test
    void shouldAddItemSuccessfullyAndRecalculateTotals() {
        Invoice invoice = buildParticularInvoice();
        invoice.addItem(buildValidItem());

        assertEquals(1, invoice.getItems().size());
        assertTrue(invoice.getSubtotal().asBigDecimal().intValue() > 0);
        assertEquals(invoice.getSubtotal().add(invoice.getTax()), invoice.getTotal());
    }

    @Test
    void shouldThrowExceptionWhenAddingItemWithDifferentCurrency() {
        Invoice invoice = buildParticularInvoice();
        Price unitPrice = Price.of(100, Currency.getInstance("USD"));
        InvoiceItem item = InvoiceItem.builder()
                .id(InvoiceItemId.of(2L))
                .serviceId(ServiceId.of(20L))
                .serviceCode("SRV002")
                .serviceDescription("Servicio en USD")
                .unitPrice(unitPrice)
                .quantity(Quantity.of(1))
                .rateId(RateId.of(6L))
                .performedAt(LocalDateTime.now())
                .build();

        assertThrows(BusinessRuleViolationException.class, () -> invoice.addItem(item));
    }

    // ===== EMISIÓN =====

    @Test
    void shouldEmitInvoiceSuccessfully() {
        Invoice invoice = buildParticularInvoice();
        invoice.addItem(buildValidItem());

        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("FAC", 0);
        invoice.emit(generator);

        assertNotNull(invoice.getNumber());
        assertTrue(invoice.getStatus().isPending());
    }

    @Test
    void shouldThrowExceptionWhenEmitWithoutItems() {
        Invoice invoice = buildParticularInvoice();
        InvoiceNumberGenerator generator = new InvoiceNumberGenerator("FAC", 0);

        assertThrows(BusinessRuleViolationException.class, () -> invoice.emit(generator));
    }

    // ===== CANCELACIÓN =====

    @Test
    void shouldCancelInvoiceSuccessfullyWithValidReason() {
        Invoice invoice = buildParticularInvoice();
        invoice.addItem(buildValidItem());

        invoice.cancel("Paciente canceló cita con anticipación");

        assertTrue(invoice.getStatus().isCancelled());
    }

    @Test
    void shouldThrowExceptionWhenCancelWithoutReason() {
        Invoice invoice = buildParticularInvoice();
        invoice.addItem(buildValidItem());

        assertThrows(BusinessRuleViolationException.class, () -> invoice.cancel(" "));
    }



}
