package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ExpenseId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.Period;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ReportStatus;
import com.example.ClinicaDefinitiva.domain.identity.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
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
