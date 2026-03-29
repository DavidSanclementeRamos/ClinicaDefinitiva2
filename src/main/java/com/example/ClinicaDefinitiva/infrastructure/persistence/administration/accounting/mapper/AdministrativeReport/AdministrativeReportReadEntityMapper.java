package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.AdministrativeReport;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Document;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Period;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.AdministrativeReportEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ReportAttachmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ReportIndicatorEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class AdministrativeReportReadEntityMapper {

    public AdministrativeReport toDomain(AdministrativeReportEntity entity) {
        if (entity == null) return null;

        return AdministrativeReport.builder()
                .withId(AdministrativeReportId.of(entity.getId()))
                .withTitle(Name.of(entity.getTitle()))
                .withPeriod(Period.of(entity.getPeriodStart(), entity.getPeriodEnd()))
                .withCreatedBy(UserIdentityId.from(entity.getCreatedBy().getId()))
                .withCreatedAt(entity.getCreatedAt())
                .withStatus(com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus.of(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus.Status.valueOf(
                                entity.getStatus())))
                .withJournalEntryReferences(entity.getReferencedEntries().stream()
                        .map(entry -> JournalEntryId.of(entry.getId()))
                        .collect(Collectors.toList()))
                .withIndicators(entity.getIndicators().stream()
                        .map(this::toIndicatorDomain)
                        .collect(Collectors.toList()))
                .withNotes(entity.getNotes())
                .withAttachments(entity.getAttachments().stream()
                        .map(this::toDocumentDomain)
                        .collect(Collectors.toList()))
                .withLastUpdate(entity.getLastUpdated())
                .withApprovedBy(entity.getApprovedBy() != null ?
                        UserIdentityId.from(entity.getApprovedBy().getId()) : null)
                .build();
    }

    private Indicator toIndicatorDomain(ReportIndicatorEntity entity) {
        return Indicator.of(
                entity.getName(),
                new BigDecimal(entity.getValue()),
                entity.getUnit()
        );
    }

    private Document toDocumentDomain(ReportAttachmentEntity entity) {
        return Document.of(
                entity.getFileName(),
                entity.getFileUrl(),
                entity.getFileType(),
                0L // El tamaño no está en la entidad
        );
    }
}
