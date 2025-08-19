package com.example.ClinicaDefinitiva.repository;



import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OdontologoRepository extends JpaRepository<Odontologo, Long> {

    boolean existsByTelefono(String telefono);
    boolean existsByDni(String dni);

    // búsqueda por usuario asociado
  Optional<Odontologo> findByUnUsuario_Id(long idUsuario);

    // buscar odontólogo por especialidad
    List<Odontologo> findByEspecialidad(Especialidades especialidad);

    // buscar odontólogo por un rango de fecha
    List<Odontologo> findDistinctByListaTurnos_FechaTurnoBetween(LocalDate desde, LocalDate hasta);
}
