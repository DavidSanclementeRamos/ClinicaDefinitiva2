package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    @DisplayName("Crear rol personalizado con descripción única")
    void createCustom_success() {
        when(rolRepository.existsByDescription("Rol especial")).thenReturn(false);

        Rol rol = rolService.createCustom(RolEnum.DENTIST, "Rol especial");

        assertThat(rol.getRolEnum()).isEqualTo(RolEnum.DENTIST);
        assertThat(rol.getDescription()).isEqualTo("Rol especial");
        assertThat(rol.isEditable()).isTrue();
        verify(rolRepository).existsByDescription("Rol especial");
    }

    @Test
    @DisplayName("Crear rol personalizado con descripción duplicada lanza excepción")
    void createCustom_duplicateDescription_throws() {
        when(rolRepository.existsByDescription("Rol existente")).thenReturn(true);

        assertThatThrownBy(() -> rolService.createCustom(RolEnum.DENTIST, "Rol existente"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Clonar rol exitosamente")
    void cloneRole_success() {
        Rol sourceRol = Rol.createDefault(RolEnum.PATIENT, "Rol original");
        when(rolRepository.existsByDescription("Rol clonado")).thenReturn(false);

        Rol cloned = rolService.cloneRole(sourceRol, "Rol clonado");

        assertThat(cloned.getRolEnum()).isEqualTo(RolEnum.PATIENT);
        assertThat(cloned.getDescription()).isEqualTo("Rol clonado");
        assertThat(cloned.isDefault()).isFalse();
        assertThat(cloned.isEditable()).isTrue();
    }
}
