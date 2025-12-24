package com.example.ClinicaDefinitiva.domain.errors;

public enum VOContext implements DomainContext {

    FULL_NAME( CodeVO.FUL02),
    DATE_OF_BIRTH(CodeVO.BIR03),
    AGE(CodeVO.AGE11 ),
    PHONE_NUMBER(CodeVO.PHO04),
    ADDRESS(CodeVO.ADD05),
    BLOOD_TYPE(CodeVO.BLO12),
    DENTIST_ID(CodeVO.DEN13),
    PATIENT_ID(CodeVO.PAT14),
    GUARDIAN_ID(CodeVO.GUA07),
    RECEPTION_ID(CodeVO.REC16),
    SPECIALTY(CodeVO.SPE17),
    SECTOR(CodeVO.SEC06),
    TYPE_GUARDIAN(CodeVO.TGU18),
    WORKING_HOURS(CodeVO.WOR08),
    DENTIST_AVAILABILITY_STATUS(CodeVO.DAS19),
    DOCUMENT_ID(CodeVO.DOC01),
    EMAIL(CodeVO.EMA20) ;

    private final CodeVO codigo;

    VOContext(CodeVO codigo) {
        this.codigo = codigo;
    }



    @Override
    public CodeEntity getCodeEntity() {
        return null;
    }

    @Override
    public CodeVO getCodeVo() {
        return codigo;
    }
}
