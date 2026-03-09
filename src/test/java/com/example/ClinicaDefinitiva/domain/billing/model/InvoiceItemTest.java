
package com.example.ClinicaDefinitiva.domain.billing.model;


import static org.junit.jupiter.api.Assertions.*;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.vo.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;


@DisplayName("Tests del Value Object InvoiceItem")
class InvoiceItemTest {

    private ServiceId serviceId;
    private String serviceCode;
    private String serviceDescription;
    private Price unitPrice;
    private Quantity quantity;
    private RateId rateId;
    private LocalDateTime performedAt;
    private Currency cop;

    @BeforeEach
    void setUp() {
        cop = Currency.getInstance("COP");
        serviceId = ServiceId.of(1L);
        serviceCode = "SVC001";
        serviceDescription = "Limpieza dental completa";
        unitPrice = Price.of(150000, cop);
        quantity = Quantity.of(2);
        rateId = RateId.of(100L);
        performedAt = LocalDateTime.now();
    }

    @Test
    @DisplayName("Debe crear un InvoiceItem correctamente usando builder")
    void shouldCreateInvoiceItemUsingBuilder() {
        // Act
        InvoiceItem item = InvoiceItem.builder()
            .id(InvoiceItemId.of(1L))
            .serviceId(serviceId)
            .serviceCode(serviceCode)
            .serviceDescription(serviceDescription)
            .unitPrice(unitPrice)
            .quantity(quantity)
            .rateId(rateId)
            .performedAt(performedAt)
            .build();

        // Assert
        assertNotNull(item);
        assertEquals(serviceId, item.getServiceId());
        assertEquals(serviceCode, item.getServiceCode());
        assertEquals(serviceDescription, item.getServiceDescription());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
        assertEquals(rateId, item.getRateId());
        assertEquals(performedAt, item.getPerformedAt());
    }

    @Test
    @DisplayName("Debe calcular el precio total correctamente")
    void shouldCalculateTotalPriceCorrectly() {
        // Arrange
        InvoiceItem item = InvoiceItem.builder()
            .serviceId(serviceId)
            .serviceCode(serviceCode)
            .serviceDescription(serviceDescription)
            .unitPrice(unitPrice)
            .quantity(quantity)
            .performedAt(performedAt)
            .build();

        // Act
        Price totalPrice = item.getTotalPrice();

        // Assert
        assertEquals(Price.of(300000, cop), totalPrice);
    }

    @Test
    @DisplayName("Debe crear InvoiceItem sin campos opcionales")
    void shouldCreateInvoiceItemWithoutOptionalFields() {
        // Act
        InvoiceItem item = InvoiceItem.builder()
            .serviceId(serviceId)
            .serviceCode(serviceCode)
            .serviceDescription(serviceDescription)
            .unitPrice(unitPrice)
            .quantity(quantity)
            .performedAt(performedAt)
            .build();

        // Assert
        assertNotNull(item);
        assertNull(item.getId());
        assertNull(item.getRateId());
        assertEquals(serviceId, item.getServiceId());
        assertEquals(serviceCode, item.getServiceCode());
        assertEquals(serviceDescription, item.getServiceDescription());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
        assertEquals(performedAt, item.getPerformedAt());
    }
}