package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByUnOdontologo(Odontologo unOdontologo);


}
