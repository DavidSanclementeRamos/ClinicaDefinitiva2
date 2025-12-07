package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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