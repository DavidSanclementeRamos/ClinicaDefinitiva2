package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository {
    AdministrativeReport save(AdministrativeReport report);
    Optional<AdministrativeReport> findById(AdministrativeReportId id);
    Page<AdministrativeReport> findByPeriod(Pageable pageable, LocalDate star, LocalDate end );
    Page<AdministrativeReport> findByCreator(Pageable pageable, UserId createdBy);
    Page<AdministrativeReport> findByStatus(Pageable pageable, ReportStatus status);
    Page<AdministrativeReport> findPublishedReports();
    Page<AdministrativeReport> findDraftReports();
}
