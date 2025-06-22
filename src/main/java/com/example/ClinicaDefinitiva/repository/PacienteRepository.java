package com.example.ClinicaDefinitiva.repository;



import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByUnTurnoContaining(Turno unturno);
    List<Paciente> findByResponsable(Responsable responsable);
    List<Paciente> findByUnUsuario (Usuario unUsuario);
}
