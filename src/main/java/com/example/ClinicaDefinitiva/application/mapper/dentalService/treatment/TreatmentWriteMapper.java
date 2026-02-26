package com.example.ClinicaDefinitiva.application.mapper.dentalService.treatment;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;

import java.util.stream.Collectors;

public class TreatmentWriteMapper {

    public Treatment fromCreateDto(CreateTreatmentDto dto) {
        return Treatment.createNew(
                TreatmentId.of(dto.patientId()), // aquí puedes ajustar la generación de ID
                PatientId.of(dto.patientId()),
                DentistId.of(dto.dentistId()),
                ServiceId.of(dto.serviceId()),
                dto.startDate(),
                dto.expectedEndDate(),
                dto.phases().stream()
                        .map(phase -> TreatmentPhase.of(
                               Name.of( phase.name()),
                                phase.startDate(),
                                PhaseStatus.valueOf(   phase.status()),
                                Notes.of(phase.description())
                        ))
                        .collect(Collectors.toList()),
                dto.notes(),
                RateId.of(dto.rateId())
        );
    }
}