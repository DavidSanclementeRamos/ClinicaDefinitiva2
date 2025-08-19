package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecretarioRepository extends JpaRepository<Secretario, Long> {

    // buscar por sector
    List<Secretario> findBySector(Sector sector);

    // buscar por nombre
    //List<Secretario> findByActivoTrue();
    List<Secretario> findByNombreContainingIgnoreCase(String nombre);

    // busqueda por usuario asociado
    Optional<Secretario> findByUnUsuario_Id(long idUsuario);
    boolean existsByDni(String din);
    boolean existsByTelefono(String telefono);
}
