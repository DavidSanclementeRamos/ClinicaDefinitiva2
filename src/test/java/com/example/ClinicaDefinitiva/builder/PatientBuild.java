package com.example.ClinicaDefinitiva.builder;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.administration.Operations.Shift;

import java.time.LocalDateTime;

public class PatientBuild implements IBuilder {



        private Address address;
        private Age age;
        private BloodType bloodType;
        private DateOfBirth dateOfBirth;
        private String dni;
        private FullName fullname;
        private Long id;
        private PhoneNumber phoneNumber;
        private UserIdentity user;
        private Shift shift;
        private Appointment appointment;
        private Guardian guardian;
        private ScheduleQueryService schedule;
        private LocalDateTime lastUpdate;
        private Long contractId;
        private String documentoEPS;


        public PatientBuild withAddress(Address a) { this.address = a; return this; }
        public PatientBuild withAge(Age a) { this.age = a; return this; }
        public PatientBuild withBloodType(BloodType b) { this.bloodType = b; return this; }
        public PatientBuild withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public PatientBuild withDni(String d) { this.dni = d; return this; }
        public PatientBuild withFullName(FullName f) { this.fullname = f; return this; }
        public PatientBuild withId(Long id) { this.id = id; return this; }
        public PatientBuild withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public PatientBuild withUser(UserIdentity u){this.user = u; return this;}
        public PatientBuild withShift(Shift s){this.shift = s; return this;}
        public PatientBuild withSchedule(ScheduleQueryService s) { this.schedule = s; return this; }
        public PatientBuild withGuardian(Guardian g){this.guardian = g; return this;}
        public PatientBuild withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public PatientBuild withContract(Long c){this.contractId = c; return this;}
        public PatientBuild withDocumentoEPS(String d){this.documentoEPS = d; return this;}

    @Override
    public Object builder() {
            Patient o = new Patient();
            o.getGuardian()
        return Patient ;
    }
}
