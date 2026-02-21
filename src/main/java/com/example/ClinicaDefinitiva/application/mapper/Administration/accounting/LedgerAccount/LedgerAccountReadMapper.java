package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.PageLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import org.springframework.stereotype.Component;

/**
 * Mapper: LedgerAccount (Domain → DTO)
 */
@Component
public class LedgerAccountReadMapper {

    public ReadLedgerAccountDto toReadDto(LedgerAccount account) {
        return new ReadLedgerAccountDto(
                account.getId() != null ? account.getId().getValue() : null,
                account.getCompanyId() != null ? account.getCompanyId().getValue() : null,
                account.getCode(),
                account.getName().getValue(),
                account.getNature().name(),
                account.isRequiresThirdParty(),
                account.isRequiresDocument(),
                account.isActive(),
                account.getAccountLevel(),
                account.getParentCode(),
                account.isAssetAccount(),
                account.isLiabilityAccount(),
                account.isIncomeAccount(),
                account.isExpenseAccount()
        );
    }

    public PageLedgerAccountDto toPageDto(LedgerAccount account) {
        return new PageLedgerAccountDto(
                account.getId() != null ? account.getId().getValue() : null,
                account.getCode(),
                account.getName().getValue(),
                account.getNature().name(),
                account.isActive(),
                account.getAccountLevel()
        );
    }
}
