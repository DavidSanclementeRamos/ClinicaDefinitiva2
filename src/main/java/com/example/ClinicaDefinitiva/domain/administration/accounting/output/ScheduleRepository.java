package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

public interface ScheduleRepository {

    Schedule findByDentistId(DentistId dentistId);
    Schedule findByPatientId(PatientId patientId);
    void save(Schedule schedule);


}
