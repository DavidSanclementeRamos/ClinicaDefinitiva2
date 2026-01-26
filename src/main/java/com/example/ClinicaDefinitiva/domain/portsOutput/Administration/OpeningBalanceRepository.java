package com.example.ClinicaDefinitiva.domain.portsOutput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.OpeningBalanceId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositorio para OpeningBalance
 */
public interface OpeningBalanceRepository {
    OpeningBalance save(OpeningBalance openingBalance);
    Optional<OpeningBalance> findById(OpeningBalanceId id);
    Page<OpeningBalance> findByCompanyId(CompanyId companyId);
    Page<OpeningBalance> findByAccount(LedgerAccountId accountId);
    Page<OpeningBalance> findByDate(LocalDate date);
    Optional<OpeningBalance> findByAccountAndDate(LedgerAccountId accountId, LocalDate date);
}