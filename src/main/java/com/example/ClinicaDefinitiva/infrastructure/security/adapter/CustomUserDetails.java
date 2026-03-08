package com.example.ClinicaDefinitiva.infrastructure.security.adapter;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.policies.RoleBasedPolicy;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    private final UserIdentityId id;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final List<Rol> rols;
    private final RolId activeRolId; // rol activo en la sesión

    public CustomUserDetails(UserIdentity user, List<UserRolAssignment> assignments, List<Rol> rol, RolId activeRolId) {
        this.id = user.getId();
        this.username = user.getEmail().value();
        this.password = user.getHashedPassword().toString();
        this.rols = rol;
        this.activeRolId = activeRolId;

        // Roles → Authorities
        this.authorities = assignments.stream()
                .flatMap(a -> {
                    RolEnum rolEnum = rol.stream()
                            .filter(r -> r.getId().equals(a.getRolId()))
                            .map(Rol::getRolEnum)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("RolEnum not found for RolId: " + a.getRolId()));

                    Set<Permission> permissions = RoleBasedPolicy.getPermissionsFor(rolEnum);
                    return Stream.concat(
                            Stream.of(new SimpleGrantedAuthority("ROLE_" + rolEnum.name())),
                            permissions.stream().map(p -> new SimpleGrantedAuthority(p.getCode()))
                    );
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public List<Rol> getRols() {
        return rols;
    }

    public RolId getActiveRolId() {
        return activeRolId;
    }

    public UserIdentityId getId() { return id; }
}

