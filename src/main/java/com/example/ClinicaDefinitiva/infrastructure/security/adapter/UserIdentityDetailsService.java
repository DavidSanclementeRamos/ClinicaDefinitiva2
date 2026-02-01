package com.example.ClinicaDefinitiva.infrastructure.security.adapter;

import com.example.ClinicaDefinitiva.domain.portsOutput.UserRepository;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserIdentityDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserIdentityDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserIdentity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe"));

        if (user.isLocked(Instant.now())) {
            throw new LockedException("Cuenta bloqueada");
        }
        if (!user.isVerified()) {
            throw new DisabledException("Usuario no verificado");
        }
        if (user.getStatus().getState() != UserStatus.State.ACTIVE) {
            throw new DisabledException("Usuario inactivo o suspendido");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail().value(),
                user.getHashedPassword().toString(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
