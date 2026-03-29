package com.example.ClinicaDefinitiva.application.schedule.service;

import com.example.ClinicaDefinitiva.application.exceptions.scheduled.AppointmentNotFoundException;
import com.example.ClinicaDefinitiva.application.schedule.service.AppointmentApplicationService;
import com.example.ClinicaDefinitiva.application.schedule.dto.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.dto.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.mapper.AppointmentReadMapper;
import com.example.ClinicaDefinitiva.application.schedule.mapper.AppointmentWriteMapper;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentApplicationServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentSchedulingService schedulingService;
    @Mock
    private AppointmentReadMapper readMapper;
    @Mock
    private AppointmentWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private AppointmentApplicationService service;

    @Test
    @DisplayName("Crear cita exitosamente")
    void save_shouldReturnDto() {
        CreateAppointmentDto dto = new CreateAppointmentDto(
                1L, 1L, 1L,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1),
                "ROUTINE_CHECKUP", "Revisión"
        );
        Appointment appointment = mock(Appointment.class);
        Appointment saved = mock(Appointment.class);
        ReadAppointmentDto resultDto = mock(ReadAppointmentDto.class);

        when(writeMapper.toDentistId(dto)).thenReturn(DentistId.of(1L));
        when(writeMapper.toPatientId(dto)).thenReturn(PatientId.of(1L));
        when(writeMapper.toStart(dto)).thenReturn(dto.start());
        when(writeMapper.toEnd(dto)).thenReturn(dto.end());
        when(writeMapper.toType(dto)).thenReturn(AppointmentType.ROUTINE_CHECKUP);
        when(writeMapper.toReason(dto)).thenReturn(dto.reason());
        when(writeMapper.toServiceId(dto)).thenReturn(ServiceId.of(1L));
        when(schedulingService.scheduleAppointment(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(saved);
        when(readMapper.toReadDto(saved)).thenReturn(resultDto);

        ReadAppointmentDto result = service.save(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(resultDto);
        verify(schedulingService).scheduleAppointment(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Buscar cita por ID")
    void findById_shouldReturnDto() {
        AppointmentId id = AppointmentId.of(1L);
        Appointment appointment = mock(Appointment.class);
        ReadAppointmentDto dto = mock(ReadAppointmentDto.class);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        when(readMapper.toReadDto(appointment)).thenReturn(dto);

        ReadAppointmentDto result = service.findById(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(dto);
    }

    @Test
    @DisplayName("Cancelar cita exitosamente")
    void cancel_shouldReturnDto() {
        AppointmentId id = AppointmentId.of(1L);
        Appointment appointment = mock(Appointment.class);
        ReadAppointmentDto dto = mock(ReadAppointmentDto.class);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(readMapper.toReadDto(appointment)).thenReturn(dto);

        ReadAppointmentDto result = service.cancel(id, "Motivo válido para cancelar", mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(dto);
        verify(appointment).cancel("Motivo válido para cancelar");
    }

    @Test
    @DisplayName("Cancelar cita inexistente lanza excepción")
    void cancel_notFound_shouldThrow() {
        AppointmentId id = AppointmentId.of(999L);
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(id, "Razón", mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(AppointmentNotFoundException.class);
    }
}
