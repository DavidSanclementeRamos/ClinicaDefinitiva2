package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Disponibilidad, Long> {

    // buscar por id odontologo
   List<Disponibilidad> findByUnOdontologo_Id(long idOdontologo);

   // ver disponivilida en un dia espesifico
   List<Disponibilidad> findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(DayOfWeek dia, LocalTime desde, LocalTime hasta);

   // cosulta por rango de fecha
   List<Disponibilidad> findDistinctByTurnos_FechaTurnoBetween(LocalDate desde, LocalDate hasta);
}
