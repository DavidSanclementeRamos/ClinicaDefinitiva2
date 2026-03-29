package com.example.ClinicaDefinitiva.application.administration.accounting.mapper.LedgerAccount;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.PageLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import org.springframework.stereotype.Component;

/**
 * Mapper: LedgerAccount (Domain → DTO)
 */
@Component
public class LedgerAccountReadMapper {

    public ReadLedgerAccountDto toReadDto(LedgerAccount account) {
        return new ReadLedgerAccountDto(
                account.getId().getValue(),
                account.getCompanyId().value(),
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
                account.getId().getValue(),
                account.getCode(),
                account.getName().getValue(),
                account.getNature().name(),
                account.isActive(),
                account.getAccountLevel()
        );
    }
}