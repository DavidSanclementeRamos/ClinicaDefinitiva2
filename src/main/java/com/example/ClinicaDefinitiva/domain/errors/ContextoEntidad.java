package com.example.ClinicaDefinitiva.domain.errors;

public enum ContextoEntidad {

    DENTIST(CodigoEntidad.OD01),
    SECRETARIO(CodigoEntidad.SEC02),
    RESPONSABLE(CodigoEntidad.RES03),
    PACIENTE(CodigoEntidad.PAC04),
    USUARIO(CodigoEntidad.USR05),
    HORARIO(CodigoEntidad.HOR06),
    TURNO(CodigoEntidad.TUR07),
    Rol(CodigoEntidad.ROL08),
    WEEKLY_AVAILABILITY(CodigoEntidad.WEA09),
    ADDRESS(CodigoEntidad.ADD11),
    AGE(CodigoEntidad.NAM10),
    DENTIST_AVAILABILITY_STATUS(CodigoEntidad.STA014),
    PHONE_NUMBER(CodigoEntidad.PHO015),
    SECTOR(CodigoEntidad.SCT016),
    DATE_OF_BIRTH(CodigoEntidad.DOB013),
    SPECIALTY(CodigoEntidad.SPC017),
    SPECIALTIES(CodigoEntidad.SPC018),
    FULL_NAME(CodigoEntidad.NAM10),
    WORKING_HOURS(CodigoEntidad.WOR020);



    private final CodigoEntidad codigo;

    ContextoEntidad(CodigoEntidad codigo) {
        this.codigo = codigo;
    }

    public CodigoEntidad getCodigoEntidad() {
        return codigo;
    }

    }

