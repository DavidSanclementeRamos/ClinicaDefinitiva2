package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.*;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.NameMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.ReportMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.ReportUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ReportRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.*;
//import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class ReportApplicationService implements ReportUseCase {

    private final ReportRepository repository;
    private final JournalEntryRepository journalEntryRepository;
    private final ReportMapper mapper;

    public ReportApplicationService(ReportRepository repository, JournalEntryRepository journalEntryRepository, ReportMapper mapper) {
        this.repository = repository;
        this.journalEntryRepository = journalEntryRepository;
        this.mapper = mapper;
    }

    @Override
    public ReportResponse findReportById(String id) {
        return null;
    }

    @Override
    public Page<ReportListResponse> listReportsByPeriod(Pageable pageable, LocalDate star, LocalDate end) {
        return null;
    }

    @Override
    public ReportResponse createAdministrativeReport(CreateReportRequest request) {
        return null;
    }

    @Override
    public ReportResponse updateReportInformation(String id, UpdateReportRequest request) {
        return null;
    }

    @Override
    public ReportResponse addJournalEntryToReport(String reportId, AddJournalEntryToReportRequest request) {
        return null;
    }

    @Override
    public ReportResponse addIndicatorToReport(String reportId, AddIndicatorToReportRequest request) {
        return null;
    }

    @Override
    public ReportResponse addAttachmentToReport(String reportId, AddAttachmentToReportRequest request) {
        return null;
    }

    @Override
    public ReportResponse approveReport(String reportId, ApproveReportRequest request) {
        return null;
    }


    /** @Override
    public ReportResponse findReportById(String id) {
        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));
        return mapper.toResponse(report);
    }

    @Override
    public Page<ReportListResponse> listReportsByPeriod(Pageable pageable, LocalDate star, LocalDate end) {
        Page<AdministrativeReport> reportPage = repository.findByPeriod(pageable, star, end);
        if(reportPage.isEmpty()){
            throw new AdministrativeReportNotFoundException("");
        }
        return reportPage.map(mapper::toListResponse);

    }

    @Override
    public ReportResponse createAdministrativeReport(CreateReportRequest dto) {

        AdministrativeReport report = AdministrativeReport.create(
                NameMapper.fromDto(dto.title()),
               mapper.fromDto( dto.periodType()),
                UserIdentityId.fromString(dto.createdBy())
        );
                repository.save(report);

        return mapper.toResponse(report);
    }

    @Override
    public ReportResponse updateReportInformation(String id, UpdateReportRequest dto) {
        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));
        report.updateInformation(
                NameMapper.fromDto(dto.title()),
                dto.notes()
        );
        repository.save(report);

        return mapper.toResponse(report);
    }

    @Override
    public ReportResponse addJournalEntryToReport(String id, AddJournalEntryToReportRequest request) {
        // Regla: Validar que el asiento exista
        journalEntryRepository.findById(JournalEntryId.fromString(request.journalEntryId()))
                .orElseThrow(() -> new IllegalArgumentException("Asiento accounting no encontrado"));

        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));

        report.addJournalEntryReference(JournalEntryId.fromString(request.journalEntryId()));
        repository.save(report);

        return mapper.toResponse(report);
    }

    @Override
    public ReportResponse addIndicatorToReport(String id, AddIndicatorToReportRequest request) {
        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));

        report.addIndicator(new Indicator(NameMapper.fromDto(request.name()), request.value(),request.unit()));

        repository.save(report);

        return mapper.toResponse(report);
    }

    @Override
    public ReportResponse addAttachmentToReport(String id, AddAttachmentToReportRequest request) {
        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));

        report.addAttachment(new Document(NameMapper.fromDto(request.name()), request.url(),request.url(),request.size()));

        repository.save(report);

        return mapper.toResponse(report);
    }

    @Override
    public ReportResponse approveReport(String id, ApproveReportRequest request) {
        AdministrativeReportId reportId = AdministrativeReportId.fromString(id);
        AdministrativeReport report =  repository.findById(reportId)
                .orElseThrow(()-> new AdministrativeReportNotFoundException("No se encontró el reporte:" + reportId));

        UserIdentityId userIdentityId = UserIdentityId.fromString(request.approveId());
        report.approve(userIdentityId);

        repository.save(report);

        return mapper.toResponse(report);
    }*/
}
