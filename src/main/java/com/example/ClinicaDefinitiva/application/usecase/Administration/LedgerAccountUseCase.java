package com.example.ClinicaDefinitiva.application.usecase.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.*;
import org.springframework.data.domain.Page;

public interface LedgerAccountUseCase {
        LedgerAccountResponse registerLedgerAccount(CreateLedgerAccountRequest request);
        LedgerAccountResponse updateLedgerAccount(String accountId, UpdateLedgerAccountRequest request);
        LedgerAccountResponse activateLedgerAccount(String accountId);
        LedgerAccountResponse inactivateLedgerAccount(String accountId, InactivateLedgerAccountRequest request);
        LedgerAccountResponse findLedgerAccountByI(String accountId);
        LedgerAccountResponse findLedgerAccountByCode(String code);
        Page<LedgerAccountListResponse> listLedgerAccountsByCompany(String companyId);
        Page<LedgerAccountListResponse> listActiveLedgerAccounts();
        Page<LedgerAccountListResponse> listLedgerAccountsByLevelU(int level);

}
