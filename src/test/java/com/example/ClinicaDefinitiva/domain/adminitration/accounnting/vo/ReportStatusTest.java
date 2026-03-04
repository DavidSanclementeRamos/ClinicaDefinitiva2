
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportStatusTest {

    @Test
    void shouldCreateDraftStatus() {
        ReportStatus status = ReportStatus.draft();
        assertTrue(status.isDraft());
        assertEquals("Borrador", status.getDescription());
        assertEquals("DRAFT", status.toString());
    }

    @Test
    void shouldCreateUnderReviewStatus() {
        ReportStatus status = ReportStatus.underReview();
        assertTrue(status.isUnderReview());
        assertEquals("En Revisión", status.getDescription());
        assertTrue(status.canBeApproved());
        assertTrue(status.canBeRejected());
    }

    @Test
    void shouldCreatePublishedStatus() {
        ReportStatus status = ReportStatus.published();
        assertTrue(status.isPublished());
        assertEquals("Publicado", status.getDescription());
        assertFalse(status.isEditable());
        assertFalse(status.canBeSubmittedForReview());
    }

    @Test
    void shouldCreateArchivedStatus() {
        ReportStatus status = ReportStatus.archived();
        assertTrue(status.isArchived());
        assertEquals("Archivado", status.getDescription());
        assertFalse(status.canBeArchived()); // ya está archivado
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        assertThrows(ValueObjectValidationException.class,
            () -> ReportStatus.of(null));
    }
}

