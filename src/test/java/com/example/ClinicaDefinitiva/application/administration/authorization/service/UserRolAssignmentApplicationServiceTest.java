package com.example.ClinicaDefinitiva.application.administration.authorization.service;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment.AssignmentReadMapper;
import com.example.ClinicaDefinitiva.application.administration.authorization.mapper.userRolAssignment.AssignmentWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.administration.authorization.UserRolAssignmentNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolAssignmentApplicationServiceTest {

    @Mock
    private UserRolAssignmentService userRolService;
    @Mock
    private AssignmentWriteMapper writeMapper;
    @Mock
    private AssignmentReadMapper readMapper;
    @Mock
    private UserRolAssignmentRepository repository;
    @Mock
    private AuthorizationHelper authorizationHelper;

    @InjectMocks
    private UserRolAssignmentApplicationService service;

    @Test
@DisplayName("Asignar rol permanente exitosamente")
void savePermanent_success() {
    CreateAssignmentPermanentDto dto = new CreateAssignmentPermanentDto(1L, 1L, true);

    // Configurar mappers para convertir DTO a valores de dominio
    when(writeMapper.toUserIdentityId(dto)).thenReturn(UserIdentityId.from(1L));
    when(writeMapper.toRolId(dto)).thenReturn(RolId.of(1L));
    when(writeMapper.toIsPrimary(dto)).thenReturn(true);

    // Mock del servicio de dominio: devuelve una asignación guardada (mock)
    UserRolAssignment savedAssignment = mock(UserRolAssignment.class);
    when(userRolService.assignRole(any(UserIdentityId.class), any(RolId.class), anyBoolean()))
        .thenReturn(savedAssignment);

    // Mock del mapper de lectura
    ReadAssignmentDto resultDto = mock(ReadAssignmentDto.class);
    when(readMapper.toReadDto(savedAssignment)).thenReturn(resultDto);

    // Ejecutar
    ReadAssignmentDto result = service.savePermanent(dto, mock(UserIdentityId.class), mock(RolId.class));

    // Verificar
    assertThat(result).isSameAs(resultDto);
    verify(userRolService).assignRole(any(UserIdentityId.class), any(RolId.class), anyBoolean());
}

    @Test
    @DisplayName("Buscar asignación por ID existente")
    void findById_success() {
        UserRolAssignmentId id = UserRolAssignmentId.of(1L);
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        ReadAssignmentDto dto = mock(ReadAssignmentDto.class);

        when(repository.findById(id)).thenReturn(Optional.of(assignment));
        when(readMapper.toReadDto(assignment)).thenReturn(dto);

        ReadAssignmentDto result = service.findById(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(dto);
    }

    @Test
    @DisplayName("Buscar asignación por ID inexistente lanza excepción")
    void findById_notFound_throws() {
        UserRolAssignmentId id = UserRolAssignmentId.of(999L);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id, mock(UserIdentityId.class), mock(RolId.class)))
                .isInstanceOf(UserRolAssignmentNotFoundException.class);
    }

    @Test
    @DisplayName("Extender asignación temporal")
    void extend_success() {
        UserRolAssignmentId id = UserRolAssignmentId.of(1L);
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        when(repository.findById(id)).thenReturn(Optional.of(assignment));

        service.extend(id, java.time.LocalDate.now().plusMonths(3), mock(UserIdentityId.class), mock(RolId.class));

        verify(assignment).extend(any());
        verify(repository).save(assignment);
    }

  @Test
@DisplayName("Buscar asignaciones por usuario devuelve página")
void findByUserId_returnsPage() {
    UserIdentityId userId = UserIdentityId.from(1L);
    Pageable pageable = Pageable.ofSize(10);
    
    // Crear una asignación mock
    UserRolAssignment assignmentMock = mock(UserRolAssignment.class);
    List<UserRolAssignment> assignmentList = List.of(assignmentMock);
    Page<UserRolAssignment> assignmentPage = new PageImpl<>(assignmentList);
    
    when(repository.findByUserId(eq(userId), eq(pageable))).thenReturn(assignmentPage);
    when(readMapper.toReadDto(any(UserRolAssignment.class))).thenReturn(mock(ReadAssignmentDto.class));

    Page<ReadAssignmentDto> result = service.findByUserId(userId, mock(UserIdentityId.class), mock(RolId.class), pageable);

    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent()).hasSize(1);
    verify(repository).findByUserId(userId, pageable);
}
}