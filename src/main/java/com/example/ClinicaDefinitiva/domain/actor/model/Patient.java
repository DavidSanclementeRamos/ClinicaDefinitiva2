package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.CodigoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.NoShiftAssignedException;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.PendingAppointmentsException;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.ShiftNotAvailableException;
import com.example.ClinicaDefinitiva.domain.exceptions.patient.exception.AgeBelowMinimumForRegistrationException;
import com.example.ClinicaDefinitiva.domain.exceptions.patient.exception.UnassignedResponsibleException;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Patient  {

    private PatientId patientId;
    private Person person;
    private GuardianId guardianId;
    private UserModel user;
    private Shift shift;
    private Schedule schedule;
    private LocalDateTime lastUpdate;
    private ContractId contractId;



    public Patient(Builder b) {
        this.user = b.user;
        this.shift = b.shift;
        this.guardianId = b.guardianId;
        this.schedule = b.schedule;
        this.lastUpdate = b.lastUpdate;
        this.contractId = b.contractId;
        this.patientId = b.patientId;
        this.person = b.person;
        validarResponsable(); // asegura consistencia al nacer
    }



    public static Patient registerPatient(PatientId id ,Person data,
                                          UserModel user,
                                          GuardianId guardian,
                                          LocalDateTime lastUpdate,
                                          ContractId contractId) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new AgeBelowMinimumForRegistrationException(ContextoEntidad.PATIENT, "age= " + data.getAge());
        }

        if (!data.getAge().isAdult()) {
            throw new UnassignedResponsibleException(ContextoEntidad.PATIENT, "age= " + data.getAge() );
        }
        ensureActiveUser(user);
        return new Builder()
                .withPatientId(id)
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withPhoneNumber(data.getPhoneNumber())
                .withUser(user)
                .withGuardianId(guardian)
                .withLastUpdate(lastUpdate)
                .withContractId(contractId)
                .withDocumentoEPS(data.getDocumentoEPS())
                .build();
    }

    public static Patient updatePatientContact(PatientId id ,Person data, LocalDateTime lastUpdate, UserModel user) {

        ensureActiveUser(user);
        return new Builder()
                .withPatientId(id)
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .build();
    }

    public static Patient updateDataSensible(PatientId id ,Person data, LocalDateTime lastUpdate, UserModel user, Schedule schedule) {

        ensureActiveUser(user);
        if (schedule.hasAppointmentsWithin(2)) {
            throw new PendingAppointmentsException(ContextoEntidad.PATIENT, "{days=2}");
        }

        return new Builder()
                .withPatientId(id)
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withFullName(data.getFullname())
                .build();
    }


    public void desactivar(Schedule schedule) {
        final int DAYS_TO_BLOCK_DEACTIVATION = 30;

        if (schedule.hasAppointmentsWithin(DAYS_TO_BLOCK_DEACTIVATION)) {
            throw new PendingAppointmentsException(ContextoEntidad.PATIENT, "days= "  + DAYS_TO_BLOCK_DEACTIVATION  );
        }
        ensureActiveUser();
        user.deactivate();
    }


    // un paciente puede agendar una cita si
    // el turno está libre
    // hay disponibilidad en la fecha selection
    public void canScheduleBetween(LocalDateTime start, LocalDateTime end) {

        ensureActiveUser();

        if (shift == null) {
            throw new NoShiftAssignedException(ContextoEntidad.PATIENT, "shift=null");
        }
        if (!shift.isAvailableBetween(start, end)) {
            throw new ShiftNotAvailableException(
                    ContextoEntidad.PATIENT,
                    "start=" + start + ";end=" + end
            );
        }


    }


    // una paciente puede reagendar y cancelar una cita si:
    public void validateReschedule() {
        ensureActiveUser();

    }

    // para operaciones del agregado)
    private void ensureActiveUser() {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.PATIENT, "user=" + user);
        }
    }
    // para factories.
    private static void ensureActiveUser(UserModel user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.PATIENT, "user=" + user);
        }
    }

    // Métodos auxiliares para validar responsable
    public GuardianId getGuardian(){
        return guardianId;
    }
    public  boolean requiereResponsable() {
        return !person.getAge().isAdult();
    }
    public  boolean tieneResponsable(){
        return getGuardian() != null;
    }

    public  void validarResponsable() {
         if (requiereResponsable()) {
            if (!tieneResponsable()){
                throw new UnassignedResponsibleException(ContextoEntidad.PATIENT, "No responsible party has been assigned");
            }
        }
    }

    public ContractId getContractId() {
        return contractId;
    }

    public Person getPerson() {
        return person;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public GuardianId getGuardianId() {
        return guardianId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Shift getShift() {
        return shift;
    }

    public UserModel getUser() {
        return user;
    }

    // Builder estático
    public static final class Builder {
        private Address address;
        private Age age;
        private BloodType bloodType;
        private DateOfBirth dateOfBirth;
        private String dni;
        private FullName fullname;
        private Long id;
        private PhoneNumber phoneNumber;
        private UserModel user;
        private Shift shift;
        private Appointment appointment;
        private GuardianId guardianId;
        private Schedule schedule;
        private LocalDateTime lastUpdate;
        private ContractId contractId;
        private String documentoEPS;
        private PatientId patientId;
        private Person person;


        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullName(FullName f) { this.fullname = f; return this; }
        public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withUser(UserModel u){this.user = u; return this;}
        public Builder withShift(Shift s){this.shift = s; return this;}
        public Builder withSchedule(Schedule s) { this.schedule = s; return this; }
        public Builder withGuardianId(GuardianId g){this.guardianId = g; return this;}
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withContractId(ContractId c){this.contractId = c; return this;}
        public Builder withDocumentoEPS(String d){this.documentoEPS = d; return this;}
        public Builder withPerson(Person p){this.person = p; return this;}
        public Builder withPatientId(PatientId p){this.patientId = p; return this;}
        public Patient build() {
            return new Patient(this);
        }

    }
}
