package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;


import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments.TreatmentsVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;

import java.time.LocalDate;

public class TreatmentPhase {
    private final Name name;
    private final LocalDate scheduledDate;
    private final PhaseStatus status;
    private final Notes notes;

    private TreatmentPhase(Name name, LocalDate scheduledDate, PhaseStatus status, Notes notes) {
        
         if (scheduledDate != null && scheduledDate.isBefore(LocalDate.now())) {
            throw new ValueObjectValidationException(
                TreatmentsVoError.ERR_TREATMENTS_PHASE_DATE_INVALID,
                VOContext.CLINICAL_TREATMENTS
            );
        }

        this.name = name;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.notes = notes;
    }
    public static TreatmentPhase of(Name name, LocalDate scheduledDate,PhaseStatus status, Notes notes){
        return new TreatmentPhase(name,scheduledDate,status,notes);
    }

    public Name getName() { return name; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public Notes getNotes() { return notes; }

    public PhaseStatus getStatus() {
        return status;
    }
}

