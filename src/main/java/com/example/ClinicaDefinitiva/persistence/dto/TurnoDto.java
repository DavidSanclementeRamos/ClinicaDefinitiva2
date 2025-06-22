package com.example.ClinicaDefinitiva.persistence.dto;



import com.example.ClinicaDefinitiva.Enum.Afeccion;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDto {

    private long id;
    @NotNull(message = " No puede ser nulo")
    private LocalDate fecha_turno;
    @NotNull(message = " No puede ser nulo")
    private LocalTime hora_turno;
    @NotNull(message = " No puede ser nulo")
    private Afeccion afeccion;
    private ReadPacienteDto readPacienteDto;
    private long idPaciente;
    private long horarioId;
    private long odontologo;

    public TurnoDto(){

    }
    public TurnoDto(Afeccion afeccion, LocalDate fecha_turno, LocalTime hora_turno
            , long idPaciente, ReadPacienteDto readPacienteDto, long id,long horarioId, long odontologo) {
        this.afeccion = afeccion;
        this.fecha_turno = fecha_turno;
        this.hora_turno = hora_turno;
        this.idPaciente = idPaciente;
        this.readPacienteDto = readPacienteDto;
        this.id= id;
        this.horarioId =  horarioId;
        this.odontologo = odontologo;
    }

    public long getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(long odontologo) {
        this.odontologo = odontologo;
    }

    public long getHorarioId( ) {
        return horarioId;
    }

    public void setHorarioId(long horarioId) {
        this.horarioId = horarioId;
    }

    public Afeccion getAfeccion() {
        return afeccion;
    }

    public void setAfeccion(Afeccion afeccion) {
        this.afeccion = afeccion;
    }

    public LocalDate getFecha_turno() {
        return fecha_turno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setFecha_turno(LocalDate fecha_turno) {
        this.fecha_turno = fecha_turno;
    }


    public LocalTime getHora_turno() {
        return hora_turno;
    }

    public void setHora_turno(LocalTime hora_turno) {
        this.hora_turno = hora_turno;
    }

    public ReadPacienteDto getReadPacienteDto() {
        return readPacienteDto;
    }

    public void setReadPacienteDto(ReadPacienteDto readPacienteDto) {
        this.readPacienteDto = readPacienteDto;
    }
}
