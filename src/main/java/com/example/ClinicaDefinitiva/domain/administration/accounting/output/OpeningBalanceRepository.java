package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Repositorio para OpeningBalance
 */
public interface OpeningBalanceRepository {
    OpeningBalance save(OpeningBalance openingBalance);
    Optional<OpeningBalance> findById(OpeningBalanceId id);
    Page<OpeningBalance> findByCompanyId(CompanyId companyId,Pageable pageable);
    Page<OpeningBalance> findByAccount(LedgerAccountId accountId,Pageable pageable);
    Page<OpeningBalance> findByDate(LocalDate date);
    Optional<OpeningBalance> findByAccountAndDate(LedgerAccountId accountId, LocalDate date);

    Page<OpeningBalance> findAll(Pageable pageable);



    public void deleteById(OpeningBalanceId openingBalanceId);
}