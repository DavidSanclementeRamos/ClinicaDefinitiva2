package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretarioRepository extends JpaRepository<Secretario, Long> {

    List<Secretario> findByUnUsuario (Usuario unUsuario);
}
