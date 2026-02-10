package com.example.ClinicaDefinitiva.domain.errors.context;

public enum EntityContext implements DomainContext {

    DENTIST(CodeEntity.OD01),
    RECEPTIONIST(CodeEntity.REC02),
    GUARDIAN(CodeEntity.GUA03),
    PATIENT(CodeEntity.PAT04),
    USER_IDENTITY(CodeEntity.USR05),
    HORARIO(CodeEntity.HOR06),
    SHIFT(CodeEntity.SHI07),
    ROL(CodeEntity.ROL08),
    APPOINTMENT(CodeEntity.APP09),
    ADMINISTRATIVEREPORT(CodeEntity.EDM10),
    COMPANY(CodeEntity.COM11),
    CONTRACT(CodeEntity.CON12),
    JOURNALENTRY(CodeEntity.JOU13),
    LEDGERACCOUNT(CodeEntity.LED14),
    OPENINGBALANCE(CodeEntity.OPE15),
    THISPARTIES(CodeEntity.THI16),
    AVAILABILITY(CodeEntity.AVA17),
    DENTAL_SERVICE(CodeEntity.SER18),
    INVOICE(CodeEntity.INV19),
    ASSIGNMENT(CodeEntity.ASS20);




    private final CodeEntity code;

    EntityContext(CodeEntity code) {
        this.code = code;
    }

    public CodeEntity getCodeEntidad() {
        return code;
    }


    @Override
    public CodeEntity getCodeEntity() {
        return code;
    }

    @Override
    public CodeVO getCodeVo() {
        return null;
    }
}

