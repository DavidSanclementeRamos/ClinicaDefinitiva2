package com.example.ClinicaDefinitiva.config;

import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.exceptions.EdadNoPermitidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Period;

import static com.example.ClinicaDefinitiva.Enum.Roles.ODONTOLOGO;
import static com.example.ClinicaDefinitiva.Enum.Roles.SECRETARIO;

@Configuration
@ConfigurationProperties(prefix = "edad.minima")
public class EdadMinimaConfig {

    private int odontologo;
    private int secretario;
    private int responsable;



    // Getters y setters


    public int getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(int odontologo) {
        this.odontologo = odontologo;
    }

    public int getResponsable() {
        return responsable;
    }

    public void setResponsable(int responsable) {
        this.responsable = responsable;
    }

    public int getSecretario() {
        return secretario;
    }

    public void setSecretario(int secretario) {
        this.secretario = secretario;
    }
}