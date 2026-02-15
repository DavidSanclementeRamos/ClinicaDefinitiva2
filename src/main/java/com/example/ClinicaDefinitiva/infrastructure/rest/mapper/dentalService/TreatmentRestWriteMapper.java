package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentPhaseDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.CreateTreatmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.TreatmentPhaseRest;
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