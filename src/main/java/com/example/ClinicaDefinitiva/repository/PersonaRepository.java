package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.persistence.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
