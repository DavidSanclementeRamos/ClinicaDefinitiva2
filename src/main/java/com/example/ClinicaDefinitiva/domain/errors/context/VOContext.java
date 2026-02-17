package com.example.ClinicaDefinitiva.domain.errors.context;

import org.aspectj.apache.bcel.classfile.Code;

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
    EMAIL(CodeVO.EMA20),
    APPOINTMENT_ID(CodeVO.APP21),
    APPOINTMENT_STATUS(CodeVO.APP22),
    AVAILABILITY_ID(CodeVO.AVA23),
    AVAILABILITY_STATUS(CodeVO.AVA24),
    SHIFT_ID(CodeVO.SHI25),
    AESTHETIC(CodeVO.AES26),
    IMPLANTOLOGY(CodeVO.IMP27),
    ORTHODONTIC(CodeVO.ORT28),
    PEDIATRIC(CodeVO.PED29),
    PROSTHETIC(CodeVO.PRO30),
    SURGICAL(CodeVO.SUR31),
    SERVICE_NAME(CodeVO.SNV32),
    SERVICE_DESCRIPTION(CodeVO.SDV33),
    SERVICE_PRICE(CodeVO.PRI34),
    SERVICE_DURATION(CodeVO.SDV35),
    SERVICE_CODE(CodeVO.SCV36),
    SERVICE_ID(CodeVO.SID37),
    SERVICE_STATUS(CodeVO.SST38),
    SERVICE_CATALOG(CodeVO.SCA39),
    USER_ID(CodeVO.USE40),
    PERMISSION(CodeVO.PER41),
    AUTHORIZATION(CodeVO.AUT42),
    COMPLETION(CodeVO.COP43),
    INVOICE_ID(CodeVO.INV44),
    INVOICE_ITEM_ID(CodeVO.INT45),
    RATE_ID(CodeVO.RAT46),
    BILLING_QUANTITY(CodeVO.QUA47);



    private final CodeVO code;

    VOContext(CodeVO code) {
        this.code = code;
    }



    @Override
    public CodeEntity getCodeEntity() {
        return null;
    }

    @Override
    public CodeVO getCodeVo() {
        return code;
    }
}
