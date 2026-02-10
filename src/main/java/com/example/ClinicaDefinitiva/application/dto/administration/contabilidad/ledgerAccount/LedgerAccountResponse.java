package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record LedgerAccountResponse(
        String id,
        String companyId,
        String code,
        //NameDto name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument,
        boolean active,
        int level,
        String parentCode,
        boolean isAssetAccount,
        boolean isLiabilityAccount,
        boolean isIncomeAccount,
        boolean isExpenseAccount
) {}
