package com.example.ClinicaDefinitiva.repository;



import com.example.ClinicaDefinitiva.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // busqueda por nombre y documento
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Paciente> findByDni(String documento);

    // busqueda por usuario asociado
    Optional<Paciente> findByUnUsuario_Id(long idUsuario);

    // pacientes que tienen turnos agendados hoy
   // @Query("SELECT Odontologo p FROM Paciente p JOIN p.turnos t WHERE t.fecha = :fecha")
    List<Paciente> findByUnTurno_FechaTurno(LocalDate fecha);
}
