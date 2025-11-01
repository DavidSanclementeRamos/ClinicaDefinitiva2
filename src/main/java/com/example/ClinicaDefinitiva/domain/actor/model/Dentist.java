package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.DentistMinimumAgeException;
import com.example.ClinicaDefinitiva.domain.exceptions.WeeklyAvailabilityException;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.PendingAppointmentsWithinHoursException;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class Dentist   {

    private final DentistId dentistId;
    private final Person personData;
    private final Specialties specialties;
    private final DentistAvailabilityStatus availabilityStatus;
    private final WorkingHours workingHours;
    private final UserModel user;
    private final List<TimeSlot> timeSlotList;
    private final List<WeeklyAvailability> availabilityList;
    private final Schedule schedule;
    private final LocalDateTime lastUpdate;


    private Dentist(Builder b ) {
        this.personData = b.personData;
        this.specialties = b.specialties;
        this.user = b.user;
        this.workingHours = b.workingHours;
        this.timeSlotList = List.copyOf(b.timeSlotList);
        this.availabilityList = List.copyOf(b.availabilityList);
        this.availabilityStatus = b.availabilityStatus;
        this.lastUpdate = b.lastUpdate;
        this.dentistId = b.dentistId;
        this.schedule = new Schedule(b.appointments, new WeeklyAvailability(b.timeSlotList, List.of(b.workingHours)));
    }

    // Factory method (fábrica semántica)
    public static Dentist registerDentist(
            DentistId id,
            Person data,
            Specialties specialties,
            UserModel user,
            WorkingHours workingHours,
            WeeklyAvailability weeklyAvailability,
            Collection<Appointment> initialAppointments,
            LocalDateTime lastUpdate) {
        ensureActiveUser(user);
        if (!data.getAge().isBetween(25, 130)) {
            throw new DentistMinimumAgeException(ContextoEntidad.DENTIST, "Dentist must be at least 25 years old.");
        }

        if (!weeklyAvailability.HorasRegistradas(40)) {
            throw new WeeklyAvailabilityException(ContextoEntidad.DENTIST, "Debe registrar al menos 40 horas semanales");
        }
        // build
        return new Builder()
                .withDentistId(id)
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullname(data.getFullname())
                .withDocumentoEPS("")
                .withPhoneNumber(data.getPhoneNumber())
                .withSpecialties(specialties)
                .withUser(user)
                .withWorkingHours(workingHours)
                .withTimeSlots(weeklyAvailability.getSlots())
                .withAppointments(initialAppointments)
                .withLastUpdate(lastUpdate)
                .withDocumentoEPS(data.getDocumentoEPS())
                .withAvailabilityStatus(DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE))
                .build();
        }

    public static Dentist updateDentistSensitiveData(Person data,
                                                     DentistId id,
                                                     UserModel user,
                                                     LocalDateTime lastUpdate,
                                                     Specialties specialties,
                                                     WorkingHours workingHours){
        return new Builder()
                .withDentistId(id)
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullname(data.getFullname())
                .withDocumentoEPS("")
                .withSpecialties(specialties)
                .withWorkingHours(workingHours)

                .build();
    }
    public static Dentist updateDentistContactData(DentistId id, Person data, UserModel user,LocalDateTime lastUpdate){
        return new Builder()
                .withDentistId(id)
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .withDocumentoEPS("")
                .build();
    }

    // se puede eliminar si:
    // tiene citas pendientes en X tiempo
    // el usuario está inactivo
    public void deactivateDentist(int hoursRange) {
        ensureActiveUser();
        if (schedule.hasAppointmentsWithinHours(hoursRange)) {
            throw new PendingAppointmentsWithinHoursException(
                    ContextoEntidad.DENTIST,
                    String.format("No se puede desactivar: tiene citas pendientes en las próximas %d horas", hoursRange)
            );
        }
        DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.UNAVAILABLE);
    }
    // puede agendar una cita si
    // usuario está activo
    // el horario de trabajo está dentro del rango solicitado
    public void canScheduleBetween(LocalDateTime start,LocalDateTime end ) {
        ensureActiveUser();
        if (!workingHours.isWithinRange(start, end)){
             throw new IllegalArgumentException("El horario solicitado esta fuera de la jornada laboral del odontólogo ");
        }
    }

    // un odontólogo puede tener vacaciones si:
    public void validateVacationRequest(LocalDateTime vacationStart, LocalDateTime vacationEnd) {
        ensureActiveUser();
        if (!TimeIntervalRules.isValid(vacationStart, vacationEnd)) {
            throw new IllegalArgumentException("Rango de fechas inválido.");
        }

        List<Appointment> conflicts = schedule.getAppointments().stream()
                .filter(a -> TimeIntervalRules.overlaps(
                        a.getStart(), a.getEnd(), vacationStart, vacationEnd))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("No se puede tomar vacaciones: hay " +
                    conflicts.size() + " cita" + (conflicts.size() > 1 ? "s" : "") +
                    " en conflicto.");
        }
    }
    // Un odontólogo puede reagendar una cita si:
    // usuario está activo.
    // La nueva fecha está en su horario laboral
    public void validateReschedule(LocalDateTime start, LocalDateTime end) {
        ensureActiveUser();
        if (!canWorkBetween(start, end)) {
            throw new IllegalArgumentException("La nueva fecha no está dentro del horario laboral del odontólogo.");
        }
    }
    /**
     * Verifica si un intervalo de tiempo está dentro del horario laboral del odontólogo.
     */
    public boolean canWorkBetween(LocalDateTime start, LocalDateTime end) {
        return workingHours != null && workingHours.isWithinRange(start, end);
    }

    // Validar cumplimiento de horas trabajadas
    public boolean isCompliantWithDeclaredWorkingHours() {
        return workingHours.isCompliantWithWorkingHours(schedule.getWeeklyAvailability());
    }
    // para operaciones del agregado)
    private void ensureActiveUser() {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }
    // para factories.
    private static void ensureActiveUser(UserModel user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }


    // Getters inmutables
    //public List<TimeSlot> getTimeSlotList() { return List.copyOf(timeSlotList); }
    public List<WeeklyAvailability> getAvailabilityList() { return List.copyOf(availabilityList); }
    public Schedule getSchedule() { return schedule; }
    public UserModel getUser() { return user; }
    public DentistAvailabilityStatus getAvailabilityStatus() {return availabilityStatus;}
    public LocalDateTime getLastUpdate() {return lastUpdate;}
    public Specialties getSpecialties() {return specialties;}
    public List<TimeSlot> getTimeSlotList() {return timeSlotList;}
    public WorkingHours getWorkingHours() {return workingHours;}
    public DentistId getDentistId() {return dentistId;}
    public Person getPersonData() {return personData;}

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
        private Specialties specialties;
        private UserModel user;
        private WorkingHours workingHours;
        private List<TimeSlot> timeSlotList = new ArrayList<>();
        private List<WeeklyAvailability> availabilityList = new ArrayList<>();
        private Collection<Appointment> appointments = new ArrayList<>();
        private DentistAvailabilityStatus availabilityStatus;
        private LocalDateTime lastUpdate;
        private String documentoEPS;
        private Person personData;
        private DentistId dentistId;

        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullname(FullName f) { this.fullname = f; return this; }
       // public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withSpecialties(Specialties s) { this.specialties = s; return this; }
        public Builder withUser(UserModel u) { this.user = u; return this; }
        public Builder withWorkingHours(WorkingHours w) { this.workingHours = w; return this; }
        public Builder withTimeSlots(Collection<TimeSlot> slots) { this.timeSlotList = new ArrayList<>(slots); return this; }
        public Builder withAvailabilityList(Collection<WeeklyAvailability> availability) { this.availabilityList = new ArrayList<>(availability); return this; }
        public Builder withAppointments(Collection<Appointment> apps) { this.appointments = apps == null ? new ArrayList<>() : new ArrayList<>(apps); return this; }
        public Builder withAvailabilityStatus(DentistAvailabilityStatus s) { this.availabilityStatus = s; return this; }
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withDocumentoEPS(String d){this.documentoEPS = d; return this;}
        public Builder withPerson(Person p){this.personData = p; return this;}
        public Builder withDentistId(DentistId d){this.dentistId = d; return this;}
        public Dentist build() {

            return new Dentist(this);
        }
    }

}
