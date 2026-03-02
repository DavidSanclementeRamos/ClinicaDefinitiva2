package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
//import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository {
    AdministrativeReport save(AdministrativeReport report);
    Optional<AdministrativeReport> findById(AdministrativeReportId id);
    Page<AdministrativeReport> findByPeriod( LocalDate star, LocalDate end, Pageable pageable );
    Page<AdministrativeReport> findByCreator( UserIdentityId createdBy, Pageable pageable);
    Page<AdministrativeReport> findByStatus(String status, Pageable pageable);
    Page<AdministrativeReport> findPublishedReports();
    Page<AdministrativeReport> findDraftReports();

    Page<AdministrativeReport> findAll(Pageable pageable);


}
