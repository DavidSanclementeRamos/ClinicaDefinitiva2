
package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Currency;

class InvoiceItemTest {

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

    @Test
    void shouldCreateInvoiceItemWithBuilderSuccessfully() {
        InvoiceItem item = buildValidItem();

        assertEquals("SRV001", item.getServiceCode());
        assertEquals("Consulta odontológica", item.getServiceDescription());
        assertEquals(2, item.getQuantity().getValue());
        assertEquals(400, item.getTotalPrice().asBigDecimal().intValue());
        assertNotNull(item.getPerformedAt());
    }

    @Test
    void shouldCreateInvoiceItemFromRateSnapshotSuccessfully() {
        InvoiceItem item = InvoiceItem.fromRateSnapshot(
                ServiceId.of(20L),
                "SRV002",
                "Tratamiento dental",
                RateId.of(6L),
                Price.of(100, Currency.getInstance("COP")),
                Quantity.of(3),
                LocalDateTime.now()
        );

        assertEquals("SRV002", item.getServiceCode());
        assertEquals("Tratamiento dental", item.getServiceDescription());
        assertEquals(3, item.getQuantity().getValue());
        assertNotNull(item.getRateId());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                InvoiceItem.builder()
                        .id(null)
                        .serviceId(ServiceId.of(10L))
                        .serviceCode("SRV003")
                        .serviceDescription("Servicio inválido")
                        .unitPrice(Price.of(100, Currency.getInstance("COP")))
                        .quantity(Quantity.of(1))
                        .rateId(RateId.of(7L))
                        .performedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Test
    void shouldThrowExceptionWhenServiceIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                InvoiceItem.builder()
                        .id(InvoiceItemId.of(3L))
                        .serviceId(null)
                        .serviceCode("SRV004")
                        .serviceDescription("Servicio inválido")
                        .unitPrice(Price.of(100, Currency.getInstance("COP")))
                        .quantity(Quantity.of(1))
                        .rateId(RateId.of(7L))
                        .performedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNull() {
        assertThrows(NullPointerException.class, () ->
                InvoiceItem.builder()
                        .id(InvoiceItemId.of(4L))
                        .serviceId(ServiceId.of(40L))
                        .serviceCode("SRV005")
                        .serviceDescription("Servicio inválido")
                        .unitPrice(null)
                        .quantity(Quantity.of(1))
                        .rateId(RateId.of(7L))
                        .performedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        Price unitPrice = Price.of(150, Currency.getInstance("COP"));
        InvoiceItem item = InvoiceItem.builder()
                .id(InvoiceItemId.of(5L))
                .serviceId(ServiceId.of(50L))
                .serviceCode("SRV006")
                .serviceDescription("Radiografía")
                .unitPrice(unitPrice)
                .quantity(Quantity.of(4))
                .rateId(RateId.of(8L))
                .performedAt(LocalDateTime.now())
                .build();

        assertEquals(600, item.getTotalPrice().asBigDecimal().intValue());
    }
}
