package com.example.ClinicaDefinitiva.persistence.entity;

import com.example.ClinicaDefinitiva.vo.EstadoCita;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class Cita {
private Long id;
private EstadoCita estadoCita;

@OneToOne
private Turno tuno;
private Servicio servicio;
private Odontologo odontologo;
private Paciente paciente;
private Facturacion facturacion;
private Responsable responsable;
}
