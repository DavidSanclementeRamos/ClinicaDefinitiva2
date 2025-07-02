package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.TipoResponsable;

import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {

    // busqueda por usuario asociado
    Optional<Responsable> findByUnUsuario_Id(long idUsuario);

    // buacar por paciente
    List<Responsable> findByPaciente_Id(long idPaciente);

    // buscar por documento y contato
    Optional<Responsable> findByDni(String documento);
    Optional<Responsable> findByTelefono(String telefono);

    // filtrar por parentesco
    List<Responsable> findByTipoResponsable(TipoResponsable tipoRelacion);
}
