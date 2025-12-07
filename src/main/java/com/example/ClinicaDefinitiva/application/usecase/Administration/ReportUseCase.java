package com.example.ClinicaDefinitiva.application.usecase.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReportUseCase {
    ReportResponse findReportById(String id);
    Page<ReportListResponse> listReportsByPeriod(Pageable pageable, LocalDate star, LocalDate end);
    ReportResponse createAdministrativeReport(CreateReportRequest request);
    ReportResponse updateReportInformation(String id, UpdateReportRequest request);
    ReportResponse addJournalEntryToReport(String reportId, AddJournalEntryToReportRequest request);
    ReportResponse addIndicatorToReport(String reportId, AddIndicatorToReportRequest request);
    ReportResponse addAttachmentToReport(String reportId, AddAttachmentToReportRequest request);
    ReportResponse approveReport(String reportId, ApproveReportRequest request);

}
