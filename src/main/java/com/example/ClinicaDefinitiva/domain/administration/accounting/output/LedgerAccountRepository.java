package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import org.springframework.data.domain.Page;

import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Repositorio para LedgerAccount
 */
public interface LedgerAccountRepository {
    LedgerAccount save(LedgerAccount ledgerAccount);
    Optional<LedgerAccount> findById(LedgerAccountId id);
    Optional<LedgerAccount> findByCode(String code);
    Page<LedgerAccount> findByCompanyId(CompanyId companyId);
    Page<LedgerAccount> findByNature(String nature, Pageable pageable);
    Page<LedgerAccount> findActiveAccounts();
    Page<LedgerAccount> findByLevel(int level,Pageable pageable);
    Page<LedgerAccount> findChildAccounts(String parentCode);
    boolean existsByCode(String code);

    Page<LedgerAccount> findAll(Pageable pageable);
    Page<LedgerAccount> findByAccountType(String type, Pageable pageable);
}
