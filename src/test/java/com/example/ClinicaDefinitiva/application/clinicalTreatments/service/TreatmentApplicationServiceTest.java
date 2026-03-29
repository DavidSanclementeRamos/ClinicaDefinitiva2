package com.example.ClinicaDefinitiva.application.clinicalTreatments.service;

import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.TreatmentDto;
import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.TreatmentPhaseDto;
import com.example.ClinicaDefinitiva.application.clinicalTreatments.mapper.TreatmentReadMapper;
import com.example.ClinicaDefinitiva.application.clinicalTreatments.mapper.TreatmentWriteMapper;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.output.TreatmentRepository;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentApplicationServiceTest {

    @Mock
    private TreatmentRepository treatmentRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private TreatmentReadMapper readMapper;
    @Mock
    private TreatmentWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private TreatmentApplicationService service;

    @Test
    @DisplayName("Crear tratamiento exitosamente")
    void create_shouldReturnDto() {
        // 1. Preparar DTO con fases válidas
        TreatmentPhaseDto phaseDto = new TreatmentPhaseDto(
                "Fase 1", 
                LocalDate.now().plusDays(1), 
                "PENDING", 
                "Descripción de la fase"
        );
        CreateTreatmentDto dto = new CreateTreatmentDto(
                1L, 2L, 3L,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(1),
                List.of(phaseDto),
                "Notas del tratamiento", // mínimo 3 caracteres
                4L
        );

        // 2. Crear una fase de dominio válida (para usar en el tratamiento real)
        TreatmentPhase validPhase = TreatmentPhase.of(
                Name.of("Fase 1"),
                LocalDate.now().plusDays(1),
                PhaseStatus.PENDING,
                Notes.of("Descripción de la fase")
        );

        // 3. Construir el tratamiento real que se guardará
        Treatment realTreatment = Treatment.createNew(
                PatientId.of(1L),
                DentistId.of(2L),
                ServiceId.of(3L),
                dto.startDate(),
                dto.expectedEndDate(),
                List.of(validPhase),
                dto.notes(),
                RateId.of(4L)
        );

        // 4. Configurar los mocks del mapper
        when(writeMapper.toPatientId(dto)).thenReturn(PatientId.of(1L));
        when(writeMapper.toDentistId(dto)).thenReturn(DentistId.of(2L));
        when(writeMapper.toServiceId(dto)).thenReturn(ServiceId.of(3L));
        when(writeMapper.toStartDate(dto)).thenReturn(dto.startDate());
        when(writeMapper.toExpectedEndDate(dto)).thenReturn(dto.expectedEndDate());
        when(writeMapper.toPhases(dto)).thenReturn(List.of(validPhase));
        when(writeMapper.toNotes(dto)).thenReturn(dto.notes());
        when(writeMapper.toRateId(dto)).thenReturn(RateId.of(4L));

        when(treatmentRepository.save(any(Treatment.class))).thenReturn(realTreatment);
        TreatmentDto resultDto = mock(TreatmentDto.class);
        when(readMapper.toDto(realTreatment)).thenReturn(resultDto);

        // 5. Ejecutar
        TreatmentDto result = service.create(dto, mock(UserIdentityId.class), mock(RolId.class));

        // 6. Verificar
        assertThat(result).isSameAs(resultDto);
        verify(treatmentRepository).save(any(Treatment.class));
    }

    // Los demás tests (complete_shouldCallDomainAndSave, complete_treatmentNotFound_shouldThrow) se mantienen igual
}
