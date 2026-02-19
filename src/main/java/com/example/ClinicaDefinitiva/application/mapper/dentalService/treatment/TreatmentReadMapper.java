package com.example.ClinicaDefinitiva.application.mapper.dentalService.treatment;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentPhaseDto;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;


import java.util.stream.Collectors;

public class TreatmentReadMapper {

    public TreatmentDto toDto(Treatment treatment) {
        return new TreatmentDto(
                treatment.getServicioId().getId(),
                treatment.getPatientId().getValue(),
                treatment.getDentistId().getValue(),
                treatment.getServicioId().getId(),
                treatment.getStatus().name(),
                treatment.getStartDate(),
                treatment.getExpectedEndDate(),
                treatment.getActualEndDate(),
                treatment.getPhases().stream()
                        .map(phase -> new TreatmentPhaseDto(
                                phase.getName(),
                                phase.getScheduledDate(),
                                   phase.getStatus().name(),
                                phase.getNotes()
                        ))
                        .collect(Collectors.toList()),
                treatment.getNotes(),
                treatment.getTarifaId().getValue()
        );
    }
}



