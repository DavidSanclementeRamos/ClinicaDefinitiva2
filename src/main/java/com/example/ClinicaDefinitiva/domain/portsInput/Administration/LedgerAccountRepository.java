package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
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
