package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    // buscar por id odontologo
   List<Horario> findByUnOdontologo_Id(long idOdontologo);

   // ver disponivilida en un dia espesifico
   List<Horario> findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(DayOfWeek dia, LocalTime desde, LocalTime hasta);

   // cosulta por rango de fecha
   List<Horario> findByTurnos_FechaTurnoBetween(LocalDate desde, LocalDate hasta);
}
