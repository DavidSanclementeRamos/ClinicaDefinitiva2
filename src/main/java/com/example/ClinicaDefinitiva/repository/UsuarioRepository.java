package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // buscaer usuario por email
    Optional<Usuario> findByCorreoEletronico(String email);

    // filtrar usuario por rol
    List<Usuario> findByRol(Roles rol);

    // buscar por user
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
