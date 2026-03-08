package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record ReadLedgerAccountDto(
        Long id,
        Long companyId,
        String code,
        String name,
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
