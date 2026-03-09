
package com.example.ClinicaDefinitiva.aplication.actor;

import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.service.actor.DentistApplicationService;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DentistApplicationService")
class DentistApplicationServiceTest {

  /**  @Mock private DentistRepository dentistRepository;
    @Mock private DentistReadMapper dentistReadMapper;
    @Mock private DentistWriteMapper dentistWriteMapper;
    @Mock private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private DentistApplicationService service;

    private final UserIdentityId requesterId = UserIdentityId.from(1L);
    private final RolId requesterRolId = RolId.of(10L);
    private Dentist dentist;

    // ── Helpers ─────────────────────────────────────────────────────────────────

    private Person buildPerson() {
        return Person.of(
                Address.of("Street 1", "Cali", "Valle", "Colombia", "760001"),
                Age.of(DateOfBirth.of(LocalDate.of(1990, 1, 1))),
                BloodType.fromLabel("O+"),
                DateOfBirth.of(LocalDate.of(1990, 1, 1)),
                Document.of("123456789"),
                "EPS123",
                FullName.of("John", "Doe"),
                PhoneNumber.of("3001234567")
        );
    }

    private ReadDentistDto buildReadDto(Dentist d) {
        return new ReadDentistDto(
                d.getDentistId().value(),
                d.getSpecialties().toString(),
                d.getAvailabilityStatus().toString(),
                d.getWorkingHours().getStart(),
                d.getWorkingHours().getEnd(),
                d.getWorkingHours().getDayOfWeek(),
                d.getWorkingHours().getDeclaredHoursPerWeek(),
                d.getPersonData().getDni().toString(),
                d.getPersonData().getFullname().FirstName(),
                d.getPersonData().getFullname().LastName(),
                d.getPersonData().getAge().toString(),
                d.getPersonData().getPhoneNumber().toString(),
                d.getPersonData().getDateOfBirth().asDate(),
                d.getPersonData().getBloodType().getValue(),
                d.getPersonData().getDocumentoEPS(),
                d.getUserId().value().toString(),
                d.getLastUpdate(),
                d.getPersonData().getAddress().Street(),
                d.getPersonData().getAddress().City(),
                d.getPersonData().getAddress().State(),
                d.getPersonData().getAddress().Country(),
                d.getPersonData().getAddress().PostalCode()
        );
    }

    // ── Setup ────────────────────────────────────────────────────────────────────

    @BeforeEach
    void setUp() {
        // withId simula que la persistencia ya asignó un DentistId
        dentist = Dentist.withId(
                DentistId.of(100L),
                buildPerson(),
                Specialties.of(Set.of(Specialty.of("Orthodontics"))),
                requesterId,
                WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(16, 0), DayOfWeek.MONDAY, 40),
                LocalDateTime.now()
        );
    }

    // ── Read ─────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Lectura de dentistas")
    class ReadTests {

        @Test
        @DisplayName("findById retorna DTO cuando autorizado")
        void findById_returnsDto_whenAuthorized() {
            when(dentistRepository.findById(dentist.getDentistId())).thenReturn(Optional.of(dentist));
            when(dentistReadMapper.toReadDto(dentist)).thenReturn(buildReadDto(dentist));

            ReadDentistDto result = service.findById(dentist.getDentistId(), requesterId, requesterRolId);

            assertEquals("John", result.first());
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.READ),
                    any(AuthorizationContext.class));
        }

        @Test
        @DisplayName("findById lanza excepción si no existe")
        void findById_throwsException_whenDentistNotFound() {
            when(dentistRepository.findById(DentistId.of(999L))).thenReturn(Optional.empty());

            assertThrows(DentistNotFoundException.class,
                    () -> service.findById(DentistId.of(999L), requesterId, requesterRolId));
        }
    }

    // ── Create ───────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Creación válida")
    class CreateTests {

        @Test
        @DisplayName("save persiste dentista cuando autorizado")
        void save_persistsDentist_whenAuthorized() {
            CreateDentistDto dto = new CreateDentistDto(
                    "Orthodontics", "AVAILABLE",
                    LocalTime.of(8, 0), LocalTime.of(16, 0), DayOfWeek.MONDAY, 40,
                    "123456789", "Jane", "Doe", "35", "3009876543",
                    LocalDate.of(1990, 1, 1), "O+", "EPS456", 2L, LocalDateTime.now(),
                    "Street 2", "Cali", "Valle", "Colombia", "760002"
            );

            // El dentista que devuelve el mapper ya tiene id (simulado por persistencia)
            Dentist savedDentist = Dentist.withId(
                    DentistId.of(200L),
                    buildPerson(),
                    Specialties.of(Set.of(Specialty.of("Orthodontics"))),
                    requesterId,
                    WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(16, 0), DayOfWeek.MONDAY, 40),
                    LocalDateTime.now()
            );

            when(dentistWriteMapper.fromCreateDto(dto)).thenReturn(savedDentist);
            when(dentistRepository.save(savedDentist)).thenReturn(savedDentist);
            when(dentistReadMapper.toReadDto(savedDentist)).thenReturn(buildReadDto(savedDentist));

            ReadDentistDto result = service.save(dto, requesterId, requesterRolId);

            assertEquals("John", result.first());
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.CREATE),
                    any(AuthorizationContext.class));
        }
    }

    // ── Update ───────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Actualización de datos")
    class UpdateTests {

        @Test
        @DisplayName("updateContactData actualiza dentista cuando autorizado")
        void updateContactData_updatesDentist_whenAuthorized() {
            UpdateDentistContactDto updateDto = new UpdateDentistContactDto(
                    "Street 3", "Cali", "Valle", "Colombia", "760003", "300111222"
            );

            when(dentistRepository.findById(dentist.getDentistId())).thenReturn(Optional.of(dentist));
            when(dentistRepository.save(dentist)).thenReturn(dentist);
            when(dentistReadMapper.toReadDto(dentist)).thenReturn(buildReadDto(dentist));

            ReadDentistDto result = service.updateContactData(
                    updateDto, dentist.getDentistId().value(), requesterId, requesterRolId);

            assertEquals("John", result.first());
            verify(dentistWriteMapper).updateContactFromDto(updateDto, dentist);
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
                    any(AuthorizationContext.class));
        }

        @Test
        @DisplayName("updateSensitiveData actualiza datos sensibles cuando autorizado")
        void updateSensitiveData_updatesDentist_whenAuthorized() {
            UpdateDentistSensitiveDto updateDto = new UpdateDentistSensitiveDto(
                    "Orthodontics",
                    LocalTime.of(9, 0), LocalTime.of(17, 0), DayOfWeek.TUESDAY, 35,
                    "987654321", "Jane", "Doe", "36",
                    LocalDate.of(1989, 5, 20), "A+", "EPS789"
            );

            when(dentistRepository.findById(dentist.getDentistId())).thenReturn(Optional.of(dentist));
            when(dentistRepository.save(dentist)).thenReturn(dentist);
            when(dentistReadMapper.toReadDto(dentist)).thenReturn(buildReadDto(dentist));

            ReadDentistDto result = service.updateSensitiveData(
                    updateDto, dentist.getDentistId().value(), requesterId, requesterRolId);

            assertEquals("John", result.first());
            verify(dentistWriteMapper).updateSensitiveFromDto(updateDto, dentist);
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
                    any(AuthorizationContext.class));
        }
    }

    // ── State changes ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Cambio de estado del dentista")
    class StateChangeTests {

        @Test
        @DisplayName("applyVacation aplica vacaciones cuando autorizado")
        void applyVacation_updatesDentistAvailability_whenAuthorized() {
            when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));

            service.applyVacation(
                    LocalDateTime.now(), LocalDateTime.now().plusDays(5), requesterId, requesterRolId);

            verify(dentistRepository).save(dentist);
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
                    any(AuthorizationContext.class));
        }

        @Test
        @DisplayName("applyIncapacity aplica incapacidad cuando autorizado")
        void applyIncapacity_updatesDentistAvailability_whenAuthorized() {
            when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));

            service.applyIncapacity(
                    LocalDateTime.now(), LocalDateTime.now().plusDays(3), "note", requesterId, requesterRolId);

            verify(dentistRepository).save(dentist);
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
                    any(AuthorizationContext.class));
        }

        @Test
        @DisplayName("returnToAvailable cambia estado a disponible cuando autorizado")
        void returnToAvailable_updatesDentistStatus_whenAuthorized() {
            when(dentistRepository.findByUserId(requesterId)).thenReturn(Optional.of(dentist));

            service.returnToAvailable(requesterId, requesterRolId);

            verify(dentistRepository).save(dentist);
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.UPDATE),
                    any(AuthorizationContext.class));
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Eliminación de dentistas")
    class DeleteTests {

        @Test
        @DisplayName("deleteById elimina dentista cuando autorizado")
        void deleteById_removesDentist_whenAuthorized() {
            when(dentistRepository.findById(dentist.getDentistId())).thenReturn(Optional.of(dentist));

            service.deleteById(dentist.getDentistId(), requesterId, requesterRolId);

            verify(dentistRepository).deleteById(dentist.getDentistId());
            verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                    eq(ResourceCatalog.BasicResource.DENTIST), eq(ActionCatalog.BasicAction.DELETE),
                    any(AuthorizationContext.class));
        }

        @Test
        @DisplayName("deleteById lanza excepción si no existe")
        void deleteById_throwsException_whenDentistNotFound() {
            when(dentistRepository.findById(DentistId.of(999L))).thenReturn(Optional.empty());

            assertThrows(DentistNotFoundException.class,
                    () -> service.deleteById(DentistId.of(999L), requesterId, requesterRolId));
        }
    }*/
}