package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Document;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Indicator;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Period;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ReportMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReportResponse toResponse(AdministrativeReport report) {
        return new ReportResponse(
                report.getId() != null ? report.getId().getValue() : null,
                NameMapper.toName(report.getTitle()),
                toPeriodDto(report.getPeriod()),
                report.getCreatedAt().format(DATE_TIME_FORMATTER),
                report.getCreatedBy() != null ? report.getCreatedBy().asString() : null,
                report.getStatus(),
                report.getJournalEntryReferences().stream()
                        .map(JournalEntryId::getValue)
                        .collect(Collectors.toList()),
                report.getIndicators().stream()
                        .map(this::toIndicatorDTO)
                        .collect(Collectors.toList()),
                report.getNotes(),
                report.getAttachments().stream()
                        .map(this::toDocumentDTO)
                        .collect(Collectors.toList()),
                report.getLastUpdate().format(DATE_TIME_FORMATTER),
                report.getApprovedBy() != null ? report.getApprovedBy().asString() : null,
                report.isComplete(),
                report.isEditable()
        );
    }

    public ReportListResponse toListResponse(AdministrativeReport report) {
        return new ReportListResponse(
                report.getId() != null ? report.getId().getValue() : null,
                NameMapper.toName(report.getTitle()),
                toPeriodDto(report.getPeriod()),
                report.getStatus(),
                report.getCreatedAt().format(DATE_TIME_FORMATTER),
                report.getTotalItemsCount()
        );
    }

    private PeriodDto toPeriodDto(Period period) {
        return new PeriodDto(
                period.getStartDate(),
                period.getEndDate()
        );
    }

    public Period fromDto(PeriodDto dto){
        return new Period(dto.star(), dto.end());
    }

    private IndicatorDto toIndicatorDTO(Indicator indicator) {
        return new IndicatorDto(
                indicator.getName(),
                indicator.getValue(),
                indicator.getUnit()
        );
    }

    private DocumentDto toDocumentDTO(Document document) {
        return new DocumentDto(
                NameMapper.toName(document.getName()),
                document.getUrl(),
                document.getType(),
                document.getSize()
        );
    }
}
