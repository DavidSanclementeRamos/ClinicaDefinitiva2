package com.example.ClinicaDefinitiva.Enum;

public enum ContextoEntidad {

    ODONTOLOGO(CodigoEntidad.OD01),
    SECRETARIO(CodigoEntidad.SEC02),
    RESPONSABLE(CodigoEntidad.RES03),
    PACIENTE(CodigoEntidad.PAC04),
    USUARIO(CodigoEntidad.USR05),
    HORARIO(CodigoEntidad.HOR06),
    TURNO(CodigoEntidad.TUR07),
    RolesEntity(CodigoEntidad.ROL08);


    private final CodigoEntidad codigo;

    ContextoEntidad(CodigoEntidad codigo) {
        this.codigo = codigo;
    }

    public CodigoEntidad getCodigoEntidad() {
        return codigo;
    }

    }

