package com.example.ClinicaDefinitiva.application.actor.service;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.mapper.reception.ReceptionistReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.reception.ReceptionistWriteMapper;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceptionApplicationServiceTest {

    @Mock
    private ReceptionRepository receptionRepository;
    @Mock
    private ReceptionistReadMapper readMapper;
    @Mock
    private ReceptionistWriteMapper writeMapper;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private ReceptionApplicationService service;

    private final UserIdentityId requesterId = UserIdentityId.from(1L);
    private final RolId requesterRolId = RolId.of(100L);

    // ========== Métodos auxiliares ==========
    private ReadReceptionistDto createReadReceptionistDto() {
        return new ReadReceptionistDto(
            1L,
            "RECEPTION",
            "12345678",
            "Juana",
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

    private PageReceptionistDto createPageReceptionistDto() {
        return new PageReceptionistDto(
            1L,
            "RECEPTION",
            "12345678",
            "Juana",
            "Pérez",
            "3001234567"
        );
    }

    private UpdateReceptionistContactDto createUpdateContactDto() {
        return new UpdateReceptionistContactDto(
           Optional.of( "Calle Nueva 456"),
           Optional.of( "Medellín"),
           Optional.of( "Antioquia"),
           Optional.of( "Colombia"),
           Optional.of( "050001"),
           Optional.of( "3011234567")
        );
    }

    private UpdateReceptionistSensitiveDto createUpdateSensitiveDto() {
        return new UpdateReceptionistSensitiveDto(
          Optional.of(  "87654321"),
           Optional.of( "María"),
           Optional.of( "Gómez"),
          Optional.of(  "35"),
           Optional.of( LocalDate.now().minusYears(35)),
           Optional.of( "A+"),
           Optional.of( "EPS456"),
           Optional.of( "BILLING")
        );
    }

    private Receptionist createReceptionist() {
        Person person = createPerson();
        UserIdentityId userId = UserIdentityId.from(2L);
        Sector sector = Sector.of(Sector.Type.RECEPTION);
        return Receptionist.registerReceptionist(person, userId, sector);
    }

    private Receptionist createReceptionistWithId() {
        Person person = createPerson();
        UserIdentityId userId = UserIdentityId.from(2L);
        Sector sector = Sector.of(Sector.Type.RECEPTION);
        return Receptionist.reconstruct(
            ReceptionId.of(1L),
            person,
            sector,
            userId,
            LocalDateTime.now()
        );
    }

    private Person createPerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juana", "Pérez");
        return Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
    }

    // ========== Tests ==========
    @Test
    @DisplayName("findById - debe retornar DTO cuando existe")
    void findById_shouldReturnDto() {
        ReceptionId id = ReceptionId.of(1L);
        Receptionist receptionist = createReceptionist();
        ReadReceptionistDto expectedDto = createReadReceptionistDto();

        when(receptionRepository.findById(id)).thenReturn(Optional.of(receptionist));
        when(readMapper.toReadDto(receptionist)).thenReturn(expectedDto);

        ReadReceptionistDto result = service.findById(id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.RECEPTIONIST), eq(ActionCatalog.BasicAction.READ),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("findAll - debe retornar página")
    void findAll_shouldReturnPage() {
        Pageable pageable = Pageable.unpaged();
        Receptionist receptionist = createReceptionist();
        Page<Receptionist> page = new PageImpl<>(List.of(receptionist));
        PageReceptionistDto dto = createPageReceptionistDto();
        when(receptionRepository.findAll(pageable)).thenReturn(page);
        when(readMapper.toPageDto(receptionist)).thenReturn(dto);

        Page<PageReceptionistDto> result = service.findAll(pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("findBySector - debe retornar página")
    void findBySector_shouldReturnPage() {
        String sector = "RECEPTION";
        Pageable pageable = Pageable.unpaged();
        Receptionist receptionist = createReceptionist();
        Page<Receptionist> page = new PageImpl<>(List.of(receptionist));
        PageReceptionistDto dto = createPageReceptionistDto();
        when(receptionRepository.findBySector(sector, pageable)).thenReturn(page);
        when(readMapper.toPageDto(receptionist)).thenReturn(dto);

        Page<PageReceptionistDto> result = service.findBySector(sector, pageable, requesterId, requesterRolId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("save - debe crear recepcionista")
    void save_shouldCreate() {
        CreateReceptionistDto dto = mock(CreateReceptionistDto.class);
        Person person = createPerson();
        UserIdentityId userId = UserIdentityId.from(2L);
        Sector sector = Sector.of(Sector.Type.RECEPTION);

        when(writeMapper.toPerson(dto)).thenReturn(person);
        when(writeMapper.toUserIdentityId(dto)).thenReturn(userId);
        when(writeMapper.toSector(dto)).thenReturn(sector);

        Receptionist receptionist = Receptionist.registerReceptionist(person, userId, sector);
        ReadReceptionistDto expectedDto = createReadReceptionistDto();

        when(receptionRepository.save(any(Receptionist.class))).thenReturn(receptionist);
        when(readMapper.toReadDto(receptionist)).thenReturn(expectedDto);

        ReadReceptionistDto result = service.save(dto, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(receptionRepository).save(any(Receptionist.class));
    }

    @Test
    @DisplayName("updateContact - debe actualizar contacto")
    void updateContact_shouldUpdate() {
        ReceptionId id = ReceptionId.of(1L);
        UpdateReceptionistContactDto dto = createUpdateContactDto(); // DTO real
        Receptionist receptionist = createReceptionist();

        when(receptionRepository.findById(id)).thenReturn(Optional.of(receptionist));
        when(receptionRepository.save(receptionist)).thenReturn(receptionist);
        ReadReceptionistDto expectedDto = createReadReceptionistDto();
        when(readMapper.toReadDto(receptionist)).thenReturn(expectedDto);

        ReadReceptionistDto result = service.updateContact(dto, id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(receptionRepository).save(receptionist);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.RECEPTIONIST), eq(ActionCatalog.BasicAction.UPDATE),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("updateSensitive - debe actualizar datos sensibles")
    void updateSensitive_shouldUpdate() {
        ReceptionId id = ReceptionId.of(1L);
        UpdateReceptionistSensitiveDto dto = createUpdateSensitiveDto(); // DTO real
        Receptionist receptionist = createReceptionist();

        when(receptionRepository.findById(id)).thenReturn(Optional.of(receptionist));
        when(receptionRepository.save(receptionist)).thenReturn(receptionist);
        ReadReceptionistDto expectedDto = createReadReceptionistDto();
        when(readMapper.toReadDto(receptionist)).thenReturn(expectedDto);

        ReadReceptionistDto result = service.updateSensitive(dto, id, requesterId, requesterRolId);

        assertThat(result).isEqualTo(expectedDto);
        verify(receptionRepository).save(receptionist);
        verify(authorizationHelper).authorize(eq(requesterId), eq(requesterRolId),
                eq(ResourceCatalog.BasicResource.RECEPTIONIST), eq(ActionCatalog.BasicAction.UPDATE),
                any(AuthorizationContext.class));
    }

    @Test
    @DisplayName("deleteById - debe eliminar recepcionista")
    void deleteById_shouldDelete() {
        ReceptionId id = ReceptionId.of(1L);
        Receptionist receptionist = createReceptionistWithId(); // con ID
        when(receptionRepository.findById(id)).thenReturn(Optional.of(receptionist));

        service.deleteById(id, requesterId, requesterRolId);

        verify(receptionRepository).deleteById(id);
    }
}