package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.NoShiftAssignedException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.PendingAppointmentsException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.appointment.exception.ShiftNotAvailableException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.patient.exception.AgeBelowMinimumForRegistrationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.patient.exception.UnassignedResponsibleException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;

import java.time.LocalDateTime;

public class Patient  {

    private final PatientId patientId;
    private Person person;
    private GuardianId guardianId;
    private UserIdentity user;
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
                                          UserIdentity user,
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


    public void updatePatientContact(Person data, UserIdentity user) {
        ensureActiveUser(user);
        this.person = this.person.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateDataSensible(Person data, UserIdentity user) {
        ensureActiveUser(user);
        if (this.schedule != null && this.schedule.hasAppointmentsWithin(2)) {
            throw new PendingAppointmentsException(ContextoEntidad.PATIENT, "{days=2}");
        }

        this.person = this.person.updateSensitive(
                data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname()
        );
        this.lastUpdate = LocalDateTime.now();
    }



    /** SERA REMOVIDO */
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
    private static void ensureActiveUser(UserIdentity user) {
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

    public UserIdentity getUser() {
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
        private UserIdentity user;
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
        public Builder withUser(UserIdentity u){this.user = u; return this;}
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
