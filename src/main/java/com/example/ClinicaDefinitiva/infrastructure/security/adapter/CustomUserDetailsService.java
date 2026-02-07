package com.example.ClinicaDefinitiva.infrastructure.security.adapter;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.UserRolAssignmentService;
import com.example.ClinicaDefinitiva.domain.authentication.UserRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRolAssignmentRepository assignmentRepository;
    private final UserRolAssignmentService userRolService;

    public CustomUserDetailsService(UserRepository userRepository,
                                    UserRolAssignmentRepository assignmentRepository, UserRolAssignmentService userRolService) {
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRolService = userRolService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserIdentity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (user.isLocked(Instant.now())) {
            throw new LockedException("Cuenta bloqueada");
        }
        if (!user.isVerified()) {
            throw new DisabledException("Usuario no verificado");
        }
        if (user.getStatus().getState() != UserStatus.State.ACTIVE) {
            throw new DisabledException("Usuario inactivo o suspendido");
        }

        List<UserRolAssignment> assignments = assignmentRepository.findByUserId(user.getId());


        if (assignments.isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin roles activos: " + email);
        }

        // Obtener roles activos del usuario
        List<Rol> activeRoles = userRolService.getActiveRoles(user.getId());

        if (activeRoles.isEmpty()) {
            throw new UsernameNotFoundException("User has no active roles: " + email);
        }
        return new CustomUserDetails(user, assignments,activeRoles, activeRoles.get(0).getId());
    }
}

