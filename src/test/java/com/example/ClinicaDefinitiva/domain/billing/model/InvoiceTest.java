package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.billing.service.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.vo.*;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.event.InvoiceFullyPaidEvent;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class InvoiceTest {

    private static final Currency COP = Currency.getInstance("COP");
    private static final Price ZERO = Price.zero(COP);
    private static final Price TOTAL = Price.of(100_000, COP);

    @Test
    @DisplayName("INV-UNIT-001: Crear factura institucional en estado DRAFT")
    void createInstitutionalInvoice_shouldBeDraft() {
        Invoice invoice = Invoice.createInstitutional(
                ContractId.of(1L),
                ProviderId.of(1L),
                DentistId.of(1L),
                CurrencyCode.of("COP"),
                Notes.of("Factura institucional"),
                LocalDateTime.now().plusDays(30)
        );

        assertThat(invoice.getStatus().getValue()).isEqualTo(InvoiceStatus.Status.DRAFT);
        assertThat(invoice.getItems()).isEmpty();
        assertThat(invoice.getTotal()).isEqualTo(ZERO);
    }

    @Test
    @DisplayName("INV-UNIT-002: Agregar ítem a factura en borrador")
    void addItem_shouldRecalculateTotals() {
        Invoice invoice = createDraftInvoice();

        InvoiceItem item = InvoiceItem.builder()
                .unitPrice(Price.of(50_000, COP))
                .quantity(Quantity.of(2))
                .build();

        invoice.addItem(item);

        assertThat(invoice.getSubtotal().asBigDecimal()).isEqualByComparingTo("100000");
        assertThat(invoice.getTotal().asBigDecimal()).isEqualByComparingTo("100000");
        assertThat(invoice.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("INV-UNIT-003: Emitir factura sin ítems lanza excepción")
    void emit_withoutItems_shouldThrow() {
        Invoice invoice = createDraftInvoice();

        assertThatThrownBy(() -> invoice.emit(mockInvoiceNumberGenerator()))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("La factura debe tener al menos un ítem antes de emitir");
    }

    @Test
    @DisplayName("INV-UNIT-004: Recibir pago parcial no cambia estado")
    void receivePayment_partial_shouldNotComplete() {
        Invoice invoice = createInvoiceWithTotal(TOTAL);

        invoice.receivePayment(Price.of(30_000, COP));

        assertThat(invoice.getTotalPaid().asBigDecimal()).isEqualByComparingTo("30000");
        assertThat(invoice.getStatus().getValue()).isEqualTo(InvoiceStatus.Status.PENDING);
        assertThat(invoice.isFullyPaid()).isFalse();
        assertThat(invoice.getRemainingBalance().asBigDecimal()).isEqualByComparingTo("70000");
    }

    @Test
    @DisplayName("INV-UNIT-005: Recibir pago que completa factura la marca como PAID y publica evento")
    void receivePayment_full_shouldMarkPaidAndPublishEvent() {
        Invoice invoice = createInvoiceWithTotal(TOTAL);

        invoice.receivePayment(TOTAL);

        assertThat(invoice.getStatus().getValue()).isEqualTo(InvoiceStatus.Status.PAID);
        assertThat(invoice.isFullyPaid()).isTrue();

        // Verificar eventos
        assertThat(invoice.pullDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(InvoiceFullyPaidEvent.class);
    }

    @Test
    @DisplayName("INV-UNIT-016: Solo facturas PENDING pueden recibir pagos")
    void receivePayment_whenNotPending_shouldThrow() {
        Invoice invoice = createInvoiceWithTotal(TOTAL);
        invoice.receivePayment(TOTAL); // ahora PAID

        assertThatThrownBy(() -> invoice.receivePayment(Price.of(10_000, COP)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("La factura debe estar en estado PENDING para poder registrarse como pagada");
    }

    // Métodos auxiliares
    private Invoice createDraftInvoice() {
        return Invoice.createParticular(
                PatientId.of(1L),
                ProviderId.of(1L),
                DentistId.of(1L),
                CurrencyCode.of("COP"),
                Notes.of("Factura de prueba"),
                LocalDateTime.now().plusDays(30)
        );
    }

    private Invoice createInvoiceWithTotal(Price total) {
        Invoice invoice = createDraftInvoice();
        InvoiceItem item = InvoiceItem.builder()
                .unitPrice(total)   // simplificado para prueba
                .quantity(Quantity.one())
                .build();
        invoice.addItem(item);
        invoice.emit(mockInvoiceNumberGenerator());
        return invoice;
    }

    private InvoiceNumberGenerator mockInvoiceNumberGenerator() {
    return new InvoiceNumberGenerator("FAC", 0) {
        @Override
        public InvoiceNumber next() {
            return InvoiceNumber.of("FAC-0001");
        }
    };
}
}
