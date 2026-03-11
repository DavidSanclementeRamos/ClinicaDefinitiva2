
package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.billing.vo.ProviderId;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;



import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;


@DisplayName("Tests del Agregado Invoice")
class InvoiceTest {

    private PatientId patientId;
    private DentistId dentistId;
    private ProviderId providerId;
    private ContractId contractId;
    private CurrencyCode currencyCode;
    private Notes notes;
    private LocalDateTime dueDate;
    private InvoiceNumberGenerator generator;
    private ServiceId serviceId;
    private Price unitPrice;
    private Quantity quantity;

   /** @BeforeEach
    void setUp() {
        patientId = PatientId.of(1L);
        dentistId = DentistId.of(2L);
        providerId = ProviderId.of(3L);
        contractId = ContractId.of(4L);
        currencyCode = CurrencyCode.of("COP");
        notes = Notes.of("Factura de prueba");
        dueDate = LocalDateTime.now().plusDays(30);
        
       /** generator = new InvoiceNumberGenerator() {
            private int counter = 0;
            @Override
            public String next() {
                return "INV-" + (++counter);
            }
        };
        
        serviceId = ServiceId.of(100L);
        unitPrice = Price.of(150000, Currency.getInstance("COP"));
        quantity = Quantity.of(2);
    }

    @Nested
    @DisplayName("Tests de creación de Invoice")
    class InvoiceCreationTests {

        @Test
        @DisplayName("Debe crear una factura particular exitosamente")
        void shouldCreateParticularInvoiceSuccessfully() {
            // Act
            Invoice invoice = Invoice.createParticular(
                patientId, providerId, dentistId, currencyCode, notes, dueDate
            );

            // Assert
            assertNotNull(invoice);
            assertNull(invoice.getId());
            assertEquals(patientId, invoice.getPatientId());
            assertEquals(dentistId, invoice.getDentistId());
            assertEquals(providerId, invoice.getProviderId());
            assertEquals(currencyCode, invoice.getCurrency());
            assertEquals(notes, invoice.getNotes());
            assertEquals(dueDate, invoice.getDueDate());
            assertEquals("DRAFT", invoice.getStatus().getValue());
            assertTrue(invoice.getItems().isEmpty());
        }

        @Test
        @DisplayName("Debe crear una factura institucional exitosamente")
        void shouldCreateInstitutionalInvoiceSuccessfully() {
            // Act
            Invoice invoice = Invoice.createInstitutional(
                contractId, providerId, dentistId, currencyCode, notes, dueDate
            );

            // Assert
            assertNotNull(invoice);
            assertEquals(contractId, invoice.getContractId());
            assertNull(invoice.getPatientId());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la fecha de vencimiento es anterior a la fecha de creación")
        void shouldThrowExceptionWhenDueDateBeforeCreationDate() {
            // Arrange
            LocalDateTime invalidDueDate = LocalDateTime.now().minusDays(1);

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Invoice.createParticular(
                    patientId, providerId, dentistId, currencyCode, notes, invalidDueDate
                )
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_INVALID_DUE_DATE, exception.getCatalogo());
        }
    }

    @Nested
    @DisplayName("Tests de gestión de items")
    class InvoiceItemTests {

        private Invoice invoice;
        private InvoiceItem item;

        @BeforeEach
        void setUp() {
            invoice = Invoice.createParticular(
                patientId, providerId, dentistId, currencyCode, notes, dueDate
            );
            
            item = InvoiceItem.builder()
                .serviceId(serviceId)
                .serviceCode("SVC001")
                .serviceDescription("Limpieza dental")
                .unitPrice(unitPrice)
                .quantity(quantity)
                .performedAt(LocalDateTime.now())
                .build();
        }

        @Test
        @DisplayName("Debe agregar un item correctamente")
        void shouldAddItemSuccessfully() {
            // Act
            invoice.addItem(item);

            // Assert
            assertEquals(1, invoice.getItems().size());
            assertTrue(invoice.getItems().contains(item));
            assertEquals(unitPrice.multiply(quantity.getValue()), invoice.getTotal());
        }

        @Test
        @DisplayName("Debe lanzar excepción al agregar item con moneda diferente")
        void shouldThrowExceptionWhenAddingItemWithDifferentCurrency() {
            // Arrange
            Currency usd = Currency.getInstance("USD");
            Price differentCurrencyPrice = Price.of(200, usd);
            
            InvoiceItem invalidItem = InvoiceItem.builder()
                .serviceId(serviceId)
                .serviceCode("SVC002")
                .serviceDescription("Servicio en USD")
                .unitPrice(differentCurrencyPrice)
                .quantity(Quantity.of(1))
                .performedAt(LocalDateTime.now())
                .build();

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> invoice.addItem(invalidItem)
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_CURRENCY_MISMATCH, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe lanzar excepción al agregar item a factura no editable")
        void shouldThrowExceptionWhenAddingItemToNonEditableInvoice() {
            // Arrange
            invoice.addItem(item);
            invoice.emit(generator);

            InvoiceItem anotherItem = InvoiceItem.builder()
                .serviceId(serviceId)
                .serviceCode("SVC003")
                .serviceDescription("Otro servicio")
                .unitPrice(unitPrice)
                .quantity(Quantity.of(1))
                .performedAt(LocalDateTime.now())
                .build();

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> invoice.addItem(anotherItem)
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_NOT_EDITABLE, exception.getCatalogo());
        }
    }

    @Nested
    @DisplayName("Tests de emisión y estados")
    class InvoiceEmissionTests {

        private Invoice invoice;

        @BeforeEach
        void setUp() {
            invoice = Invoice.createParticular(
                patientId, providerId, dentistId, currencyCode, notes, dueDate
            );
            
            InvoiceItem item = InvoiceItem.builder()
                .serviceId(serviceId)
                .serviceCode("SVC001")
                .serviceDescription("Limpieza dental")
                .unitPrice(unitPrice)
                .quantity(quantity)
                .performedAt(LocalDateTime.now())
                .build();
            
            invoice.addItem(item);
        }

        @Test
        @DisplayName("Debe emitir la factura correctamente")
        void shouldEmitInvoiceSuccessfully() {
            // Act
            invoice.emit(generator);

            // Assert
            assertEquals("PENDING", invoice.getStatus().getValue());
            assertNotNull(invoice.getNumber());
        }

        @Test
        @DisplayName("Debe lanzar excepción al emitir factura sin items")
        void shouldThrowExceptionWhenEmittingEmptyInvoice() {
            // Arrange
            Invoice emptyInvoice = Invoice.createParticular(
                patientId, providerId, dentistId, currencyCode, notes, dueDate
            );

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> emptyInvoice.emit(generator)
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_NO_ITEMS, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe cancelar la factura correctamente")
        void shouldCancelInvoice() {
            // Arrange
            invoice.emit(generator);

            // Act
            invoice.cancel("Razón de cancelación válida con más de 10 caracteres");

            // Assert
            assertEquals("CANCELLED", invoice.getStatus().getValue());
        }

        @Test
        @DisplayName("Debe lanzar excepción al cancelar con razón muy corta")
        void shouldThrowExceptionWhenCancellingWithShortReason() {
            // Arrange
            invoice.emit(generator);

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> invoice.cancel("Corta")
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_CANCELLATION_REQUIRES_REASON, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe lanzar excepción al cancelar factura pagada")
        void shouldThrowExceptionWhenCancellingPaidInvoice() {
            // Arrange
            invoice.emit(generator);
            
            // Simular pago completo
            Price paymentAmount = invoice.getTotal();
            invoice.receivePayment(paymentAmount);

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> invoice.cancel("Razón de cancelación válida")
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_CANNOT_CANCEL_PAID, exception.getCatalogo());
        }
    }

    @Nested
    @DisplayName("Tests de gestión de pagos")
    class PaymentTests {

        private Invoice invoice;

        @BeforeEach
        void setUp() {
            invoice = Invoice.createParticular(
                patientId, providerId, dentistId, currencyCode, notes, dueDate
            );
            
            InvoiceItem item = InvoiceItem.builder()
                .serviceId(serviceId)
                .serviceCode("SVC001")
                .serviceDescription("Limpieza dental")
                .unitPrice(unitPrice)
                .quantity(quantity)
                .performedAt(LocalDateTime.now())
                .build();
            
            invoice.addItem(item);
            invoice.emit(generator);
        }

        @Test
        @DisplayName("Debe recibir un pago parcial correctamente")
        void shouldReceivePartialPayment() {
            // Arrange
            Price partialPayment = Price.of(100000, Currency.getInstance("COP"));

            // Act
            invoice.receivePayment(partialPayment);

            // Assert
            assertEquals(partialPayment, invoice.getTotalPaid());
            assertEquals("PENDING", invoice.getStatus().getValue());
            assertFalse(invoice.isFullyPaid());
            assertEquals(invoice.getTotal().subtract(partialPayment), invoice.getRemainingBalance());
        }

        @Test
        @DisplayName("Debe marcar como pagada cuando los pagos cubren el total")
        void shouldMarkAsPaidWhenPaymentsCoverTotal() {
            // Arrange
            Price fullPayment = invoice.getTotal();

            // Act
            invoice.receivePayment(fullPayment);

            // Assert
            assertEquals("PAID", invoice.getStatus().getValue());
            assertTrue(invoice.isFullyPaid());
            assertEquals(Price.zero(Currency.getInstance("COP")), invoice.getRemainingBalance());
            assertEquals(1, invoice.pullDomainEvents().size()); // InvoiceFullyPaidEvent
        }

        @Test
        @DisplayName("Debe acumular múltiples pagos hasta completar el total")
        void shouldAccumulateMultiplePaymentsUntilTotal() {
            // Arrange
            Price firstPayment = Price.of(100000, Currency.getInstance("COP"));
            Price secondPayment = Price.of(100000, Currency.getInstance("COP"));
            Price thirdPayment = Price.of(100000, Currency.getInstance("COP"));

            // Act & Assert
            invoice.receivePayment(firstPayment);
            assertEquals(firstPayment, invoice.getTotalPaid());
            assertFalse(invoice.isFullyPaid());

            invoice.receivePayment(secondPayment);
            assertEquals(firstPayment.add(secondPayment), invoice.getTotalPaid());
            assertFalse(invoice.isFullyPaid());

            invoice.receivePayment(thirdPayment);
            assertEquals(invoice.getTotal(), invoice.getTotalPaid());
            assertTrue(invoice.isFullyPaid());
            assertEquals("PAID", invoice.getStatus().getValue());
        }

        @Test
        @DisplayName("Debe lanzar excepción al recibir pago en factura no pendiente")
        void shouldThrowExceptionWhenReceivingPaymentOnNonPendingInvoice() {
            // Arrange
            invoice.cancel("Razón de cancelación válida con más de 10 caracteres");

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> invoice.receivePayment(Price.of(50000, Currency.getInstance("COP")))
            );
            
            assertEquals(InvoiceError.ERR_INVOICE_MUST_BE_PENDING_TO_PAY, exception.getCatalogo());
        }
    }*/
}