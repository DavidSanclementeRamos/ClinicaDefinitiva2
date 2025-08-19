package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // buscaer usuario por email
    Optional<Usuario> findByCorreoEletronicoIgnoreCase(String email);



    // buscar por user
    Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);

    //



}
