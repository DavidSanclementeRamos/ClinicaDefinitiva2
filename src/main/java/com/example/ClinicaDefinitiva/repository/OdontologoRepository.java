package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OdontologoRepository extends JpaRepository<Odontologo, Long> {
    List<Odontologo> findByUnTurnoContaining(Turno unTurno);
    //List<Odontologo> findByUnHorarioContaining(Horario horario);
    @Query("SELECT o FROM Odontologo o JOIN o.unHorario h WHERE h = :horario")
    List<Odontologo> findByUnHorarioContaining(@Param("horario") Horario unHorario);

}
