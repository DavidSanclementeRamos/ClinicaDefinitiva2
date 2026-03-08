package com.example.ClinicaDefinitiva.application.mapper.treatment;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.PhaseStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentPhase;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TreatmentWriteMapper {

    public PatientId toPatientId(CreateTreatmentDto dto) {
        return PatientId.of(dto.patientId());
    }

    public DentistId toDentistId(CreateTreatmentDto dto) {
        return DentistId.of(dto.dentistId());
    }

    public ServiceId toServiceId(CreateTreatmentDto dto) {
        return ServiceId.of(dto.serviceId());
    }

    public LocalDate toStartDate(CreateTreatmentDto dto) {
        return dto.startDate();
    }

    public LocalDate toExpectedEndDate(CreateTreatmentDto dto) {
        return dto.expectedEndDate();
    }

    public List<TreatmentPhase> toPhases(CreateTreatmentDto dto) {
        return dto.phases().stream()
                .map(phase -> TreatmentPhase.of(
                        Name.of(phase.name()),
                        phase.startDate(),
                        PhaseStatus.valueOf(phase.status()),
                        Notes.of(phase.description())
                ))
                .collect(Collectors.toList());
    }

    public String toNotes(CreateTreatmentDto dto) {
        return dto.notes();
    }

    public RateId toRateId(CreateTreatmentDto dto) {
        return RateId.of(dto.rateId());
    }
}