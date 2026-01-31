package com.example.ClinicaDefinitiva.domain.portsOutput.Administration;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

public interface ScheduleRepository {

    Schedule findByDentistId(DentistId dentistId);
    Schedule findByPatientId(PatientId patientId);
    void save(Schedule schedule);


}
