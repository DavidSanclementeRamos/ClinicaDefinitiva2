package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Repositorio para LedgerAccount
 */
public interface LedgerAccountRepository {
    LedgerAccount save(LedgerAccount ledgerAccount);
    Optional<LedgerAccount> findById(LedgerAccountId id);
    Optional<LedgerAccount> findByCode(String code);
    Page<LedgerAccount> findByCompanyId(CompanyId companyId);
    Page<LedgerAccount> findByNature(NaturalezaCuenta nature);
    Page<LedgerAccount> findActiveAccounts();
    Page<LedgerAccount> findByLevel(int level);
    Page<LedgerAccount> findChildAccounts(String parentCode);
    boolean existsByCode(String code);
}
