package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;

public interface ScheduleRepository {

    ScheduleQueryService findByDentistId(DentistId dentistId);
    ScheduleQueryService findByPatientId(PatientId patientId);
    void save(ScheduleQueryService schedule);


}
