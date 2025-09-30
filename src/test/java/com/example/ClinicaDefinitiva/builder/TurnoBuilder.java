package com.example.ClinicaDefinitiva.builder;

import com.example.ClinicaDefinitiva.Enum.Afeccion;
import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;



    public class TurnoBuilder implements IBuilder<Turno> {

        private LocalDate fechaTurno = LocalDate.of(2025,8,20);
        private LocalTime horaTurno = LocalTime.of(9, 0);
        private Odontologo odontologo = new OdontologoBuilder().builder();
        private Paciente paciente = new Paciente(); // puedes usar PacienteBuilder si lo tienes
        private Afeccion afeccion = Afeccion.CARIES; // valor por defecto
        private Disponibilidad disponibilidad; // puedes usar HorarioBuilder si lo tienes
        private Estado estado = Estado.ACTIVO;

        public TurnoBuilder withFechaTurno(LocalDate fechaTurno) {
            this.fechaTurno = fechaTurno;
            return this;
        }

        public TurnoBuilder withHoraTurno(LocalTime horaTurno) {
            this.horaTurno = horaTurno;
            return this;
        }

        public TurnoBuilder withOdontologo(Odontologo odontologo) {
            this.odontologo = odontologo;
            return this;
        }

        public TurnoBuilder withPaciente(Paciente paciente) {
            this.paciente = paciente;
            return this;
        }

        public TurnoBuilder withAfeccion(Afeccion afeccion) {
            this.afeccion = afeccion;
            return this;
        }

        public TurnoBuilder withHorario(Disponibilidad disponibilidad) {
            this.disponibilidad = disponibilidad;
            return this;
        }

        public TurnoBuilder withEstado(Estado estado) {
            this.estado = estado;
            return this;
        }

        @Override
        public Turno builder() {
            Turno turno = new Turno();
            turno.setFechaTurno(fechaTurno);
            turno.setHoraTurno(horaTurno);
            turno.setOdontologo(odontologo);
            turno.setPaciente(paciente);
            turno.setAfeccion(afeccion);
            turno.setHorario(disponibilidad);
            turno.setEstado(estado);
            return turno;
        }

    }
