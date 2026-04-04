package com.example.ClinicaDefinitiva.infrastructure.security.adapter;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserIdentityRepository userIdentityRepository;
    private final UserRolAssignmentRepository assignmentRepository;
    private final UserRolAssignmentService userRolService;

    public CustomUserDetailsService(UserIdentityRepository userIdentityRepository,
                                    UserRolAssignmentRepository assignmentRepository,
                                    UserRolAssignmentService userRolService) {
        this.userIdentityRepository = userIdentityRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRolService = userRolService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserIdentity user = userIdentityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (user.isLocked(Instant.now())) {
            throw new LockedException("Cuenta bloqueada");
        }
        if (!user.isVerified()) {
            throw new DisabledException("Usuario no verificado");
        }
        if (user.getStatus().getValue() != UserIdentityStatus.Status.ACTIVE) {
            throw new DisabledException("Usuario inactivo o suspendido");
        }

        Page<UserRolAssignment> assignmentsPage = assignmentRepository.findByUserId(user.getId(), Pageable.unpaged());

        if (assignmentsPage.isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin asignaciones: " + email);
        }

        // ✅ Filtrar solo asignaciones activas
        List<UserRolAssignment> activeAssignments = assignmentsPage.getContent().stream()
                .filter(UserRolAssignment::isCurrentlyActive)
                .collect(Collectors.toList());

        if (activeAssignments.isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin asignaciones activas: " + email);
        }

        // Obtener roles activos del usuario (coinciden con las asignaciones activas)
        List<Rol> activeRoles = userRolService.getActiveRoles(user.getId());

        if (activeRoles.isEmpty()) {
            throw new UsernameNotFoundException("User has no active roles: " + email);
        }

        // Pasar solo asignaciones activas y roles activos
        return new CustomUserDetails(user, activeAssignments, activeRoles, activeRoles.get(0).getId());
    }
}
