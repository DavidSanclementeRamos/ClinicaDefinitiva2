package com.example.ClinicaDefinitiva.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class HistorialCambioResponsable {
    @Id
    @GeneratedValue
    private Long id;
    private Long idMenor;
    private Long idAntiguoResponsable;
    private Long idNuevoResponsable;
    private LocalDateTime fechaCambio;
    private String motivoCambio;


    public HistorialCambioResponsable(LocalDateTime fechaCambio, Long id
            , Long idAntiguoResponsable, Long idMenor, Long idNuevoResponsable
            , String motivoCambio) {
        this.fechaCambio = fechaCambio;
        this.id = id;
        this.idAntiguoResponsable = idAntiguoResponsable;
        this.idMenor = idMenor;
        this.idNuevoResponsable = idNuevoResponsable;
        this.motivoCambio = motivoCambio;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAntiguoResponsable() {
        return idAntiguoResponsable;
    }

    public void setIdAntiguoResponsable(Long idAntiguoResponsable) {
        this.idAntiguoResponsable = idAntiguoResponsable;
    }

    public Long getIdMenor() {
        return idMenor;
    }

    public void setIdMenor(Long idMenor) {
        this.idMenor = idMenor;
    }

    public Long getIdNuevoResponsable() {
        return idNuevoResponsable;
    }

    public void setIdNuevoResponsable(Long idNuevoResponsable) {
        this.idNuevoResponsable = idNuevoResponsable;
    }

    public String getMotivoCambio() {
        return motivoCambio;
    }

    public void setMotivoCambio(String motivoCambio) {
        this.motivoCambio = motivoCambio;
    }
}
