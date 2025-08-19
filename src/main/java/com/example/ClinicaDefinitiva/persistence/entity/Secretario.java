package com.example.ClinicaDefinitiva.persistence.entity;
import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity

public class Secretario extends Persona {
    // private int id_secretario;
    private Sector sector;
    @OneToOne
    @JoinColumn(name = "usuario_id") // Clave foránea está en la clase hija
    private Usuario unUsuario;

    public Secretario(){

    }

    public Secretario(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, long id, String nombre, String telefono, Tipo_sangre tipoSangre, Sector sector, Usuario unUsuario) {
        super(apellido, direccion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
        this.sector = sector;
        this.unUsuario = unUsuario;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Usuario getUnUsuario() {
        return unUsuario;
    }

    public void setUnUsuario(Usuario unUsuario) {
        this.unUsuario = unUsuario;
    }
}
