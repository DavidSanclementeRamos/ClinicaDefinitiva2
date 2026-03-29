
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount;

public record ReadAccountResponse(
    Long id,
    String code,
    String name,
    String nature,
    boolean requiresThirdParty,
    boolean requiresDocument,
    boolean active
) {}
