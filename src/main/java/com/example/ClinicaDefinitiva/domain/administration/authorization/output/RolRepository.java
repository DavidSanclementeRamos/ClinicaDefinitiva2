package com.example.ClinicaDefinitiva.domain.administration.authorization.output;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RolRepository {
    Optional<Rol> findById(RolId id);
    Optional<Rol> findByRolEnum(RolEnum rolEnum);
    Page<Rol> findAll(Pageable pageable);
    Page<Rol> findByEditable(boolean editable, Pageable pageable);
    Rol save(Rol rol);
    void delete(RolId id);

    boolean existsByDescription(String description);
}
