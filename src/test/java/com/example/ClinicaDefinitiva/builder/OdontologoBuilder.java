package com.example.ClinicaDefinitiva.builder;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OdontologoBuilder implements IBuilder<Odontologo> {



        // Atributos heredados de Persona
        private String dni = "00000000";
        private String nombre = "NombreDefault";
        private String apellido = "ApellidoDefault";
        private String telefono = "000-0000";
        private String direccion = "Calle Falsa 123";
        private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
        private Tipo_sangre tipoSangre = Tipo_sangre.O_POSITIVO;

        // Atributos propios de Odontologo
        private Especialidades especialidad = Especialidades.ENDODONCIA;
        private Usuario unUsuario; // puedes usar un UsuarioBuilder si lo tienes
        private List<Horario> listaHorarios = new ArrayList<>() ; // idem con HorarioBuilder
        private List<Turno> listaTurnos = new ArrayList<>();

        // Métodos fluidos
        public OdontologoBuilder withDni(String dni) {
            this.dni = dni;
            return this;
        }

        public OdontologoBuilder withNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public OdontologoBuilder withApellido(String apellido) {
            this.apellido = apellido;
            return this;
        }

        public OdontologoBuilder withTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public OdontologoBuilder withDireccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public OdontologoBuilder withFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public OdontologoBuilder withTipoSangre(Tipo_sangre tipoSangre) {
            this.tipoSangre = tipoSangre;
            return this;
        }

        public OdontologoBuilder withEspecialidad(Especialidades especialidad) {
            this.especialidad = especialidad;
            return this;
        }

        public OdontologoBuilder withUsuario(Usuario unUsuario) {
            this.unUsuario = unUsuario;
            return this;
        }

        public OdontologoBuilder withHorario(List<Horario> listaHorarios) {
            this.listaHorarios = listaHorarios;
            return this;
        }

        public OdontologoBuilder withTurnos(List<Turno> listaTurnos) {
            this.listaTurnos = listaTurnos;
            return this;
        }

        public OdontologoBuilder addTurno(Turno turno) {
            this.listaTurnos.add(turno);
            return this;
        }


    @Override
    public Odontologo builder() {
        Odontologo o = new Odontologo();
        o.setDni(dni);
        o.setNombre(nombre);
        o.setApellido(apellido);
        o.setTelefono(telefono);
        o.setDireccion(direccion);
        o.setFecha_nacimiento(fechaNacimiento);
        o.setTipoSangre(tipoSangre);
        o.setEspecialidad(especialidad);
        o.setUnUsuario(unUsuario);
        o.setListaHorarios(listaHorarios);
        o.setListaTurnos(listaTurnos);
        return o;
    }
}
