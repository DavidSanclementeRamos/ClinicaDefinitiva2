package com.example.ClinicaDefinitiva.application.actor.service;

import com.example.ClinicaDefinitiva.application.actor.dto.dentist.*;
import com.example.ClinicaDefinitiva.application.actor.mapper.dentist.DentistReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.dentist.DentistWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
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
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistApplicationServiceTest {

    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private DentistReadMapper dentistReadMapper;
    @Mock
    private DentistWriteMapper dentistWriteMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private DentistApplicationService service;

    private final UserIdentityId requesterId = UserIdentityId.from(1L);
    private final RolId requesterRolId = RolId.of(100L);

    // ========== Métodos auxiliares ==========
    private ReadDentistDto createReadDentistDto() {
        return new ReadDentistDto(
            1L,                                           // dentistId
            "General Dentistry",                          // specialties
            "AVAILABLE",                                  // availabilityStatus
            new WorkingHoursDto(LocalTime.of(8,0), LocalTime.of(17,0), DayOfWeek.MONDAY, 40), // workingHoursDto
            "12345678",                                   // dni
            "Juan",                                       // first
            "Pérez",                                      // lastName
            "30",                                         // age
            "3001234567",                                 // phoneNumber
            LocalDate.now().minusYears(30),               // dateOfBirth
            "O+",                                         // bloodType
            "EPS123",                                     // documentoEPS
            "juan.perez@example.com",                     // user
            LocalDateTime.now(),                          // lastUpdate
            "Calle 123",                                  // street
            "Bogotá",                                     // city
            "Cundinamarca",                               // state
            "Colombia",                                   // country
            "110111"                                      // postalCode
        );
    }

    private PageDentistDto createPageDentistDto() {
        return new PageDentistDto(
            1L,
            "General Dentistry",
            "12345678",
            "Juan",
            "Pérez",
            "3001234567",
            "AVAILABLE"
        );
    }

    private UpdateDentistContactDto createUpdateContactDto() {
        return new UpdateDentistContactDto(
            Optional.of("Calle Nueva 456"),
            Optional.of("Medellín"),
            Optional.of("Antioquia"),
           Optional.of( "Colombia"),
            Optional.of("050001"),
            Optional.of("3011234567")
        );
    }

    private UpdateDentistSensitiveDto createUpdateSensitiveDto() {
        WorkingHoursDto whDto = new WorkingHoursDto(
            LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40
        );
        return new UpdateDentistSensitiveDto(
            Optional.of("General Dentistry"),
            Optional.of(whDto),
            Optional.of("87654321"),
            Optional.of("María"),
            Optional.of("Gómez"),
            Optional.of("35"),
            Optional.of(LocalDate.now().minusYears(35)),
            Optional.of("A+"),
            Optional.of("EPS456")
        );
    }

    // Helper para crear un dentista de prueba
    private Dentist createDentist() {
        Person person = createPerson();
        Specialties specialties = Specialties.of(Set.of(Specialty.of("General Dentistry")));
        UserIdentityId userId = UserIdentityId.from(2L);
        WorkingHours workingHours = WorkingHours.of(
                LocalTime.of(8, 0), LocalTime.of(17, 0),
                DayOfWeek.MONDAY, 40);
        return Dentist.registerDentist(person, specialties, userId, workingHours);
    }

    private Person createPerson() {
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
        DentistId id = DentistId.of(1L);
        Dentist dentist = createDentist();
        ReadDentistDto expectedDto = createReadDentistDto();

        when(dentistRepository.findById(id)).thenReturn(Optional.of(dentist));
        when(dentistReadMapper.toReadDto(dentist)).thenReturn(expectedDto);

        ReadDentistDto result = service.findById(id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.READ),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("findById - lanza excepción cuando no existe")
    void findById_shouldThrowWhenNotFound() {
        DentistId id = DentistId.of(1L);
        when(dentistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id, requesterId, requesterRolId))
                .isInstanceOf(DentistNotFoundException.class);
    }

    @Test
    @DisplayName("findAll - debe retornar página de DTOs")
    void findAll_shouldReturnPage() {
        Pageable pageable = Pageable.unpaged();
        Dentist dentist = createDentist();
        Page<Dentist> page = new PageImpl<>(List.of(dentist));
        PageDentistDto dto = createPageDentistDto();
        when(dentistRepository.findAll(pageable)).thenReturn(page);
        when(dentistReadMapper.toPageDto(dentist)).thenReturn(dto);

        Page<PageDentistDto> result = service.findAll(pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }


   
  
    @Test
@DisplayName("save - debe crear dentista y retornar DTO")
void save_shouldCreateAndReturnDto() {
    // Crear un DTO real con valores predecibles
    CreateDentistDto dto = new CreateDentistDto(
        "General Dentistry",
        "AVAILABLE",
        new WorkingHoursDto(LocalTime.of(8,0), LocalTime.of(17,0), DayOfWeek.MONDAY, 40),
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

    Person person = createPerson();
    Specialties specialties = Specialties.of(Set.of(Specialty.of("General Dentistry")));
    UserIdentityId userId = UserIdentityId.from(2L);
    WorkingHours workingHours = WorkingHours.of(LocalTime.of(8,0), LocalTime.of(17,0), DayOfWeek.MONDAY, 40);

    // Stubs necesarios (solo los que el servicio realmente usa)
    when(dentistWriteMapper.toPerson(any(CreateDentistDto.class))).thenReturn(person);
    when(dentistWriteMapper.toSpecialties(anyString())).thenReturn(specialties);
    when(dentistWriteMapper.toUserIdentityId(any(CreateDentistDto.class))).thenReturn(userId);
    when(dentistWriteMapper.toWorkingHours(any(WorkingHoursDto.class))).thenReturn(workingHours);

    Dentist dentist = Dentist.registerDentist(person, specialties, userId, workingHours);
    ReadDentistDto expectedDto = createReadDentistDto();

    when(dentistRepository.save(any(Dentist.class))).thenReturn(dentist);
    when(dentistReadMapper.toReadDto(dentist)).thenReturn(expectedDto);

    ReadDentistDto result = service.save(dto, requesterId, requesterRolId);

    assertThat(result).isEqualTo(expectedDto);
    verify(dentistRepository).save(any(Dentist.class));
}

   @Test
@DisplayName("updateContactData - debe actualizar contacto")
void updateContactData_shouldUpdate() {
    Long id = 1L;
    UpdateDentistContactDto dto = createUpdateContactDto();
    Dentist dentist = createDentist();

    when(dentistRepository.findById(DentistId.of(id))).thenReturn(Optional.of(dentist));
    when(dentistRepository.save(dentist)).thenReturn(dentist);  // ← NUEVO
    ReadDentistDto expectedDto = createReadDentistDto();
    when(dentistReadMapper.toReadDto(dentist)).thenReturn(expectedDto);

    ReadDentistDto result = service.updateContactData(dto, id, requesterId, requesterRolId);

    assertThat(result).isEqualTo(expectedDto);
    verify(dentistRepository).save(dentist);
    verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
            eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
            any(AuthorizationContext.class));
}

@Test
@DisplayName("updateSensitiveData - debe actualizar datos sensibles")
void updateSensitiveData_shouldUpdate() {
    Long id = 1L;
    UpdateDentistSensitiveDto dto = createUpdateSensitiveDto();
    Dentist dentist = createDentist();

    when(dentistRepository.findById(DentistId.of(id))).thenReturn(Optional.of(dentist));
    when(dentistRepository.save(dentist)).thenReturn(dentist);  // ← NUEVO
    ReadDentistDto expectedDto = createReadDentistDto();
    when(dentistReadMapper.toReadDto(dentist)).thenReturn(expectedDto);

    ReadDentistDto result = service.updateSensitiveData(dto, id, requesterId, requesterRolId);

    assertThat(result).isEqualTo(expectedDto);
    verify(dentistRepository).save(dentist);
    verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
            eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
            any(AuthorizationContext.class));
}

    @Test
    @DisplayName("applyVacation - debe aplicar vacaciones al dentista autenticado")
    void applyVacation_shouldApply() {
        Dentist dentist = createDentist();
        when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(10);

        service.applyVacation(start, end, requesterId, requesterRolId);

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION));
        assertThat(dentist.getVacationStart()).isEqualTo(start);
        assertThat(dentist.getVacationEnd()).isEqualTo(end);
        verify(dentistRepository).save(dentist);
    }

    @Test
    @DisplayName("applyIncapacity - debe aplicar incapacidad")
    void applyIncapacity_shouldApply() {
        Dentist dentist = createDentist();
        when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusDays(3);
        String note = "Fiebre";

        service.applyIncapacity(start, end, note, requesterId, requesterRolId);

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE));
        assertThat(dentist.getIncapacityStart()).isEqualTo(start);
        assertThat(dentist.getIncapacityEnd()).isEqualTo(end);
        assertThat(dentist.getIncapacityNote()).isEqualTo(note);
        verify(dentistRepository).save(dentist);
    }

    @Test
    @DisplayName("returnToAvailable - debe volver a disponible")
    void returnToAvailable_shouldReturn() {
        Dentist dentist = createDentist();
        when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));
        // Aplicar vacaciones primero
        dentist.applyVacation(LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        service.returnToAvailable(requesterId, requesterRolId);

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE));
        assertThat(dentist.getVacationStart()).isNull();
        verify(dentistRepository).save(dentist);
    }

    @Test
    @DisplayName("deleteById - debe eliminar dentista")
    void deleteById_shouldDelete() {
        DentistId id = DentistId.of(1L);
        Dentist dentist = createDentist();
        when(dentistRepository.findById(id)).thenReturn(Optional.of(dentist));

        service.deleteById(id, requesterId, requesterRolId);

        verify(dentistRepository).deleteById(dentist.getDentistId());
    }
}