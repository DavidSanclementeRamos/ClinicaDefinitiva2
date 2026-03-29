package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.administrativeReport;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.PageAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.ReadAdministrativeReportDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AttachmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.IndicatorResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.PageAdministrativeReportResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.ReadAdministrativeReportResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AdministrativeReportRestReadMapper {

    public ReadAdministrativeReportResponse toRest(ReadAdministrativeReportDto dto) {
        if (dto == null) return null;

        return new ReadAdministrativeReportResponse(
                dto.id(),
                dto.title(),
                dto.period().star(),
                dto.period().end(),
                dto.createdAt(),
                dto.createdBy(),
                dto.status(),
                dto.journalEntryReferences(),
                dto.indicators().stream()
                        .map(i -> new IndicatorResponse(i.name(), i.value(), i.unit()))
                        .collect(Collectors.toList()),
                dto.notes(),
                dto.attachments().stream()
                        .map(a -> new AttachmentResponse(a.name(), a.url(), a.type(), a.size()))
                        .collect(Collectors.toList()),
                dto.lastUpdate(),
                dto.approvedBy(),
                dto.isComplete(),
                dto.isEditable()
        );
    }

    public PageAdministrativeReportResponse toPageRest(PageAdministrativeReportDto dto) {
        if (dto == null) return null;

        return new PageAdministrativeReportResponse(
                dto.id(),
                dto.title(),
                dto.period().star(),
                dto.period().end(),
                dto.status().getDescription(),
                dto.createdAt(),
                dto.totalItems()
        );
    }
}