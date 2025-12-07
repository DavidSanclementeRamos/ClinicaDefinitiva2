package com.example.ClinicaDefinitiva.security.validator;

import com.example.ClinicaDefinitiva.persistence.entity.RolesEntity;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository userRepo;

    public CustomUserDetailsService(UsuarioRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario u = userRepo.findByNombreUsuarioIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe"));

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (RolesEntity role : u.getRoles()) {
            // prefijo ROLE_ para roles
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()));
            // accessControl directos como authorities
            role.getPermissionList().forEach(perm ->
                    authorities.add(new SimpleGrantedAuthority(perm.name()))
            );
        }

        return new org.springframework.security.core.userdetails.User(
                u.getNombreUsuario(),
                u.getContrasena(),
                u.isEnabled(),
                u.isAccountNoExpired(),
                u.isCredentialNoExpired(),
                u.isAccountNoLocked(),
                authorities
        );
    }
}