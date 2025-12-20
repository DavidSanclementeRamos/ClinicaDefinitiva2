package com.example.ClinicaDefinitiva.domain.errors;

public enum ContextoEntidad {

    DENTIST(CodigoEntidad.OD01),
    RECEPTIONIST(CodigoEntidad.REC02),
    GUARDIAN(CodigoEntidad.GUAS03),
    PATIENT(CodigoEntidad.PAT04),
    USUARIO(CodigoEntidad.USR05),
    HORARIO(CodigoEntidad.HOR06),
    TURNO(CodigoEntidad.TUR07),
    Rol(CodigoEntidad.ROL08),
    APPOINTMENT(CodigoEntidad.APP09),
    ADMINISTRATIVEREPORT(CodigoEntidad.EDM10),
    COMPANY(CodigoEntidad.COM11),
    CONTRACT(CodigoEntidad.CON12),
    JOURNALENTRY(CodigoEntidad.JOU13),
    LEDGERACCOUNT(CodigoEntidad.LED14),
    OPENINGBALANCE(CodigoEntidad.OPE15),
    THISPARTIES(CodigoEntidad.THI16);




    private final CodigoEntidad codigo;

    ContextoEntidad(CodigoEntidad codigo) {
        this.codigo = codigo;
    }

    public CodigoEntidad getCodigoEntidad() {
        return codigo;
    }

    }

