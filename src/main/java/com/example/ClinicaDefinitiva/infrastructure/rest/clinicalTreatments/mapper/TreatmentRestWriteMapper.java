package com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.mapper;

import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.TreatmentPhaseDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.dto.CreateTreatmentRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class TreatmentRestWriteMapper {


    public CreateTreatmentDto toServiceCreate(CreateTreatmentRequest request) {
        if (request == null) {
            return null;
        }

        return new CreateTreatmentDto(
                request.patientId(),
                request.dentistId(),
                request.serviceId(),
                request.startDate(),
                request.expectedEndDate(),
                request.phases().stream()
                                        .map(phase -> new TreatmentPhaseDto(
                                                phase.name(),
                                                phase.startDate(),
                                                 phase.status(),
                                                phase.description()
                                        ))
                                        .collect(Collectors.toList()),
                request.notes(),
                request.rateId()
                ) ;
    }
}