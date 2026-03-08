package com.example.ClinicaDefinitiva.application.mapper.clinicalTreatments;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentPhaseDto;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;


import java.util.stream.Collectors;

public class TreatmentReadMapper {

    public TreatmentDto toDto(Treatment treatment) {
        return new TreatmentDto(
                treatment.getServicioId().getId(),
                treatment.getPatientId().value(),
                treatment.getDentistId().value(),
                treatment.getServicioId().getId(),
                treatment.getStatus().name(),
                treatment.getStartDate(),
                treatment.getExpectedEndDate(),
                treatment.getActualEndDate(),
                treatment.getPhases().stream()
                        .map(phase -> new TreatmentPhaseDto(
                                phase.getName().getValue(),
                                phase.getScheduledDate(),
                                   phase.getStatus().name(),
                                phase.getNotes().getValue().get()
                        ))
                        .collect(Collectors.toList()),
                treatment.getNotes(),
                treatment.getTarifaId().getValue()
        );
    }
}



