package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.AdministrativeReport;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Document;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.AdministrativeReportEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ReportAttachmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ReportIndicatorEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AdministrativeReportWriteEntityMapper {

    public AdministrativeReportEntity toEntity(AdministrativeReport report) {
        if (report == null) return null;

        AdministrativeReportEntity entity = new AdministrativeReportEntity();

       /** if (report.getId() != null && report.getId().value() != null) {
            entity.setId(report.getId().value());
        }*/

        entity.setTitle(report.getTitle().getValue());
        entity.setPeriodStart(report.getPeriod().getStartDate());
        entity.setPeriodEnd(report.getPeriod().getEndDate());
        entity.setStatus(report.getStatus().getValue().name());
        entity.setNotes(report.getNotes());
        entity.setCreatedAt(report.getCreatedAt());
        entity.setLastUpdated(report.getLastUpdate());

        entity.setIndicators(report.getIndicators().stream()
                .map(indicator -> toIndicatorEntity(indicator, entity))
                .collect(Collectors.toList()));

        entity.setAttachments(report.getAttachments().stream()
                .map(doc -> toAttachmentEntity(doc, entity))
                .collect(Collectors.toList()));

        return entity;
    }

    private ReportIndicatorEntity toIndicatorEntity(Indicator indicator, AdministrativeReportEntity reportEntity) {
        ReportIndicatorEntity entity = new ReportIndicatorEntity();
        entity.setReport(reportEntity);
        entity.setName(indicator.getName());
        entity.setValue(indicator.getValue().toString());
        entity.setUnit(indicator.getUnit());
        return entity;
    }

    private ReportAttachmentEntity toAttachmentEntity(Document doc, AdministrativeReportEntity reportEntity) {
        ReportAttachmentEntity entity = new ReportAttachmentEntity();
        entity.setReport(reportEntity);
        entity.setFileName(doc.getName());
        entity.setFileUrl(doc.getUrl());
        entity.setFileType(doc.getType());
        return entity;
    }
}