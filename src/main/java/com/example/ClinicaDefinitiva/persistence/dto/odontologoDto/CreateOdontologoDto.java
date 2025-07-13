package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.Enumvalidation.EspecialidadesValido;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
public class CreateOdontologoDto {

    @NotBlank(message = "El dni no puede estar vacio ")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe contener exactamente 8 dígitos numéricos")
    @NotNull(message = "El nombre no puede ser nulo")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "El nombre no puede ser nulo")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    @NotBlank(message = "El el telefono no puede estar vacio")
    @NotNull(message = "El teléfono no puede ser nulo")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")

    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = "La dirección no puede ser nula")
    private String direccion;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;

    @NotNull(message = "La especialida no puede ser nula")
    @EspecialidadesValido
    private Especialidades especialidad;

    @NotNull(message = "El tipo de sangre no puede ser nula")
   // @TipoSangreValido
    private Tipo_sangre tipoSangre;


  //  @NotNull(message = "El tipo de sangre no puede ser nula")
   // @TipoSangreValido(allowed = {Tipo_sangre.A_NEGATIVO, Tipo_sangre.AB_NEGATIVO, Tipo_sangre.B_NEGATIVO, Tipo_sangre.A_NEGATIVO, Tipo_sangre.A_POSITIVO, Tipo_sangre.B_POSITIVO, Tipo_sangre.O_POSITIVO,
   //         Tipo_sangre.AB_POSITIVO , Tipo_sangre.O_NEGATIVO})
   // private String tipoSangre;

    private long idUsuario;


    public CreateOdontologoDto(String apellido, String direccion, String dni
            , Especialidades especialidad,   LocalDate fecha_nacimiento
            , long idUsuario, String nombre, String telefono,Tipo_sangre tipoSangre ) {
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.especialidad = especialidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }


    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public String getTelefono() {
        return telefono;
    }
}
