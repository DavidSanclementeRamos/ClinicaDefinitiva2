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
    public LedgerAccount fromCreate(CreateLedgerAccountDto dto){
        return LedgerAccount.registerLedgerAccount(
                CompanyId.of( dto.companyId()),
                dto.code(),
                Name.of(dto.name()),
                NaturalezaCuenta.valueOf( dto.nature()),
                dto.requiresThirdParty(),
                dto.requiresDocument()
        );
    }

    public void toUpdate(UpdateLedgerAccountDto dto, LedgerAccount ledgerAccount){
        ledgerAccount.updateAccountInformation(
               Name.of( dto.name()),
                dto.requiresThirdParty(),
                dto.requiresDocument()

        );
    }
}
