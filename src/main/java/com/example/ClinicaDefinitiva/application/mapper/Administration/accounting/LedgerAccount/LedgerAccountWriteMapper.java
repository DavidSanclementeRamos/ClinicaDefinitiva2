package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.CreateLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.UpdateLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import org.springframework.stereotype.Component;

@Component
public class LedgerAccountWriteMapper {

    public CompanyId toCompanyId(CreateLedgerAccountDto dto) {
        return CompanyId.of(dto.companyId());
    }

    public String toCode(CreateLedgerAccountDto dto) {
        return dto.code();
    }

    public Name toName(CreateLedgerAccountDto dto) {
        return Name.of(dto.name());
    }

    public NaturalezaCuenta toNature(CreateLedgerAccountDto dto) {
        return NaturalezaCuenta.valueOf(dto.nature());
    }

    public boolean toRequiresThirdParty(CreateLedgerAccountDto dto) {
        return dto.requiresThirdParty();
    }

    public boolean toRequiresDocument(CreateLedgerAccountDto dto) {
        return dto.requiresDocument();
    }


    public Name toName(UpdateLedgerAccountDto dto) {
        return Name.of(dto.name());
    }

    public boolean toRequiresThirdParty(UpdateLedgerAccountDto dto) {
        return dto.requiresThirdParty();
    }

    public boolean toRequiresDocument(UpdateLedgerAccountDto dto) {
        return dto.requiresDocument();
    }

}
