package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // turno por fecha y rango de fecha
   // List<Turno> findByFechaTurno(LocalDate fecha);
    List<Turno> findByFechaTurnoBetween(LocalDate desde, LocalDate hasta);

    // turno por odontologo y paciente
    List<Turno> findByOdontologo_Id(long idOdontologo);
    List<Turno> findByPaciente_Id (long  idPaciente);

    // filtrar por estado
    List<Turno> findByEstado(Estado estado);

    // Buscar si ya hay un turno en una hora específica para evitar solapamientos
    boolean existsByOdontologo_IdAndFechaTurnoAndHoraTurno(long idOdontologo, LocalDate fecha, LocalTime hora);
}
