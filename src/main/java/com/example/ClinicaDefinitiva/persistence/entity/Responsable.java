package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
//@Getter
//@Setter
public class Responsable extends Persona{
        @Enumerated(EnumType.STRING)
        private TipoResponsable tipoResponsable;
        @OneToOne
        @JoinColumn(name = "usuario_id") // Clave foránea está en la clase hija
        private Usuario usuario;

        private Usuario unUsuario;

        @OneToMany(mappedBy = "responsable")
        private List<Paciente> paciente;


        public Responsable() {

        }


        public Responsable(String apellido, String direcion, String dni, LocalDate fecha_nacimiento, int id, String nombre, String telefono, Tipo_sangre tipoSangre, TipoResponsable tipoResponsable, Usuario unUsuario, List<Paciente> paciente) {
                super(apellido, direcion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
                this.tipoResponsable = tipoResponsable;
                this.unUsuario = unUsuario;
                this.paciente = paciente;
        }

        public List<Paciente> getPaciente() {
                return paciente;
        }

        public void setPaciente(List<Paciente> paciente) {
                this.paciente = paciente;
        }

        public Usuario getUsuario() {
                return usuario;
        }

        public void setUsuario(Usuario usuario) {
                this.usuario = usuario;
        }

        public TipoResponsable getTipoResponsable() {
                return tipoResponsable;
        }

        public void setTipoResponsable(TipoResponsable tipoResponsable) {
                this.tipoResponsable = tipoResponsable;
        }

        public Usuario getUnUsuario() {
                return unUsuario;
        }

        public void setUnUsuario(Usuario unUsuario) {
                this.unUsuario = unUsuario;
        }
}
