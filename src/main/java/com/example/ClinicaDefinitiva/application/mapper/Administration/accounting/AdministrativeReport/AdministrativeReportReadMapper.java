package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.DocumentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.IndicatorDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.PageAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.PeriodDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.ReadAdministrativeReportDto;
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
            report.getId().value(),                
            report.getTitle().toString(),
            toPeriodDto(report.getPeriod()),
            report.getCreatedAt(),
            report.getCreatedBy().value(),         
            report.getStatus().getDescription(),
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
            report.getApprovedBy().value(),        
            report.isComplete(),
            report.isEditable()
    );
}

public PageAdministrativeReportDto toPageDto(AdministrativeReport report) {
    return new PageAdministrativeReportDto(
            report.getId().value(),                            report.getTitle().toString(),
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
