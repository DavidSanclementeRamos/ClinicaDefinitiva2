package com.example.ClinicaDefinitiva.domain.errors;

public enum EntityContext implements DomainContext {

    DENTIST(CodeEntity.OD01),
    RECEPTIONIST(CodeEntity.REC02),
    GUARDIAN(CodeEntity.GUAS03),
    PATIENT(CodeEntity.PAT04),
    USUARIO(CodeEntity.USR05),
    HORARIO(CodeEntity.HOR06),
    TURNO(CodeEntity.TUR07),
    Rol(CodeEntity.ROL08),
    APPOINTMENT(CodeEntity.APP09),
    ADMINISTRATIVEREPORT(CodeEntity.EDM10),
    COMPANY(CodeEntity.COM11),
    CONTRACT(CodeEntity.CON12),
    JOURNALENTRY(CodeEntity.JOU13),
    LEDGERACCOUNT(CodeEntity.LED14),
    OPENINGBALANCE(CodeEntity.OPE15),
    THISPARTIES(CodeEntity.THI16);




    private final CodeEntity codigo;

    EntityContext(CodeEntity codigo) {
        this.codigo = codigo;
    }

    public CodeEntity getCodigoEntidad() {
        return codigo;
    }


    @Override
    public CodeEntity getCodeEntity() {
        return null;
    }

    @Override
    public CodeVO getCodeVo() {
        return null;
    }
}

