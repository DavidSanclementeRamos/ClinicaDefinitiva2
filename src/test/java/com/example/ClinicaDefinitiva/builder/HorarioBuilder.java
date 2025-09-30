package com.example.ClinicaDefinitiva.builder;

import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public  class HorarioBuilder implements IBuilder<Disponibilidad>{

    private DayOfWeek diaSemana = DayOfWeek.MONDAY;
    private LocalTime horaInicio = LocalTime.of(6, 30);
    private LocalTime horaFin = LocalTime.of(12, 0);
    private Estado estado = Estado.ACTIVO;
    private LocalDate fecha = LocalDate.now();
    private List<Turno> turnos = new ArrayList<>();
    private Odontologo unOdontologo;




    public HorarioBuilder() {
    }

    public  HorarioBuilder setDiaSemana (DayOfWeek diaSemana){
        this.diaSemana = diaSemana;
        return this;
    }
    public HorarioBuilder setHoraInicio (LocalTime horaInicio){
       this.horaInicio = horaInicio;
        return this;
    }
    public HorarioBuilder setHoraFin (LocalTime horaFin){
        this.horaFin = horaFin;
        return this;
    }
    public HorarioBuilder setEstado (Estado estado){
        this.estado = estado;
        return this;
    }
    public HorarioBuilder setFecha (LocalDate fecha){
        this.fecha = fecha;
        return this;
    }
    public HorarioBuilder setOdontologo (Odontologo UnOdontologo){
        this.unOdontologo = UnOdontologo;
        return this;
    }
    public HorarioBuilder setTurnos (List<Turno> turnos){
        this.turnos =  turnos;
        return this;
    }
    public HorarioBuilder addTurno(Turno turno){
        if (this.turnos == null) {
            this.turnos = new ArrayList<>();
        }
        this.turnos.add(turno);
        return this;
    }



    @Override
    public Disponibilidad builder() {
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setDiaSemana(diaSemana);
        disponibilidad.setHoraInicio(horaInicio);
        disponibilidad.setHoraFin(horaFin);
        disponibilidad.setEstado(estado);
        disponibilidad.setFecha(fecha);
        disponibilidad.setTurnos(turnos);
        disponibilidad.setUnOdontologo(unOdontologo);
        return disponibilidad;

    }
}
