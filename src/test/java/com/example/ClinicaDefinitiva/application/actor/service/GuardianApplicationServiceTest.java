package com.example.ClinicaDefinitiva.application.actor.service;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.mapper.patient.PatientReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.patient.PatientWriteMapper;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardianApplicationServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ReceptionRepository receptionRepository;
    @Mock
    private PatientReadMapper readMapper;
    @Mock
    private PatientWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private PatientApplicationService service;

    private final UserIdentityId requesterId = UserIdentityId.from(1L);
    private final RolId requesterRolId = RolId.of(100L);

    // ========== Métodos auxiliares ==========
    private ReadPatientDto createReadPatientDto() {
        return new ReadPatientDto(
            1L,
            100L,          // guardianId
            200L,          // contractId
            "12345678",
            "Juan",
            "Pérez",
            "30",
            "3001234567",
            LocalDate.now().minusYears(30),
            "O+",
            "EPS123",
            2L,
            LocalDateTime.now(),
            "Calle 123",
            "Bogotá",
            "Cundinamarca",
            "Colombia",
            "110111"
        );
    }

    private PagePatientDto createPagePatientDto() {
        return new PagePatientDto(
            1L,
            200L,
            45L,
            "12345678",
            "Juan",
            "Pérez",
            "3001234567"
        );
    }

    private UpdatePatientContactDto createUpdatePatientContactDto() {
        return new UpdatePatientContactDto(
           Optional.of( "Calle Nueva 456"),
           Optional.of( "Medellín"),
           Optional.of( "Antioquia"),
           Optional.of( "Colombia"),
           Optional.of( "050001"),
           Optional.of( "3011234567")
        );
    }

    private UpdatePatientSensitiveDto createUpdatePatientSensitiveDto() {
        return new UpdatePatientSensitiveDto(
            Optional.of("87654321"),
            Optional.of("María"),
            Optional.of("Gómez"),
            Optional.of("35"),
            Optional.of(LocalDate.now().minusYears(35)),
            Optional.of("A+"),
            Optional.of("EPS456")
        );
    }

    private Patient createPatient() {
        Person person = createAdultPerson();
        UserIdentityId userId = UserIdentityId.from(2L);
        return Patient.registerPatient(person, userId, null);
    }

    private Patient createPatientWithId() {
        Patient patient = createPatient();
        return Patient.reconstruct(
            PatientId.of(1L),
            patient.getUser(),
            patient.getGuardianId(),
            patient.getLastUpdate(),
            patient.getContractId(),
            patient.getTreatments(),
            patient.getPerson()
        );
    }

    private Person createAdultPerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");
        return Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
    }

    // ========== Tests ==========
    @Test
    @DisplayName("findById - debe retornar DTO cuando existe")
    void findById_shouldReturnDto() {
        PatientId id = PatientId.of(1L);
        Patient patient = createPatient();
        ReadPatientDto expectedDto = createReadPatientDto();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(readMapper.toReadDto(patient)).thenReturn(expectedDto);

        ReadPatientDto result = service.findById(id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.PATIENT), eq(ActionCatalog.BasicAction.READ),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("findAll - debe retornar página")
    void findAll_shouldReturnPage() {
        Pageable pageable = Pageable.unpaged();
        Patient patient = createPatient();
        Page<Patient> page = new PageImpl<>(List.of(patient));
        PagePatientDto dto = createPagePatientDto();
        when(patientRepository.findAll(pageable)).thenReturn(page);
        when(readMapper.toPageDto(patient)).thenReturn(dto);

        Page<PagePatientDto> result = service.findAll(pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("findByContractId - debe retornar página")
    void findByContractId_shouldReturnPage() {
        ContractId contractId = ContractId.of(1L);
        Pageable pageable = Pageable.unpaged();
        Patient patient = createPatient();
        Page<Patient> page = new PageImpl<>(List.of(patient));
        PagePatientDto dto = createPagePatientDto();
        when(patientRepository.findByContractId(contractId, pageable)).thenReturn(page);
        when(readMapper.toPageDto(patient)).thenReturn(dto);

        Page<PagePatientDto> result = service.findByContractId(contractId, pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("findByGuardianId - debe retornar página")
    void findByGuardianId_shouldReturnPage() {
        GuardianId guardianId = GuardianId.fromLong(1L);
        Pageable pageable = Pageable.unpaged();
        Patient patient = createPatient();
        Page<Patient> page = new PageImpl<>(List.of(patient));
        PagePatientDto dto = createPagePatientDto();
        when(patientRepository.findByGuardianId(guardianId, pageable)).thenReturn(page);
        when(readMapper.toPageDto(patient)).thenReturn(dto);

        Page<PagePatientDto> result = service.findByGuardianId(guardianId, pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("save - debe crear paciente")
    void save_shouldCreate() {
        CreatePatientDto dto = mock(CreatePatientDto.class);
        Person person = createAdultPerson();
        UserIdentityId userId = UserIdentityId.from(2L);
        GuardianId guardianId = null;

        when(writeMapper.toPerson(dto)).thenReturn(person);
        when(writeMapper.toUserIdentityId(dto)).thenReturn(userId);
        when(writeMapper.toGuardianId(dto)).thenReturn(guardianId);

        Patient patient = Patient.registerPatient(person, userId, guardianId);
        ReadPatientDto expectedDto = createReadPatientDto();

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(readMapper.toReadDto(patient)).thenReturn(expectedDto);

        ReadPatientDto result = service.save(dto, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    @DisplayName("updateContactData - debe actualizar contacto")
    void updateContactData_shouldUpdate() {
        PatientId id = PatientId.of(1L);
        UpdatePatientContactDto dto = createUpdatePatientContactDto(); // DTO real
        Patient patient = createPatientWithId();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        ReadPatientDto expectedDto = createReadPatientDto();
        when(readMapper.toReadDto(patient)).thenReturn(expectedDto);

        ReadPatientDto result = service.updateContactData(dto, id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(patientRepository).save(patient);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.PATIENT), eq(ActionCatalog.BasicAction.UPDATE),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("updateSensitiveData - debe actualizar datos sensibles")
    void updateSensitiveData_shouldUpdate() {
        PatientId id = PatientId.of(1L);
        UpdatePatientSensitiveDto dto = createUpdatePatientSensitiveDto(); // DTO real
        Patient patient = createPatientWithId();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        ReadPatientDto expectedDto = createReadPatientDto();
        when(readMapper.toReadDto(patient)).thenReturn(expectedDto);

        ReadPatientDto result = service.updateSensitiveData(dto, id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(patientRepository).save(patient);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.PATIENT), eq(ActionCatalog.BasicAction.UPDATE),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("deleteById - debe eliminar paciente")
    void deleteById_shouldDelete() {
        PatientId id = PatientId.of(1L);
        Patient patient = createPatientWithId(); // paciente con ID
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        service.deleteById(id, requesterId, requesterRolId);

        verify(patientRepository).deleteById(id);
    }
} 