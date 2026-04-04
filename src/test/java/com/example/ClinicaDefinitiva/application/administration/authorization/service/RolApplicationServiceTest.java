package com.example.ClinicaDefinitiva.application.administration.authorization.service;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol.ReadRolDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.RolNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolApplicationServiceTest {

    @Mock
    private RolReadMapper readMapper;
    @Mock
    private RolWriteMapper writeMapper;
    @Mock
    private RolRepository repository;
    @Mock
    private AuthorizationHelper authorizationHelper;
    @Mock
    private RolService rolService;

    @InjectMocks
    private RolApplicationService service;

    @Test
    @DisplayName("Crear rol personalizado exitosamente")
    void createCustom_success() {
        CreateRolDto dto = new CreateRolDto("DENTIST", "Rol personalizado", false, false, false);
        Rol created = mock(Rol.class);
        Rol saved = mock(Rol.class);
        ReadRolDto resultDto = mock(ReadRolDto.class);

        when(rolService.createCustom(RolEnum.DENTIST, "Rol personalizado")).thenReturn(created);
        when(repository.save(created)).thenReturn(saved);
        when(readMapper.toReadDto(saved)).thenReturn(resultDto);

        ReadRolDto result = service.createCustom(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(resultDto);
        verify(rolService).createCustom(RolEnum.DENTIST, "Rol personalizado");
    }

    @Test
    @DisplayName("Buscar rol por ID existente")
    void findById_success() {
        RolId id = RolId.of(1L);
        Rol rol = mock(Rol.class);
        ReadRolDto dto = mock(ReadRolDto.class);

        when(repository.findById(id)).thenReturn(Optional.of(rol));
        when(readMapper.toReadDto(rol)).thenReturn(dto);

        ReadRolDto result = service.findById(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(dto);
    }

    @Test
    @DisplayName("Buscar rol por ID inexistente lanza excepción")
    void findById_notFound_throws() {
        RolId id = RolId.of(999L);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(RolNotFoundException.class);
    }

    @Test
    @DisplayName("Activar rol")
    void activate_success() {
        RolId id = RolId.of(1L);
        Rol rol = mock(Rol.class);
        when(repository.findById(id)).thenReturn(Optional.of(rol));

        service.activate(id, "Razón válida con más de diez caracteres", mock(UserIdentityId.class), mock(RolId.class));

        verify(rol).activate("Razón válida con más de diez caracteres");
        verify(repository).save(rol);
    }

  @Test
@DisplayName("Eliminar rol")
void deleteById_success() {
    RolId id = RolId.of(1L);
    Rol rol = mock(Rol.class);
    when(rol.getId()).thenReturn(id);  // ← IMPORTANTE: falta esto
    when(repository.findById(id)).thenReturn(Optional.of(rol));

    service.deleteById(id, mock(UserIdentityId.class), mock(RolId.class));

    verify(rol).delete();
    verify(repository).delete(id);
}
}