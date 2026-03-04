
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Period;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AdministrativeReportTest {

    private final Name title = Name.of("Reporte Enero");
    private final Period period = Period.of(LocalDate.of(2025,1,1), LocalDate.of(2025,1,31));
    private final UserIdentityId creator = UserIdentityId.from(1L);

    @Test
    void shouldCreateReportWithDefaults() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        assertEquals("Reporte Enero", report.getTitle().getValue());
        assertTrue(report.isEditable());
        assertEquals(ReportStatus.draft(), report.getStatus());
        assertTrue(report.getJournalEntryReferences().isEmpty());
        assertTrue(report.getIndicators().isEmpty());
        assertTrue(report.getAttachments().isEmpty());
    }

    @Test
    void shouldAddAndRemoveJournalEntry() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        JournalEntryId entry = JournalEntryId.of(10L);

        report.addJournalEntryReference(entry);
        assertTrue(report.getJournalEntryReferences().contains(entry));

        report.removeJournalEntryReference(entry);
        assertFalse(report.getJournalEntryReferences().contains(entry));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateJournalEntry() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        JournalEntryId entry = JournalEntryId.of(10L);

        report.addJournalEntryReference(entry);
        assertThrows(BusinessRuleViolationException.class,
            () -> report.addJournalEntryReference(entry));
    }

    @Test
    void shouldAddAndRemoveIndicator() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        Indicator indicator = Indicator.of("Ingresos", BigDecimal.valueOf(1000), "USD");

        report.addIndicator(indicator);
        assertTrue(report.getIndicators().contains(indicator));

        report.removeIndicator(indicator);
        assertFalse(report.getIndicators().contains(indicator));
    }

    @Test
    void shouldThrowExceptionWhenAddingMoreThanOneIndicator() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        Indicator i1 = Indicator.of("Ingresos", BigDecimal.valueOf(1000), "USD");
        Indicator i2 = Indicator.of("Costos", BigDecimal.valueOf(500), "USD");

        report.addIndicator(i1);
        assertThrows(DomainAggregateException.class,
            () -> report.addIndicator(i2));
    }

    @Test
    void shouldSubmitApproveRejectArchiveUnarchive() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        JournalEntryId entry = JournalEntryId.of(10L);
        report.addJournalEntryReference(entry);

        report.submitForReview();
        assertTrue(report.getStatus().isUnderReview());

        UserIdentityId approver = UserIdentityId.from(2L);
        report.approve(approver);
        assertTrue(report.isPublished());
        assertEquals(approver, report.getApprovedBy());

        report.archive();
        assertTrue(report.getStatus().isArchived());

        report.unarchive();
        assertTrue(report.getStatus().isDraft());
    }

    @Test
    void shouldRejectWithReason() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        JournalEntryId entry = JournalEntryId.of(10L);
        report.addJournalEntryReference(entry);
        report.submitForReview();

        report.reject("Datos inconsistentes");
        assertTrue(report.isEditable());
        assertTrue(report.getNotes().contains("RECHAZADO"));
    }

    @Test
    void shouldThrowExceptionWhenRejectWithoutReason() {
        AdministrativeReport report = AdministrativeReport.create(title, period, creator);
        JournalEntryId entry = JournalEntryId.of(10L);
        report.addJournalEntryReference(entry);
        report.submitForReview();

        assertThrows(DomainAggregateException.class,
            () -> report.reject("   "));
    }
}

