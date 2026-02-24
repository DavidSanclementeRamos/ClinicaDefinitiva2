package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Document;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Period;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AdministrativeReportReadMapper {

    public ReadAdministrativeReportDto toReadDto(AdministrativeReport report) {
        return new ReadAdministrativeReportDto(
                report.getId() != null ? report.getId().value(): null,
                report.getTitle().toString(),
                toPeriodDto(report.getPeriod()),
                report.getCreatedAt(),
                report.getCreatedBy() != null ? report.getCreatedBy().value() : null,
                report.getStatus().getDisplayName(),
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
                report.getLastUpdate(),
                report.getApprovedBy() != null ? report.getApprovedBy().value() : null,
                report.isComplete(),
                report.isEditable()
        );
    }

    public PageAdministrativeReportDto toPageDto(AdministrativeReport report) {
        return new PageAdministrativeReportDto(
                report.getId() != null ? report.getId().value(): null,
                report.getTitle().toString(),
                toPeriodDto(report.getPeriod()),
                report.getStatus(),
                report.getCreatedAt(),
                report.getTotalItemsCount()
        );
    }

    private PeriodDto toPeriodDto(Period period) {
        return new PeriodDto(
                period.getStartDate(),
                period.getEndDate()
        );
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
                document.getName(),
                document.getUrl(),
                document.getType(),
                document.getSize()
        );
    }
}
