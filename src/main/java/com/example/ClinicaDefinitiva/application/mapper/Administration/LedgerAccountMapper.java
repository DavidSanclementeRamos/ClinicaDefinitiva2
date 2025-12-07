package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.LedgerAccountListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.LedgerAccountResponse;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.LedgerAccount;
import org.springframework.stereotype.Component;

/**
 * Mapper: LedgerAccount (Domain → DTO)
 */
@Component
public class LedgerAccountMapper {

    public LedgerAccountResponse toResponse(LedgerAccount account) {
        return new LedgerAccountResponse(
                account.getId() != null ? account.getId().getValue() : null,
                account.getCompanyId() != null ? account.getCompanyId().getValue() : null,
                account.getCode(),
                NameMapper.toName(account.getName()),
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

    public LedgerAccountListResponse toListResponse(LedgerAccount account) {
        return new LedgerAccountListResponse(
                account.getId() != null ? account.getId().getValue() : null,
                account.getCode(),
                NameMapper.toName(account.getName()),
                account.getNature().name(),
                account.isActive(),
                account.getAccountLevel()
        );
    }
}
