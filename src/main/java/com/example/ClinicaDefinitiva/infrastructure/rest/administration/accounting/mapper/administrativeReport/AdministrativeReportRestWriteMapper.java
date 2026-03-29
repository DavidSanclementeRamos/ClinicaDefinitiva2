package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.administrativeReport;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.CreateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.DocumentDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.IndicatorDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.PeriodDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.UpdateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AddAttachmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AddIndicatorRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.CreateAdministrativeReportRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.UpdateAdministrativeReportRequest;
import org.springframework.stereotype.Component;

@Component
public class AdministrativeReportRestWriteMapper {

    public CreateAdministrativeReportDto toServiceCreate(CreateAdministrativeReportRequest request) {
        if (request == null) return null;

        return new CreateAdministrativeReportDto(
                request.title(),
                new PeriodDto(request.periodStart(), request.periodEnd()),
                request.createdBy()
        );
    }

    public UpdateAdministrativeReportDto toServiceUpdate(UpdateAdministrativeReportRequest request) {
        if (request == null) return null;

        return new UpdateAdministrativeReportDto(
                request.title(),
                request.notes()
        );
    }

    public IndicatorDto toIndicatorDto(AddIndicatorRequest request) {
        if (request == null) return null;

        return new IndicatorDto(
                request.name(),
                request.value(),
                request.unit()
        );
    }

    public DocumentDto toDocumentDto(AddAttachmentRequest request) {
        if (request == null) return null;

        return new DocumentDto(
                request.name(),
                request.url(),
                request.type(),
                request.size()
        );
    }
}
