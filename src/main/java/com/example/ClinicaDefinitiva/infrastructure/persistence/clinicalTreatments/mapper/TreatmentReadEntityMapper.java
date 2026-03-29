package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.mapper;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentPhaseEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TreatmentReadEntityMapper {

    public Treatment toDomain(TreatmentEntity entity) {
        if (entity == null) return null;

        return Treatment.builder()
                .withId(TreatmentId.of(entity.getId()))
                .withPatientId(PatientId.of(entity.getPatient().getId()))
                .withDentistId(DentistId.of(entity.getDentist().getId()))
                .withServiceId(ServiceId.of(entity.getDentalService().getId()))
                .withStartDate(entity.getStartDate())
                .withExpectedEndDate(entity.getExpectedEndDate())
                .withPhases(entity.getPhases().stream()
                        .map(this::toTreatmentPhaseDomain)
                        .collect(Collectors.toList()))
                .withNotes(entity.getNotes())
                .withRateId(entity.getRate() != null ? 
                        RateId.of(entity.getRate().getId()) : null)
                .build();
        // Nota: El status se setea después de la construcción o necesitas un setter
    }

    private TreatmentPhase toTreatmentPhaseDomain(TreatmentPhaseEntity entity) {
        return TreatmentPhase.of(
               Name.of( entity.getName()),
                entity.getPlannedDate(),
                PhaseStatus.valueOf(entity.getStatus()),
               Notes.of( entity.getDescription()));
    }
}
