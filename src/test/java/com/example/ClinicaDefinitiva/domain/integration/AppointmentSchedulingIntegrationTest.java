
package com.example.ClinicaDefinitiva.domain.integration;


import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCode;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceName;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Integración: AppointmentSchedulingService")
class AppointmentSchedulingIntegrationTest {

  /**  @Mock
    private AppointmentRepository appointmentRepository;
    
    @Mock
    private ShiftRepository shiftRepository;
    
    @Mock
    private ProvidedServiceRepository serviceRepository;
    
    @InjectMocks
    private AppointmentSchedulingService schedulingService;

    private DentistId dentistId;
    private PatientId patientId;
    private ServiceId serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate date;
    private AppointmentType appointmentType;
    private String reason;
    private ProvidedService service;
    private Shift shift;

    @BeforeEach
    void setUp() {
        dentistId = DentistId.of(1L);
        patientId = PatientId.of(2L);
        serviceId = ServiceId.of(3L);
        date = LocalDate.now().plusDays(5);
        startTime = LocalDateTime.of(date, LocalTime.of(10, 0));
        endTime = LocalDateTime.of(date, LocalTime.of(11, 0));
        appointmentType = AppointmentType.CONSULTATION;
        reason = "Dolor de muela";
        
        Currency cop = Currency.getInstance("COP");
        service = ProvidedService.builder()
            .id(serviceId)
            .name(ServiceName.custom("Limpieza"))
            .code(ServiceCode.of("SVC001"))
            .baseRate(Price.of(100000, cop))
            .duration(ServiceDuration.of(Duration.ofMinutes(60)))
            .build();
        
        shift = Shift.create(
            dentistId,
            date,
            LocalTime.of(8, 0),
            LocalTime.of(17, 0),
            ShiftType.CLINICAL
        );
    }

    @Test
    @DisplayName("Debe agendar una cita exitosamente cuando todo está válido")
    void shouldScheduleAppointmentSuccessfully() {
        // Arrange
        when(shiftRepository.findActiveByDentistAndDate(dentistId, date))
            .thenReturn(List.of(shift));
        
        when(appointmentRepository.findConflictingForDentist(dentistId, startTime, endTime, true))
            .thenReturn(List.of());
        
        when(appointmentRepository.findConflictingForPatient(patientId, startTime, endTime, true))
            .thenReturn(List.of());
        
        when(serviceRepository.findById(serviceId))
            .thenReturn(Optional.of(service));
        
        when(appointmentRepository.save(any(Appointment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Appointment appointment = schedulingService.scheduleAppointment(
            dentistId, patientId, startTime, endTime, appointmentType, reason, serviceId
        );

        // Assert
        assertNotNull(appointment);
        assertEquals(dentistId, appointment.getDentistId());
        assertEquals(patientId, appointment.getPatientId());
        assertEquals(serviceId, appointment.getServiceId());
        assertEquals(startTime, appointment.getStart());
        assertEquals(endTime, appointment.getEnd());
        assertEquals(appointmentType, appointment.getAppointmentType());
        assertEquals(reason, appointment.getReason());
        
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no hay turno activo")
    void shouldThrowExceptionWhenNoActiveShift() {
        // Arrange
        when(shiftRepository.findActiveByDentistAndDate(dentistId, date))
            .thenReturn(List.of());

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
            BusinessRuleViolationException.class,
            () -> schedulingService.scheduleAppointment(
                dentistId, patientId, startTime, endTime, appointmentType, reason, serviceId
            )
        );
        
        // Verificar que se lanza la excepción esperada
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando hay conflicto de horario con el dentista")
    void shouldThrowExceptionWhenDentistHasConflict() {
        // Arrange
        when(shiftRepository.findActiveByDentistAndDate(dentistId, date))
            .thenReturn(List.of(shift));
        
        Appointment conflictingAppointment = mock(Appointment.class);
        when(appointmentRepository.findConflictingForDentist(dentistId, startTime, endTime, true))
            .thenReturn(List.of(conflictingAppointment));

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
            BusinessRuleViolationException.class,
            () -> schedulingService.scheduleAppointment(
                dentistId, patientId, startTime, endTime, appointmentType, reason, serviceId
            )
        );
        
        assertEquals(AppointmentError.ERR_APPT_DENTIST_TIME_CONFLICT, exception.getCatalogo());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando hay conflicto de horario con el paciente")
    void shouldThrowExceptionWhenPatientHasConflict() {
        // Arrange
        when(shiftRepository.findActiveByDentistAndDate(dentistId, date))
            .thenReturn(List.of(shift));
        
        when(appointmentRepository.findConflictingForDentist(dentistId, startTime, endTime, true))
            .thenReturn(List.of());
        
        Appointment conflictingAppointment = mock(Appointment.class);
        when(appointmentRepository.findConflictingForPatient(patientId, startTime, endTime, true))
            .thenReturn(List.of(conflictingAppointment));

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
            BusinessRuleViolationException.class,
            () -> schedulingService.scheduleAppointment(
                dentistId, patientId, startTime, endTime, appointmentType, reason, serviceId
            )
        );
        
        assertEquals(AppointmentError.ERR_APPT_PATIENT_TIME_CONFLICT, exception.getCatalogo());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el servicio no está activo")
    void shouldThrowExceptionWhenServiceIsInactive() {
        // Arrange
        when(shiftRepository.findActiveByDentistAndDate(dentistId, date))
            .thenReturn(List.of(shift));
        
        when(appointmentRepository.findConflictingForDentist(dentistId, startTime, endTime, true))
            .thenReturn(List.of());
        
        when(appointmentRepository.findConflictingForPatient(patientId, startTime, endTime, true))
            .thenReturn(List.of());
        
        ProvidedService inactiveService = ProvidedService.builder()
            .id(serviceId)
            .name(ServiceName.custom("Inactivo"))
            .code(ServiceCode.of("SVC002"))
            .baseRate(Price.of(100000, Currency.getInstance("COP")))
            .build();
        inactiveService.deactivate("Razón de prueba");
        
        when(serviceRepository.findById(serviceId))
            .thenReturn(Optional.of(inactiveService));

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
            BusinessRuleViolationException.class,
            () -> schedulingService.scheduleAppointment(
                dentistId, patientId, startTime, endTime, appointmentType, reason, serviceId
            )
        );
        
        assertEquals("ERR_SERVICE_INACTIVE", exception.getCatalogo().toString());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cita abarca múltiples días")
    void shouldThrowExceptionWhenAppointmentSpansMultipleDays() {
        // Arrange
        LocalDateTime multiDayStart = LocalDateTime.of(date, LocalTime.of(22, 0));
        LocalDateTime multiDayEnd = LocalDateTime.of(date.plusDays(1), LocalTime.of(1, 0));

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
            BusinessRuleViolationException.class,
            () -> schedulingService.scheduleAppointment(
                dentistId, patientId, multiDayStart, multiDayEnd, appointmentType, reason, serviceId
            )
        );
        
        assertEquals(AppointmentError.ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS, exception.getCatalogo());
    }

    @Test
    @DisplayName("Debe reprogramar una cita exitosamente")
    void shouldRescheduleAppointmentSuccessfully() {
        // Arrange
        Appointment originalAppointment = mock(Appointment.class);
        when(originalAppointment.getStatus()).thenReturn(mock(com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus.class));
        when(originalAppointment.getStatus().isEditable()).thenReturn(true);
        when(originalAppointment.isWithinNext24Hours(any(LocalDateTime.class))).thenReturn(false);
        when(originalAppointment.getId()).thenReturn(AppointmentId.of(1L));
        when(originalAppointment.getServiceId()).thenReturn(serviceId);
        when(originalAppointment.getAppointmentType()).thenReturn(appointmentType);
        when(originalAppointment.getReason()).thenReturn(reason);

        LocalDateTime newStart = startTime.plusDays(1);
        LocalDateTime newEnd = endTime.plusDays(1);
        LocalDate newDate = newStart.toLocalDate();

        Shift newShift = Shift.create(
            dentistId,
            newDate,
            LocalTime.of(8, 0),
            LocalTime.of(17, 0),
            ShiftType.CLINICAL
        );

        when(shiftRepository.findActiveByDentistAndDate(dentistId, newDate))
            .thenReturn(List.of(newShift));
        
        when(appointmentRepository.findConflictingForDentist(dentistId, newStart, newEnd, true))
            .thenReturn(List.of());
        
        when(appointmentRepository.findConflictingForPatient(patientId, newStart, newEnd, true))
            .thenReturn(List.of());
        
        when(appointmentRepository.save(any(Appointment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Appointment rescheduled = schedulingService.rescheduleAppointment(
            originalAppointment, dentistId, patientId, newStart, newEnd
        );

        // Assert
        assertNotNull(rescheduled);
        assertEquals(newStart, rescheduled.getStart());
        assertEquals(newEnd, rescheduled.getEnd());
        
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Debe cancelar una cita exitosamente")
    void shouldCancelAppointmentSuccessfully() {
        // Arrange
        Appointment appointment = mock(Appointment.class);
        String cancelReason = "El paciente canceló con anticipación";

        // Act
        schedulingService.cancelAppointment(appointment, cancelReason);

        // Assert
        verify(appointment, times(1)).cancel(cancelReason);
        verify(appointmentRepository, times(1)).save(appointment);
    }*/
}
