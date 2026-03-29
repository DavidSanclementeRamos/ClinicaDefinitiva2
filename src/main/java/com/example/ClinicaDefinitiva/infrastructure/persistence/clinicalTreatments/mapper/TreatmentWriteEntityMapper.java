package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.mapper;

import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentPhaseEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TreatmentWriteEntityMapper {

    public TreatmentEntity toEntity(Treatment treatment) {
        if (treatment == null) return null;

        TreatmentEntity entity = new TreatmentEntity();

         /**if (treatment.getId() != null && treatment.getId().getValue() != null) {
            entity.setId(treatment.getId().getValue());
        }*/

        entity.setStatus(treatment.getStatus().name());
        entity.setStartDate(treatment.getStartDate());
        entity.setExpectedEndDate(treatment.getExpectedEndDate());
        entity.setActualEndDate(treatment.getActualEndDate());
        entity.setNotes(treatment.getNotes());

        // Mapear fases
        entity.setPhases(
                treatment.getPhases().stream()
                        .map(phase -> toTreatmentPhaseEntity(phase, entity))
                        .collect(Collectors.toList())
        );

        // Nota: Las relaciones con PatientEntity, DentistEntity, etc.
        // se establecen en el adapter usando los repositorios

        return entity;
    }

    private TreatmentPhaseEntity toTreatmentPhaseEntity(TreatmentPhase phase, TreatmentEntity treatmentEntity) {
        TreatmentPhaseEntity entity = new TreatmentPhaseEntity();
        entity.setTreatment(treatmentEntity);
        entity.setName(phase.getName().getValue());
        entity.setDescription(phase.getNotes() != null ? phase.getNotes().getValue().orElse(null) : null);
        entity.setStatus(phase.getStatus().name());
        entity.setPlannedDate(phase.getScheduledDate());
        entity.setCompletedDate(phase.getScheduledDate());
        return entity;
    }
}
