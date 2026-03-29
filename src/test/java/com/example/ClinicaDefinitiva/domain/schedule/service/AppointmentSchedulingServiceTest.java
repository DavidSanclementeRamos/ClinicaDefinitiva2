package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentSchedulingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private ProvidedServiceRepository serviceRepository;

    @InjectMocks
    private AppointmentSchedulingService schedulingService;

    private static final DentistId DENTIST_ID = DentistId.of(1L);
    private static final PatientId PATIENT_ID = PatientId.of(1L);
    private static final ServiceId SERVICE_ID = ServiceId.of(1L);
    private static final LocalDateTime START = LocalDateTime.now().plusDays(3).withHour(10).withMinute(0);
    private static final LocalDateTime END = START.plusHours(1);
    private static final AppointmentType TYPE = AppointmentType.ROUTINE_CHECKUP;
    private static final String REASON = "Revisión rutinaria";

    // NO usar @BeforeEach con stubs globales

    @Test
    @DisplayName("SCH-INT-001: Agendar cita exitosamente")
    void scheduleAppointment_success() {
        // Configurar mocks específicos para este test
        Shift shift = mock(Shift.class);
        when(shift.canAccommodateAppointment(any(), any())).thenReturn(true);
        when(shiftRepository.findActiveByDentistAndDate(any(), any())).thenReturn(List.of(shift));
        
        ProvidedService service = mock(ProvidedService.class);
        when(service.isActive()).thenReturn(true);
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        
        when(appointmentRepository.findConflictingForDentist(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of());
        when(appointmentRepository.findConflictingForPatient(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of());
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArgument(0));

        Appointment result = schedulingService.scheduleAppointment(
                DENTIST_ID, PATIENT_ID, START, END, TYPE, REASON, SERVICE_ID
        );

        assertThat(result).isNotNull();
        assertThat(result.getDentistId()).isEqualTo(DENTIST_ID);
        assertThat(result.getPatientId()).isEqualTo(PATIENT_ID);
        assertThat(result.getServiceId()).isEqualTo(SERVICE_ID);
        assertThat(result.getStatus().isScheduled()).isTrue();
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Agendar cita sin turno activo lanza excepción")
    void scheduleAppointment_noShift_throws() {
        // Solo configurar lo necesario para este test
        when(shiftRepository.findActiveByDentistAndDate(any(), any())).thenReturn(List.of());
        // No configurar serviceRepository porque el código no llega a usarlo

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(
                DENTIST_ID, PATIENT_ID, START, END, TYPE, REASON, SERVICE_ID))
                .isInstanceOf(BusinessRuleViolationException.class);
        
        // Verificar que no se llamó a serviceRepository
        verify(serviceRepository, never()).findById(any());
    }

    @Test
@DisplayName("Agendar cita con conflicto de horario para dentista")
void scheduleAppointment_dentistConflict_throws() {
    // Configurar shift
    Shift shift = mock(Shift.class);
    when(shift.canAccommodateAppointment(any(), any())).thenReturn(true);
    when(shiftRepository.findActiveByDentistAndDate(any(), any())).thenReturn(List.of(shift));
    
    // Configurar conflicto para dentista - esto es lo que hace fallar el test
    Appointment conflictingAppointment = mock(Appointment.class);
    when(appointmentRepository.findConflictingForDentist(any(), any(), any(), anyBoolean()))
            .thenReturn(List.of(conflictingAppointment));
    
    // NO configurar findConflictingForPatient (no se necesita porque la excepción ocurre antes)
    // NO configurar serviceRepository (no se necesita porque la excepción ocurre antes)
    // NO configurar isActive en service (no se necesita)

    assertThatThrownBy(() -> schedulingService.scheduleAppointment(
            DENTIST_ID, PATIENT_ID, START, END, TYPE, REASON, SERVICE_ID))
            .isInstanceOf(BusinessRuleViolationException.class);
    
    // Verificar que no se llegó a verificar el servicio
    verify(serviceRepository, never()).findById(any());
}

    @Test
    @DisplayName("Agendar cita con servicio inactivo lanza excepción")
    void scheduleAppointment_inactiveService_throws() {
        // Configurar shift
        Shift shift = mock(Shift.class);
        when(shift.canAccommodateAppointment(any(), any())).thenReturn(true);
        when(shiftRepository.findActiveByDentistAndDate(any(), any())).thenReturn(List.of(shift));
        
        // Configurar servicio inactivo
        ProvidedService service = mock(ProvidedService.class);
        when(service.isActive()).thenReturn(false);
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        
        // No hay conflictos
        when(appointmentRepository.findConflictingForDentist(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of());
        when(appointmentRepository.findConflictingForPatient(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> schedulingService.scheduleAppointment(
                DENTIST_ID, PATIENT_ID, START, END, TYPE, REASON, SERVICE_ID))
                .isInstanceOf(BusinessRuleViolationException.class);
    }
}