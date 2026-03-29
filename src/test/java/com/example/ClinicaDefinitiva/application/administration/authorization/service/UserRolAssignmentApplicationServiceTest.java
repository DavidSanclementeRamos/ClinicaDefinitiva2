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
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        UserRolAssignment saved = mock(UserRolAssignment.class);
        ReadAssignmentDto resultDto = mock(ReadAssignmentDto.class);

        when(writeMapper.toUserIdentityId(dto)).thenReturn(UserIdentityId.from(1L));
        when(writeMapper.toRolId(dto)).thenReturn(RolId.of(1L));
        when(writeMapper.toIsPrimary(dto)).thenReturn(true);
        when(assignment.getUserId()).thenReturn(UserIdentityId.from(1L));
        when(assignment.getRolId()).thenReturn(RolId.of(1L));
        when(assignment.isPrimary()).thenReturn(true);
        when(userRolService.assignRole(any(), any(), anyBoolean())).thenReturn(saved);
        when(readMapper.toReadDto(saved)).thenReturn(resultDto);

        ReadAssignmentDto result = service.savePermanent(dto, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).isSameAs(resultDto);
        verify(userRolService).assignRole(any(), any(), anyBoolean());
    }

    @Test
    @DisplayName("Buscar asignación por ID")
    void findById_success() {
        UserRolAssignmentId id = UserRolAssignmentId.of(1L);
        UserRolAssignment assignment = mock(UserRolAssignment.class);
        ReadAssignmentDto dto = mock(ReadAssignmentDto.class);

        when(repository.findById(id)).thenReturn(Optional.of(assignment));
        when(readMapper.toReadDto(assignment)).thenReturn(dto);

        Optional<ReadAssignmentDto> result = service.findById(id, mock(UserIdentityId.class), mock(RolId.class));

        assertThat(result).contains(dto);
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
}