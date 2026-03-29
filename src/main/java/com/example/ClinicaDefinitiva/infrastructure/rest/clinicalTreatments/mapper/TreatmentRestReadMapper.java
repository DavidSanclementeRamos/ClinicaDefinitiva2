package com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.mapper;


import com.example.ClinicaDefinitiva.application.clinicalTreatments.dto.TreatmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.dto.ReadTreatmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.dto.TreatmentPhaseRest;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;


@Component
public class TreatmentRestReadMapper {


    public ReadTreatmentResponse toRest(TreatmentDto dto) {
        if (dto == null) {
            return null;
        }

        return new  ReadTreatmentResponse(
                dto.id(),
                dto.patientId(),
                dto.dentistId(),
                dto.serviceId(),
                dto.status(),
                dto.startDate(),
                dto.expectedEndDate(),
                dto.actualEndDate(),
                dto.phases().stream()
                        .map(phase -> new TreatmentPhaseRest(
                                phase.name(),
                                phase.startDate(),
                                phase.status(),
                                phase.description()
                        ))
                        .collect(Collectors.toList()),
                dto.notes(),
                dto.rateId()


        );

    }

}


