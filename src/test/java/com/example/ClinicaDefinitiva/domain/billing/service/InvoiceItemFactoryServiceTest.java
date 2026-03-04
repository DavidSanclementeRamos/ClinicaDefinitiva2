
package com.example.ClinicaDefinitiva.domain.billing.service;

import com.example.ClinicaDefinitiva.domain.billing.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCode;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceName;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.time.LocalDateTime;
import java.util.Currency;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class InvoiceItemFactoryServiceTest {

    private InvoiceItemFactoryService factory;

    @BeforeEach
    void setUp() {
        factory = new InvoiceItemFactoryService();
    }

    @Test
    void shouldCreateInvoiceItemSuccessfully() {
        ProvidedService service = mock(ProvidedService.class);
        when(service.getId()).thenReturn(ServiceId.of(100L));
        when(service.getCode()).thenReturn(ServiceCode.of("SRV001"));
        when(service.getName()).thenReturn(ServiceName.custom("Consulta"));

        Rate rate = mock(Rate.class);
        when(rate.getId()).thenReturn(RateId.of(5L));
        when(rate.getAmount()).thenReturn(Price.of(200, Currency.getInstance("COP")));

        Quantity quantity = Quantity.of(1);
        LocalDateTime performedAt = LocalDateTime.now();

        InvoiceItem item = factory.createFromRateSnapshot(
                InvoiceItemId.of(1L),
                service,
                rate,
                quantity,
                performedAt
        );

        //assertEquals(ServiceId.of(100L), item.getServiceId());
        assertEquals(100L, item.getServiceId().getId());
        assertEquals("SRV001", item.getServiceCode());
        assertEquals("Consulta", item.getServiceDescription());
        assertEquals(Price.of(200, Currency.getInstance("COP")), item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
        assertEquals(RateId.of(5L), item.getRateId());
        assertEquals(performedAt, item.getPerformedAt());
    }

    @Test
    void shouldThrowExceptionWhenServiceIsNull() {
        Rate rate = mock(Rate.class);
        when(rate.getId()).thenReturn(RateId.of(5L));
        when(rate.getAmount()).thenReturn(Price.of(200, Currency.getInstance("COP")));

        assertThrows(NullPointerException.class, () ->
                factory.createFromRateSnapshot(
                        InvoiceItemId.of(1L),
                        null,
                        rate,
                        Quantity.of(1),
                        LocalDateTime.now()
                ));
    }

    @Test
    void shouldThrowExceptionWhenRateIsNull() {
        ProvidedService service = mock(ProvidedService.class);
        when(service.getId()).thenReturn(ServiceId.of(100L));
        when(service.getCode()).thenReturn(ServiceCode.of("SRV001"));
        when(service.getName()).thenReturn(ServiceName.custom("Consulta"));

        assertThrows(NullPointerException.class, () ->
                factory.createFromRateSnapshot(
                        InvoiceItemId.of(1L),
                        service,
                        null,
                        Quantity.of(1),
                        LocalDateTime.now()
                ));
    }
}