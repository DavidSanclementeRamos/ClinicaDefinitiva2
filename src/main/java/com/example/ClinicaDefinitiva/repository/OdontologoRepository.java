package com.example.ClinicaDefinitiva.repository;



import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OdontologoRepository extends JpaRepository<Odontologo, Long> {

    // busqueda por usuario asociado
    Optional<Odontologo> findByUnUsuario_Id(long idUsuario);

    // buscar odontologo por especialida
    List<Odontologo> findByEspecialidad(Especialidades especialidad);

    // buscar odontologo por un rango de fecha
   // @Query("SELECT o FROM Odontologo o JOIN o.turnos t WHERE t.fecha BETWEEN :desde AND :hasta")
    List<Odontologo> findByUnTurno_FechaTurnoBetween(LocalDate desde, LocalDate hasta);
}
