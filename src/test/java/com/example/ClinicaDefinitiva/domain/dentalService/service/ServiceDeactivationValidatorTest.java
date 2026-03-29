package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceDeactivationValidatorTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private ServiceDeactivationValidator validator;

    private final ServiceId serviceId = ServiceId.of(1L);

    @Test
    @DisplayName("Validar que no hay citas ni facturas asociadas -> pasa")
    void validateNoAppointments_noAssociations_shouldPass() {
        when(appointmentRepository.existsByServiceId(serviceId)).thenReturn(false);
        when(invoiceRepository.existsByServiceId(serviceId)).thenReturn(false);

        assertThatCode(() -> validator.validateNoAppointments(serviceId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Si hay facturas pendientes, lanza excepción")
    void validateNoAppointments_hasInvoices_shouldThrow() {
        when(invoiceRepository.existsByServiceId(serviceId)).thenReturn(true);

        assertThatThrownBy(() -> validator.validateNoAppointments(serviceId))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("No puede desactivarse porque tiene facturas pendientes");
    }

    @Test
    @DisplayName("Si hay citas pendientes, lanza excepción")
    void validateNoAppointments_hasAppointments_shouldThrow() {
        when(invoiceRepository.existsByServiceId(serviceId)).thenReturn(false);
        when(appointmentRepository.existsByServiceId(serviceId)).thenReturn(true);

        assertThatThrownBy(() -> validator.validateNoAppointments(serviceId))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("No puede desactivarse porque tiene citas programadas");
    }
}
